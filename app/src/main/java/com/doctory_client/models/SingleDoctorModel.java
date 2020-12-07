package com.doctory_client.models;

import java.io.Serializable;

public class SingleDoctorModel implements Serializable {
    private int id;
    private String code;
    private String user_type;
    private String name;
    private String email;
    private String phone_code;
    private String phone;
    private String gender;
    private String address;
    private double latitude;
    private double longitude;
    private int specialization_id;
    private int job_degree_id;
    private int city_id;
    private String license_img;
    private String logo;
    private String banner;
    private String birth_day;
    private String blood_type;
    private String details;
    private double rates;
    private int app_cost;
    private int detection_price;
    private String appointment_time;
    private String is_emergency;
    private String email_verified_at;
    private String is_blocked;
    private String is_login;
    private String logout_time;
    private int is_confirmed;
    private String confirmation_code;
    private String forget_password_code;
    private String software_type;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private double distance;
    private Job job_degree_fk;
    private SpecializationModel specialization_fk;
    private CityModel city_fk;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getSpecialization_id() {
        return specialization_id;
    }

    public int getJob_degree_id() {
        return job_degree_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getLicense_img() {
        return license_img;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public String getDetails() {
        return details;
    }

    public double getRates() {
        return rates;
    }

    public int getApp_cost() {
        return app_cost;
    }

    public int getDetection_price() {
        return detection_price;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public String getForget_password_code() {
        return forget_password_code;
    }

    public String getSoftware_type() {
        return software_type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public double getDistance() {
        return distance;
    }

    public Job getJob_degree_fk() {
        return job_degree_fk;
    }

    public SpecializationModel getSpecialization_fk() {
        return specialization_fk;
    }

    public CityModel getCity_fk() {
        return city_fk;
    }

    public class Job implements Serializable {
        private int id;
        private String title;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

}
