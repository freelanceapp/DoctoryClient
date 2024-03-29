package com.doctory_client.ui.activity_google_nearby_places;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.adapters.NearbyAdapter;
import com.doctory_client.databinding.ActivityGoogleNearybyPlacesBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.NearbyModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.ui.activity_shop_details.ShopDetailsActivity;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleNearybyPlacesActivity extends AppCompatActivity {
    private ActivityGoogleNearybyPlacesBinding binding;
    private String query="";
    private List<NearbyModel.Result> resultList;
    private NearbyAdapter adapter;
    private double user_lat;
    private double user_lng;
    private String ar_title="";
    private String en_title="";
    private SkeletonScreen skeletonScreen;
    private String lang;
    private boolean hasManyPages = false;
    private boolean isLoading = false;
    private String next_page="";
    private Preferences preferences;
    private UserModel userModel;
    private List<NearbyModel.Result> resultListFiltered;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_google_nearyby_places);
        getDataFromIntent();
        initView();

    }

    private void initView() {
        resultList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        if (lang.equals("ar")){
            binding.setTitle(ar_title);

        }else {
            binding.setTitle(en_title);

        }


        resultList = new ArrayList<>();
        binding.recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NearbyAdapter(resultList,this,user_lat,user_lng);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .count(5)
                .frozen(false)
                .shimmer(true)
                .show();

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    int totalItem = adapter.getItemCount();
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recView.getLayoutManager();
                    int pos = manager.findLastCompletelyVisibleItemPosition();
                    if (hasManyPages&&totalItem>=20&&(totalItem-pos==2)&&!isLoading){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });


        binding.llBack.setOnClickListener(v -> super.onBackPressed());
        getShops(query);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        user_lat = intent.getDoubleExtra("lat",0.0);
        user_lng = intent.getDoubleExtra("lng",0.0);
        query = intent.getStringExtra("query");
        ar_title = intent.getStringExtra("ar_title");
        en_title = intent.getStringExtra("en_title");
    }

    private void getShops(String query) {
        resultList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        skeletonScreen.show();

        String loc = user_lat+","+user_lng;
        Log.e("loc",loc);
        Api.getService("https://maps.googleapis.com/maps/api/")
                .nearbyPlaceRankBy(loc,query,"distance",lang,"",getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyModel>() {
                    @Override
                    public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            if (response.body().getStatus().equals("OK")){

                                if (response.body().getNext_page_token()!=null){
                                    hasManyPages = true;
                                    next_page = response.body().getNext_page_token();
                                }else {
                                    hasManyPages = false;
                                    next_page = "";
                                }

                                if (response.body().getResults().size()>0)
                                {
                                    calculateDistance(response.body().getResults());
                                    binding.tvNoData.setVisibility(View.GONE);

                                }else
                                {
                                    skeletonScreen.hide();
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }else {
                                skeletonScreen.hide();
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }

                        }else
                        {

                            skeletonScreen.hide();

                            try {
                                Log.e("error_code",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyModel> call, Throwable t) {
                        try {


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(GoogleNearybyPlacesActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                }
                                else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }

                                else {
                                    Toast.makeText(GoogleNearybyPlacesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            Log.e("Error",t.getMessage());
                            skeletonScreen.hide();
                        }catch (Exception e)
                        {

                        }
                    }
                });
    }

    private void loadMore() {

        resultList.add(null);
        adapter.notifyItemInserted(resultList.size()-1);


        String loc = user_lat+","+user_lng;
        Log.e("loc",loc);
        Api.getService("https://maps.googleapis.com/maps/api/")
                .nearbyPlaceRankBy(loc,query,"distance",lang,next_page,getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyModel>() {
                    @Override
                    public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {


                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            if (response.body().getStatus().equals("OK")){

                                if (response.body().getNext_page_token()!=null){
                                    hasManyPages = true;
                                    next_page = response.body().getNext_page_token();
                                }else {
                                    hasManyPages = false;
                                    next_page = "";
                                }
                                if (response.body().getResults().size()>0)
                                {

                                    calculateDistanceLoadMore(response.body().getResults());
                                }else {
                                    if (resultList.get(resultList.size()-1)==null){
                                        resultList.remove(resultList.size()-1);
                                        adapter.notifyItemRemoved(resultList.size()-1);
                                    }
                                }
                            }

                        }else
                        {


                            try {
                                Log.e("error_code",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(GoogleNearybyPlacesActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                                }
                                else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                                else {
                                    Toast.makeText(GoogleNearybyPlacesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e)
                        {

                        }
                    }
                });




    }

    private void calculateDistance(List<NearbyModel.Result> results){
        resultListFiltered = new ArrayList<>();

        for (int i =0 ;i<results.size();i++){
            NearbyModel.Result result = results.get(i);

            if (result!=null){


                LatLng user_location = new LatLng(user_lat,user_lng);
                LatLng place_location = new LatLng(result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng());
                double distance = getDistance(user_location,place_location);
                result.setDistance(distance);
                resultListFiltered.add(result);
            }

        }


        if (resultListFiltered.size()>0){
            skeletonScreen.hide();
            resultList.clear();
            resultList.addAll(resultListFiltered);
            adapter.notifyDataSetChanged();
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }

    }

    private void calculateDistanceLoadMore(List<NearbyModel.Result> results){

        List<NearbyModel.Result> resultListFiltered = new ArrayList<>();

        for (int i =0 ;i<results.size();i++){
            NearbyModel.Result result = results.get(i);

            if (result!=null){

                LatLng user_location = new LatLng(user_lat,user_lng);
                LatLng place_location = new LatLng(result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng());
                double distance = getDistance(user_location,place_location);
                result.setDistance(distance);
                resultListFiltered.add(result);
            }

        }



        isLoading = false;
        if (resultList.get(resultList.size()-1)==null){
            resultList.remove(resultList.size()-1);
            adapter.notifyItemRemoved(resultList.size()-1);
        }
        int oldPos = resultList.size();
        resultList.addAll(resultListFiltered);

        int newPos = resultList.size();
        adapter.notifyItemRangeChanged(oldPos,newPos);



    }


    private double getDistance(LatLng latLng1,LatLng latLng2){
        return SphericalUtil.computeDistanceBetween(latLng1,latLng2)/1000;
    }
    public void setShopData(NearbyModel.Result placeModel) {
        Intent intent = new Intent(this, ShopDetailsActivity.class);
        intent.putExtra("data",placeModel);
        startActivity(intent);

    }
}