package com.doctory_client.services;

import com.doctory_client.models.AllAdviceModel;
import com.doctory_client.models.AllCityModel;
import com.doctory_client.models.AllDiseasesModel;
import com.doctory_client.models.AllSpiclixationModel;
import com.doctory_client.models.ApointmentModel;
import com.doctory_client.models.DiseaseModel;
import com.doctory_client.models.DoctorModel;
import com.doctory_client.models.NearbyModel;
import com.doctory_client.models.NotificationModel;
import com.doctory_client.models.PlaceDetailsModel;
import com.doctory_client.models.PlaceGeocodeData;
import com.doctory_client.models.PlaceMapDetailsData;
import com.doctory_client.models.ReservisionTimeModel;
import com.doctory_client.models.SingleAdviceModel;
import com.doctory_client.models.SingleDataDoctorModel;
import com.doctory_client.models.Slider_Model;
import com.doctory_client.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("place/details/json")
    Call<PlaceDetailsModel> getPlaceDetails(@Query(value = "placeid") String placeid,
                                            @Query(value = "fields") String fields,
                                            @Query(value = "language") String language,
                                            @Query(value = "key") String key
    );

    @GET("place/nearbysearch/json")
    Call<NearbyModel> nearbyPlaceRankBy(@Query(value = "location") String location,
                                        @Query(value = "keyword") String keyword,
                                        @Query(value = "rankby") String rankby,
                                        @Query(value = "language") String language,
                                        @Query(value = "pagetoken") String pagetoken,
                                        @Query(value = "key") String key
    );
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String user_token
    );
    @GET("api/get-diseases")
    Call<AllDiseasesModel> getdiseas();
    @FormUrlEncoded
    @POST("api/patient-register")
    Call<UserModel> signup(@Field("phone_code") String phone_code,
                           @Field("phone") String phone,
                           @Field("name") String name,
                           @Field("birth_day")String birth_day,
                           @Field("blood_type")String blood_type,
                           @Field("gender")String gender,
                           @Field("user_type")String user_type,
                           @Field("software_type") String software_type,
                           @Field("diseases_ids[]") List<String> diseases_ids




    );
    @Multipart
    @POST("api/patient-register")
    Call<UserModel> signup(@Part("phone_code") RequestBody phone_code,
                           @Part("phone") RequestBody phone,
                           @Part("name") RequestBody name,
                           @Part("birth_day")RequestBody birth_day,
                           @Part("blood_type")RequestBody blood_type,
                           @Part("gender")RequestBody gender,
                           @Part("user_type")RequestBody user_type,
                           @Part("software_type") RequestBody software_type,
                           @Part("diseases_ids[]") List<RequestBody> diseases_ids,
                           @Part MultipartBody.Part image





    );
    @GET("api/get-specializations")
    Call<AllSpiclixationModel> getspicailest();
    @GET("api/get-cities")
    Call<AllCityModel> getcities();
    @GET("api/sliders")
    Call<Slider_Model> get_slider();

    @GET("api/search-general")
    Call<DoctorModel> getdoctors(
            @Query("name") String name,
            @Query("specialization_id") String specialization_id,
            @Query("city_id") String city_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("near") String near

    );
    @GET("api/get-reservations")
    Call<ApointmentModel> getMyApointment(
            @Query("pagination_status") String pagination_status,
            @Query("per_link_") int per_link_,
            @Query("page") int page,
            @Query("user_id") int user_id,
            @Query("reservation_type") String reservation_type

    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<ResponseBody> updateToken(
            @Field("user_id") int user_id,
            @Field("phone_token") String phone_token,
            @Field("software_type") String software_type
    );
    @GET("api/show-doctor-profile")
    Call<SingleDataDoctorModel> getsingledoctor(
            @Query("doctor_id") String doctor_id,
            @Query("user_id") String user_id


            );
    @GET("api/get-doctor-reservations")
    Call<ReservisionTimeModel> getreservision(
            @Query("doctor_id") String doctor_id,
            @Query("date") String date,
            @Query("day_name") String day_name,
            @Query("reservation_type") String reservation_type




    );
    @FormUrlEncoded
    @POST("api/add-reservations")
    Call<ResponseBody> addreservision(@Field("user_id") String user_id,
                                      @Field("doctor_id") String doctor_id,
                                      @Field("date") String date,
                                      @Field("time")String time,
                                      @Field("cost")String cost,
                                      @Field("reservation_type")String reservation_type,
                                      @Field("day_name")String day_name,
                                      @Field("time_type")String time_type





    );
    @GET("api/get-diseases-by-specializations")
    Call<AllDiseasesModel> getdiseasbyspicial(
            @Query("specialization_id") int specialization_id

    );
    @GET("api/get-blogs")
    Call<AllAdviceModel> getadvice(
            @Query("specialization_id") String specialization_id,
            @Query("disease_id") String disease_id


    );
    @GET("api/get-available-doctors")
    Call<DoctorModel> getdoctors(
            );
    @FormUrlEncoded
    @POST("api/add-reservations")
    Call<ResponseBody> changereservision(@Field("user_id") String user_id,
                                      @Field("doctor_id") String doctor_id,
                                      @Field("date") String date,
                                      @Field("time")String time,
                                      @Field("cost")String cost,
                                      @Field("reservation_type")String reservation_type,
                                      @Field("day_name")String day_name,
                                      @Field("time_type")String time_type





    );
    @GET("api/get-my-notifications")
    Call<NotificationModel> getnotification(
            @Query("user_id") String user_id


    );
}
