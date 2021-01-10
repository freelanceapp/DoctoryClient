package com.doctory_client.ui.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityHomeBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_home_mvp.ActivityHomePresenter;
import com.doctory_client.mvp.activity_home_mvp.HomeActivityView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;
import com.doctory_client.ui.activity_doctor.DoctorActivity;
import com.doctory_client.ui.activity_login.LoginActivity;
import com.doctory_client.ui.activity_reservision_detials.ReservationDetialsActivity;
import com.doctory_client.ui.notification_activity.NotificationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private ActivityHomePresenter presenter;
    private double lat = 0.0, lng = 0.0;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);
    }

    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
        presenter = new ActivityHomePresenter(this, this, fragmentManager, lat, lng);
        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                presenter.manageFragments(item);
                return true;
            }
        });

        binding.flSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, DoctorActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            startActivity(intent);
        });
        binding.flnotification.setOnClickListener(view -> {
            if(userModel!=null){
            Intent intent = new Intent(this, NotificationActivity.class);

            startActivity(intent);}
            else {
presenter.notlogin();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            presenter.displayFragmentAppointment();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        presenter.backPress();
    }

    @Override
    public void onHomeFragmentSelected() {
        binding.navigationView.setSelectedItemId(R.id.home);
    }


    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onFinished() {
        finish();
    }


    public void displayfragmentconsolt() {
       // presenter.displayFragmentAppointment();
        binding.navigationView.setSelectedItemId(binding.navigationView.getMenu().getItem(1).getItemId());
    }


    public void Logout() {
        presenter.logout(userModel);
    }
    @Override
    public void onLoad() {
        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }

    @Override
    public void logout() {
        preferences.clear(this);
        navigateToSignInActivity();
    }

    @Override
    public void notlogin() {
        Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));

    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
    private void navigateToSignInActivity() {

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Paper.init(this);
            Paper.book().write("lang",lang);
            Language.updateResources(this,lang);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        },1500);


    }
}