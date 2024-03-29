package com.doctory_client.ui.activity_contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityContactUsBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.ContactUsModel;
import com.doctory_client.models.SettingModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.mvp.activity_contactus_mvp.ActivityContactusPresenter;
import com.doctory_client.mvp.activity_contactus_mvp.ActivityContactusView;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.share.Common;

import io.paperdb.Paper;

public class ContactusActivity extends AppCompatActivity implements ActivityContactusView {
    private ActivityContactUsBinding binding;

    private ContactUsModel contactUsModel;
    private ActivityContactusPresenter presenter;
    private Preferences preferences;
    private String lang;
    private ProgressDialog dialog2;
    private UserModel userModel;
    private SettingModel setting;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(userModel);
        Paper.init(this);

        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        contactUsModel = new ContactUsModel();
        binding.setContactModel(contactUsModel);

        presenter = new ActivityContactusPresenter(this, this);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkData(contactUsModel);
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting != null && setting.getSettings() != null && setting.getSettings().getFacebook() != null) {
                    presenter.open(setting.getSettings().getFacebook());
                }
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting != null && setting.getSettings() != null && setting.getSettings().getWhatsapp() != null) {
                    presenter.open("https://api.whatsapp.com/send?phone="+setting.getSettings().getWhatsapp());
                }
            }
        });
        presenter.getSetting();

    }


    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onContactVaild() {
        finish();

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ContactusActivity.this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onFailed() {
        Toast.makeText(ContactusActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(ContactusActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onsetting(SettingModel body) {
        this.setting = body;

        if (setting.getSettings().getWhatsapp() == null) {
            binding.whatsapp.setVisibility(View.GONE);
        }
        if (setting.getSettings().getFacebook() == null) {
            binding.facebook.setVisibility(View.GONE);
        }


    }
    @Override
    public void ViewSocial(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);

    }

}