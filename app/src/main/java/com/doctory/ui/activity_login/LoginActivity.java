package com.doctory.ui.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;

import com.doctory.R;
import com.doctory.databinding.ActivityLoginBinding;
import com.doctory.language.Language;
import com.doctory.models.LoginModel;
import com.doctory.mvp.activity_login_presenter.ActivityLoginPresenter;
import com.doctory.mvp.activity_login_presenter.ActivityLoginView;

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
    }

    @Override
    public void onLoginValid() {

    }
}