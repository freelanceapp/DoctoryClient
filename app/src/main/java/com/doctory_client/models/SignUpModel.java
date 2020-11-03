package com.doctory_client.models;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.doctory_client.BR;
import com.doctory_client.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SignUpModel extends BaseObservable implements Serializable {
    private String imageUrl;
    private String name;
    private String gender;
    private String birth_date;
    private String blood_type;
    private boolean have_diseases;
    private String phone_code;
    private String phone;
    private List<DiseaseModel> diseaseModelList;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_birth_date = new ObservableField<>();


    public SignUpModel(String phone_code, String phone) {
        this.phone_code = phone_code;
        this.phone = phone;
        this.imageUrl="";
        this.name = "";
        this.gender="";
        this.birth_date ="";
        this.blood_type = "";
        have_diseases = false;
        diseaseModelList = new ArrayList<>();
    }

    public boolean isDataValid(Context context){
        if (!name.isEmpty()&&
                !gender.isEmpty()&&
                !birth_date.isEmpty()&&
                !blood_type.isEmpty()
        ){
            if (have_diseases){
                if (diseaseModelList.size()>0){
                    return true;
                }else {
                    return false;
                }
            }else {
                return true;
            }
        }else {
            if (name.isEmpty()){
                error_name.set(context.getString(R.string.field_req));
            }else {
                error_name.set(null);
            }

            if (gender.isEmpty()){
                Toast.makeText(context, context.getString(R.string.ch_gender), Toast.LENGTH_SHORT).show();
            }

            if (birth_date.isEmpty()){
                error_birth_date.set(context.getString(R.string.field_req));
            }else {
                error_birth_date.set(null);
            }

            if (blood_type.isEmpty()){
                Toast.makeText(context, context.getString(R.string.ch_blood), Toast.LENGTH_SHORT).show();
            }

            if (have_diseases){
                if (diseaseModelList.size()==0){
                    Toast.makeText(context, R.string.ch_diseases, Toast.LENGTH_SHORT).show();

                }
            }
            return false;
        }
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Bindable
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
        notifyPropertyChanged(BR.birth_date);
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public boolean isHave_diseases() {
        return have_diseases;
    }

    public void setHave_diseases(boolean have_diseases) {
        this.have_diseases = have_diseases;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<DiseaseModel> getDiseaseModelList() {
        return diseaseModelList;
    }

    public void setDiseaseModelList(List<DiseaseModel> diseaseModelList) {
        this.diseaseModelList = diseaseModelList;
    }
}
