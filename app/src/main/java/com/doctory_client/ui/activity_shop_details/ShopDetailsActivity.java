package com.doctory_client.ui.activity_shop_details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.doctory_client.R;
import com.doctory_client.adapters.HoursAdapter;
import com.doctory_client.adapters.ImagePagerAdapter;
import com.doctory_client.databinding.ActivityShopDetailsBinding;
import com.doctory_client.databinding.DialogHoursBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.HourModel;
import com.doctory_client.models.NearbyModel;
import com.doctory_client.models.PhotosModel;
import com.doctory_client.models.PlaceDetailsModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_shop_details_mvp.ActivityShopDetailsPresenter;
import com.doctory_client.mvp.activity_shop_details_mvp.ActivityShopDetailsView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.remote.Api;
import com.doctory_client.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopDetailsActivity extends AppCompatActivity implements ActivityShopDetailsView {
    private ActivityShopDetailsBinding binding;
    private List<PhotosModel> photosModelList;
    private NearbyModel.Result placeModel;
    private String lang;
    private List<HourModel> hourModelList;
    private ImagePagerAdapter imagePagerAdapter;
    private UserModel userModel;
    private Preferences preferences;
    private ActivityShopDetailsPresenter presenter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_details);
        getDataFromIntent();
        initView();
    }


    private void getDataFromIntent()
    {

        Intent intent = getIntent();
        placeModel = (NearbyModel.Result) intent.getSerializableExtra("data");

    }
    private void initView()
    {
        presenter = new ActivityShopDetailsPresenter(this,this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        hourModelList = new ArrayList<>();
        photosModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setDistance("");
        binding.flBack.setOnClickListener(v -> finish());
        binding.tvShow.setOnClickListener(v -> {
            if (hourModelList.size()>0){
                createDialogAlert();
            }else {
                Toast.makeText(this, R.string.work_hour_not_aval, Toast.LENGTH_SHORT).show();
            }
        });

        if (placeModel.getPhotos()!=null){
            if (placeModel.getPhotos().size()>0)
            {
                String url = Tags.IMAGE_Places_URL+placeModel.getPhotos().get(0).getPhoto_reference()+"&key="+getString(R.string.map_api_key);
                Picasso.get().load(Uri.parse(url)).fit().into(binding.image);

            }else
            {
                Picasso.get().load(Uri.parse(placeModel.getIcon())).fit().into(binding.image);

            }
        }
        else {
            Picasso.get().load(Uri.parse(placeModel.getIcon())).fit().into(binding.image);

        }

        presenter.getPlaceDetails(placeModel.getPlace_id(),lang);
    }



    private void updateHoursUI(PlaceDetailsModel body)
    {

        if (body.getResult().getReviews()!=null&&body.getResult().getReviews().size()>0){
            placeModel.setReviews(body.getResult().getReviews());


        }else {
            placeModel.setReviews(new ArrayList<>());
        }
        if (body.getResult().getPhotos()!=null&&body.getResult().getPhotos().size()>0){
            placeModel.setPhotosModels(body.getResult().getPhotos());
            photosModelList.clear();
            photosModelList.addAll(placeModel.getPhotosModels());
            imagePagerAdapter = new ImagePagerAdapter(photosModelList,this);
            binding.pager.setAdapter(imagePagerAdapter);
            binding.tab.setupWithViewPager(binding.pager);

            for(int i=0; i < binding.tab.getTabCount(); i++) {
                View tab = ((ViewGroup) binding.tab.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(2, 0, 2, 0);
                tab.requestLayout();
            }

        }else {
            placeModel.setPhotosModels(new ArrayList<>());
        }

        if (body.getResult().getOpening_hours() != null) {
            placeModel.setOpen(body.getResult().getOpening_hours().isOpen_now());
            if (body.getResult().getOpening_hours().getWeekday_text()!=null&&body.getResult().getOpening_hours().getWeekday_text().size()>0){
                placeModel.setWork_hours(body.getResult().getOpening_hours());
                binding.tvStatus.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                binding.icon.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));
                placeModel.setOpen(true);
                hourModelList.clear();
                hourModelList.addAll(getHours());
                if (hourModelList.size()>0){
                    binding.tvHours.setText(hourModelList.get(0).getTime());

                }

            }else {
                binding.tvStatus.setTextColor(ContextCompat.getColor(this,R.color.color1));
                binding.icon.setColorFilter(ContextCompat.getColor(this,R.color.color1));

                placeModel.setOpen(false);

            }


        } else {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this,R.color.color1));
            binding.icon.setColorFilter(ContextCompat.getColor(this,R.color.color1));


            placeModel.setOpen(false);

        }




        binding.progBar.setVisibility(View.GONE);
        binding.setDistance(String.format(Locale.ENGLISH,"%.2f",placeModel.getDistance()));
        binding.setModel(placeModel);
        binding.ll.setVisibility(View.VISIBLE);
        binding.ll.setVisibility(View.VISIBLE);


    }
    private List<HourModel> getHours()
    {
        List<HourModel> list = new ArrayList<>();

        for (String time: placeModel.getWork_hours().getWeekday_text()){

            String day = time.split(":", 2)[0].trim();
            String t = time.split(":",2)[1].trim();
            HourModel hourModel = new HourModel(day,t);
            list.add(hourModel);




        }

        return list;
    }



    private void createDialogAlert()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogHoursBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_hours, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        HoursAdapter adapter = new HoursAdapter(hourModelList,this);
        binding.recView.setAdapter(adapter);

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(PlaceDetailsModel model) {
        updateHoursUI(model);
    }
}