package com.doctory_client.ui.activity_doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.adapters.CityAdapter;
import com.doctory_client.adapters.DoctorsAdapter;
import com.doctory_client.adapters.FilterAdapter;
import com.doctory_client.adapters.SpicialAdapter;
import com.doctory_client.databinding.ActivityDoctorBinding;
import com.doctory_client.databinding.DoctorRowBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.CityModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.FilterModel;
import com.doctory_client.models.SingleDoctorModel;
import com.doctory_client.models.SpecializationModel;
import com.doctory_client.mvp.activity_doctors_mvp.ActivityDoctorsPresenter;
import com.doctory_client.mvp.activity_doctors_mvp.DoctorsActivityView;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_doctor_details.DoctorDetailsActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DoctorActivity extends AppCompatActivity implements DoctorsActivityView {
    private ActivityDoctorBinding binding;
    private ActivityDoctorsPresenter presenter;
    private String lang;
    private double lat = 0.0, lng = 0.0;
    private float distance = 0f;
    private String city_id = "";
    private String specialization_id = "all";
    private DoctorsAdapter adapter;
    private FilterAdapter filterAdapter;
    private List<FilterModel> filterModelList;
    private List<SpecializationModel> specializationModels;
    private SpicialAdapter spicialAdapter;
    private List<CityModel> cityModels;
    private List<SingleDoctorModel> singleDoctorModelList;
    private CityAdapter cityAdapter;
    private String query, near = "off";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);

    }

    private void initView() {
        presenter = new ActivityDoctorsPresenter(this, this);
        singleDoctorModelList = new ArrayList<>();
        filterModelList = new ArrayList<>();
        specializationModels = new ArrayList<>();
        cityModels = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        adapter = new DoctorsAdapter(singleDoctorModelList, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        //binding.progBar.setVisibility(View.GONE);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);
        binding.llBack.setOnClickListener(view -> {
            presenter.backPress();
        });
        binding.llSpecialization.setOnClickListener(view -> openSheet(1));
        binding.llNearby.setOnClickListener(view -> openSheet(2));
        binding.llCity.setOnClickListener(view -> openSheet(3));

        binding.imageCloseSpecialization.setOnClickListener(view -> closeSheet(1));
        binding.imageCloseNearby.setOnClickListener(view -> closeSheet(2));
        binding.imageCloseCity.setOnClickListener(view -> closeSheet(3));


        filterAdapter = new FilterAdapter(filterModelList, this);
        binding.recViewFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewFilter.setAdapter(filterAdapter);
        spicialAdapter = new SpicialAdapter(specializationModels, this);
        binding.recViewSpecialization.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewSpecialization.setAdapter(spicialAdapter);
        cityAdapter = new CityAdapter(cityModels, this);
        binding.recViewCity.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewCity.setAdapter(cityAdapter);
        updateDistanceUI(1f);
        binding.seekBarDistance.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                distance = seekParams.progressFloat;
                updateDistanceUI(distance);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(DoctorActivity.this, binding.editQuery);
                    presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);
                    return false;
                } else {
                    query = "";
                }
                presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);

            }
            return false;
        });
        binding.btnDistance.setOnClickListener(view -> {
            int pos = getItemPos("distance");
            String title = distance + " " + getString(R.string.km);

            if (pos == -1) {
                FilterModel filterModel = new FilterModel(title, "distance");
                filterModelList.add(filterModel);
                filterAdapter.notifyDataSetChanged();
            } else {
                FilterModel filterModel = filterModelList.get(pos);
                filterModel.setTitle(title);
                filterModelList.set(pos, filterModel);
                filterAdapter.notifyItemChanged(pos);
            }
            binding.llFilter.setVisibility(View.VISIBLE);
            closeSheet(2);
            near = "on";
            presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);

        });


    }


    private void openSheet(int type) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        if (type == 1) {
            binding.flSpecializationSheet.clearAnimation();
            binding.flSpecializationSheet.startAnimation(animation);

            presenter.getSpecilization(type);

        } else if (type == 2) {
            //  binding.flNearBySheet.clearAnimation();
            //binding.flNearBySheet.startAnimation(animation);
            near = "off";
            int pos = getItemPos("distance");
            String title = getResources().getString(R.string.nearby);

            if (pos == -1) {
                FilterModel filterModel = new FilterModel(title, "distance");
                filterModelList.add(filterModel);
                filterAdapter.notifyDataSetChanged();
            } else {
                FilterModel filterModel = filterModelList.get(pos);
                filterModel.setTitle(title);
                filterModelList.set(pos, filterModel);
                filterAdapter.notifyItemChanged(pos);
            }
            binding.llFilter.setVisibility(View.VISIBLE);
            closeSheet(2);
            near = "on";
            presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);

        } else if (type == 3) {
            binding.flCitySheet.clearAnimation();
            binding.flCitySheet.startAnimation(animation);

            presenter.getcities(type);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (type == 1) {
                    binding.flSpecializationSheet.setVisibility(View.VISIBLE);


                } else if (type == 2) {
                    binding.flNearBySheet.setVisibility(View.VISIBLE);


                } else if (type == 3) {
                    binding.flCitySheet.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet(int type) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        if (type == 1) {
            binding.flSpecializationSheet.clearAnimation();
            binding.flSpecializationSheet.startAnimation(animation);


        } else if (type == 2) {
            binding.flNearBySheet.clearAnimation();
            binding.flNearBySheet.startAnimation(animation);


        } else if (type == 3) {
            binding.flCitySheet.clearAnimation();
            binding.flCitySheet.startAnimation(animation);


        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == 1) {
                    binding.flSpecializationSheet.setVisibility(View.GONE);


                } else if (type == 2) {
                    binding.flNearBySheet.setVisibility(View.GONE);


                } else if (type == 3) {
                    binding.flCitySheet.setVisibility(View.GONE);


                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void updateDistanceUI(float distance) {

        binding.tvDistance.setText(String.format(Locale.ENGLISH, "%s %s", distance, getString(R.string.km)));

    }


    private int getItemPos(String type) {
        int pos = -1;
        for (int index = 0; index < filterModelList.size(); index++) {
            FilterModel filterModel = filterModelList.get(index);
            if (filterModel.getType().equals(type)) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    public void deleteFilter(FilterModel model, int adapterPosition) {
        filterModelList.remove(adapterPosition);
        filterAdapter.notifyItemRemoved(adapterPosition);
        if (model.getType().equals("distance")) {
            distance = 1.0f;
            binding.seekBarDistance.setProgress(distance);
            near = "off";
        } else if (model.getType().equals("specialization")) {
            specialization_id = "all";
        } else if (model.getType().equals("city")) {
            city_id = "all";
        }

        if (filterModelList.size() > 0) {
            binding.llFilter.setVisibility(View.VISIBLE);
        } else {
            binding.llFilter.setVisibility(View.GONE);

        }
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);
    }

    public void setItemData(SingleDoctorModel doctorModel, DoctorRowBinding binding, int adapterPosition) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtra("data",doctorModel);
        List<Pair<View, String>> pairs = new ArrayList<>();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(binding.image, binding.image.getTransitionName()),
                Pair.create(binding.rateBar, binding.rateBar.getTransitionName()),
                Pair.create(binding.llAddress, binding.llAddress.getTransitionName()),
                Pair.create(binding.tvDistance, binding.tvDistance.getTransitionName())
        );
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onFinished() {
        finish();
    }


    @Override
    public void onProgressShow(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            binding.progBarCity.setVisibility(View.VISIBLE);

        } else if (type == 2) {
            binding.progBar.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onProgressHide(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.GONE);
        } else if (type == 3) {
            binding.progBarCity.setVisibility(View.GONE);

        } else if (type == 2) {
            binding.progBar.setVisibility(View.GONE);

        }

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(DoctorActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(AllSpiclixationModel allSpiclixationModel) {
        specializationModels.clear();
        specializationModels.addAll(allSpiclixationModel.getData());
        spicialAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccesscitie(AllCityModel allCityModel) {
        cityModels.clear();
        cityModels.addAll(allCityModel.getData());
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void ondoctorsucess(DoctorModel body) {
        singleDoctorModelList.clear();
        singleDoctorModelList.addAll(body.getData());
        adapter.notifyDataSetChanged();
    }

    public void setspicialization(int id) {
//        specializationModels.clear();
//        spicialAdapter.notifyDataSetChanged();
        int pos = getItemPos("specialization");
        String title = getString(R.string.specialization);

        if (pos == -1) {
            FilterModel filterModel = new FilterModel(title, "specialization");
            filterModelList.add(filterModel);
            filterAdapter.notifyDataSetChanged();
        } else {
            FilterModel filterModel = filterModelList.get(pos);
            filterModel.setTitle(title);
            filterModelList.set(pos, filterModel);
            filterAdapter.notifyItemChanged(pos);
        }
        binding.llFilter.setVisibility(View.VISIBLE);
        specialization_id = id + "";

        closeSheet(1);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);

    }

    public void setcity(int id) {
//        specializationModels.clear();
//        spicialAdapter.notifyDataSetChanged();
        int pos = getItemPos("city");
        String title = getString(R.string.city);

        if (pos == -1) {
            FilterModel filterModel = new FilterModel(title, "city");
            filterModelList.add(filterModel);
            filterAdapter.notifyDataSetChanged();
        } else {
            FilterModel filterModel = filterModelList.get(pos);
            filterModel.setTitle(title);
            filterModelList.set(pos, filterModel);
            filterAdapter.notifyItemChanged(pos);
        }
        binding.llFilter.setVisibility(View.VISIBLE);
        city_id = id + "";

        closeSheet(3);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, 2);

    }

}