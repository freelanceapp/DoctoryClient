package com.doctory_client.ui.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;

import com.doctory_client.R;
import com.doctory_client.databinding.ActivityLoginBinding;
import com.doctory_client.language.Language;
import com.doctory_client.models.LoginModel;
import com.doctory_client.mvp.activity_login_presenter.ActivityLoginPresenter;
import com.doctory_client.mvp.activity_login_presenter.ActivityLoginView;
import com.doctory_client.ui.activity_confirm_code.ConfirmCodeActivity;
import com.doctory_client.ui.activity_home.HomeActivity;
import com.doctory_client.ui.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements ActivityLoginView {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityLoginPresenter presenter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        initView();
    }

    private void initView() {
        model = new LoginModel();
        binding.tv1.setText(Html.fromHtml(getString(R.string.login2)));
        binding.tvSkip.setPaintFlags(binding.tvSkip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        binding.setModel(model);
        presenter = new ActivityLoginPresenter(this,this);
        binding.btnLogin.setOnClickListener(view -> {
            presenter.checkData(model);
        });

        binding.tvSkip.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLoginValid() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code",model.getPhone_code());
        intent.putExtra("phone",model.getPhone());
        startActivity(intent);
        finish();

    }
}