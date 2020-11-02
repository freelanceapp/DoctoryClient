package com.doctory.mvp.activity_splash_mvp;

import android.content.Context;
import android.os.Handler;

import com.doctory.models.UserModel;
import com.doctory.models.UserSettingsModel;
import com.doctory.preferences.Preferences;

public class SplashPresenter {
    private Context context;
    private SplashView view;
    private Preferences preferences;
    private UserModel userModel;
    private UserSettingsModel userSettingsModel;

    public SplashPresenter(Context context, SplashView view) {
        this.context = context;
        this.view = view;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
        userSettingsModel = preferences.getUserSettings(context);
        delaySplash();
    }

    private void delaySplash(){
        new Handler().postDelayed(()->{

            if (userSettingsModel!=null&&userSettingsModel.isLanguageSelected()){
                if (userModel==null){
                    view.onNavigateToLoginActivity();
                }else {
                    view.onNavigateToHomeActivity();
                }
            }else {
                view.onNavigateToLanguageActivity();

            }



        },2000);
    }
}
