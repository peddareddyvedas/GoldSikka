package com.goldsikka.goldsikka.interfaces;

import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactsModal;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.Models.MainModuleModel;
import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.Models.PredictionListModel;
import com.goldsikka.goldsikka.Models.ReferandEarnModel;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.OrganizationWalletModule.ORGModel;
import com.goldsikka.goldsikka.OrganizationWalletModule.UserOrganizationDetailsModel;
import com.goldsikka.goldsikka.model.CheckoutModel;
import com.goldsikka.goldsikka.model.Ecommerce_ModelClass.Ecommerce_DataClass;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.ReviewsModel;
import com.goldsikka.goldsikka.model.data;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDao {

//    @GET("api/price_cities/")
//    Call<List<price>> getprices(
//    @Query("city") String city);


    @GET("api/user/wallet/content")
    Call<List_Model> getDidigtalwallet_content(
            @Header("Authorization") String bannerToken);

    @GET("api/user/wallet/content")
    Call<data> getDidigtalwallet_subcontent(
            @Header("Authorization") String bannerToken);

    @GET("api/aboutUs")
    Call<Listmodel> getabouts(
            @Header("Authorization") String bannerToken);

    @GET("api/user/wallet/content")
    Call<data> getDidigtalwallet_featurecontent(
            @Header("Authorization") String bannerToken);

    @GET("api/faqs")
    Call<List<Listmodel>> getfaqs(
            @Header("Authorization") String faqid_token,
            @Query("key") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/marketing/banners")
    Call<List<Listmodel>> getBannerImages(
            @Header("Authorization") String bannerToken);

    @FormUrlEncoded
    @POST("api/auth/register")
    Call<Listmodel> getsignup(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("confirmPassword") String cpassword,
            @Field("roleType") String roletype,
            @Field("referral_code") String referral_code,
            @Field("via_registration") String via_registration,
            @Field("heard_us") String heard_us,
            @Field("heard_others_text") String heard_others_text,
            @Field("deviceType") String devicetype,
            @Field("appVersion") String app_version,
            @Field("androidVersion") String android_version,
            @Field("deviceUUID") String deviceUUID,
            @Field("deviceToken") String deviceToken,
            @Field("device_ip") String deviceip,
            @Field("device_id") String deviceid,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("zipcode") String zipcode,
            @Field("address") String address);

    @Multipart
    @POST("api/auth/register")
    Call<Listmodel> orggetsignup(
            @Part("roleType") RequestBody roletype,
            @Part("organization_type") RequestBody organization_type,
            @Part("org_name") RequestBody org_name,
            @Part("org_address") RequestBody org_address,
            @Part("org_city") RequestBody org_city,
            @Part("org_state") RequestBody org_state,
            @Part("org_zip_code") RequestBody org_zip_code,
            @Part("org_registration_number") RequestBody org_registration_number,
            @Part("org_description") RequestBody org_description,
            @Part List<MultipartBody.Part> org_photo,
            @Part List<MultipartBody.Part> org_registraion_photo,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody password,
            @Part("confirmPassword") RequestBody cpassword,
            @Part("via_registration") RequestBody via_registration,
            @Part("heard_us") RequestBody heard_us,
            @Part("heard_others_text") RequestBody heard_others_text,
            @Part("deviceType") RequestBody devicetype,
            @Part("appVersion") RequestBody app_version,
            @Part("androidVersion") RequestBody android_version,
            @Part("deviceUUID") RequestBody deviceUUID,
            @Part("deviceToken") RequestBody deviceToken,
            @Part("device_ip") RequestBody deviceip,
            @Part("device_id") RequestBody deviceid,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("zipcode") RequestBody zipcode,
            @Part("address") RequestBody address);


    @FormUrlEncoded
    @POST("api/auth/login")
    Call<Listmodel> getlogin(
            @Field("email") String email,
            /*@Field("password") String password,*/
            @Field("deviceType") String devicetype,
            @Field("appVersion") String app_version,
            @Field("androidVersion") String android_version,
            @Field("deviceUUID") String deviceUUID,
            @Field("deviceToken") String deviceToken,
            @Field("loginType") String loginType,
            @Field("device_ip") String deviceip,
            @Field("device_id") String deviceid,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("zipcode") String zipcode,
            @Field("address") String address);

    @POST("api/auth/reset-link")
    Call<Listmodel> get_forgot_email(
            @Query("email") String email);

    @FormUrlEncoded
    @POST("api/user/testimonials/add")
    Call<Listmodel> feedbackForm(
            @Header("Authorization") String token,
            @Field("message") String email,
            @Field("rating") String password);

    @POST("api/user/money-wallet/add")
    Call<Listmodel> addmoney_wallet(
            @Header("Authorization") String resendotpToken,
            @Query("amount") String amount,
            @Query("payment_id") String paymentid);

    @POST("api/auth/account/verify")
    Call<Listmodel> verify_account(
            @Query("mobile") String mobile);

    @POST("api/user/gspin/add")
    Call<Listmodel> savepin(
            @Header("Authorization") String resendotpToken,
            @Query("gs_pin") String gs_pin,
            @Query("confirm_gs_pin") String re_gs_pin);

    @POST("api/user/gspin/change")
    Call<Listmodel> updatepin(
            @Header("Authorization") String resendotpToken,
            @Query("current_gs_pin") String cu_gs_pin,
            @Query("new_gs_pin") String en_gs_pin,
            @Query("confirm_gs_pin") String re_gs_pin);

    @POST("api/auth/reset-gspin/{token}")
    Call<Listmodel> forgotpin(
            @Path("token") String token,
            @Query("gs_pin") String gs_pin,
            @Query("confirm_gs_pin") String re_gs_pin);

    @GET("api/user/gspin/check")
    Call<Listmodel> entrypin(
            @Header("Authorization") String resendotpToken,
            @Query("gsPin") String gs_pin);

    @GET("api/auth/login-check")
    Call<Listmodel> isPin(
            @Header("Authorization") String resendotpToken);

    @POST("api/auth/reset-password")
    Call<Listmodel> get_forgot_changepassword(
            @Query("email") String email,
            @Query("password") String password,
            @Query("confirmPassword") String confirmpassword);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/auth/verify/{code}")
    Call<Listmodel> getforgot_otp(
            @Path("code") String otp,
            @Query("email") String email);


    @GET("api/auth/register/{token}/otp/verify/{code}")
    Call<Listmodel> getotp(
            @Path("code") String otp,
            @Path("token") String otptoken);

    @GET("api/auth/gspin/{token}/otp/verify/{code}")
    Call<Listmodel> getotppin(
            @Path("code") String otp,
            @Path("token") String otptoken);


    @GET("api/otp/send")
    Call<Listmodel> resendotp(
            @Header("Authorization") String resendotpToken
    );

    @GET("api/otp/verify/{code}")
    Call<Listmodel> getlogin_otp(
            @Header("Authorization") String loginotptoken,
            @Path("code") String loginotp,
            @Query("device_uuid") String uuid);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/profile")
    Call<Listmodel> getprofile_details(
            @Header("Authorization") String profileToken);


    @GET("api/organizations/profile/request-list")
    Call<ORGModel> getEditplist(
            @Header("Authorization") String Token);

    @GET("api/organizations/profile/request-list")
    Call<ORGModel> getnextEditplist(
            @Header("Authorization") String Token,
            @Query("page") String pagenumber);

    @GET("api/user/kyc/is-kyc")
    Call<Listmodel> checkkyc(
            @Header("Authorization") String profileToken);

    @Multipart
    @POST("api/user/profile-image")
    Call<Listmodel> sendprofileimage(
            @Header("Authorization") String bannerToken,
            @Part List<MultipartBody.Part> weddingimag);

    @GET("api/user/schemes/{id}/calculations")
    Call<Listmodel> getcalculation(
            @Header("Authorization") String profileToken,
            @Path("id") String id,
            @Query("tenure") String tenure,
            @Query("grams") String grams);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })

    @GET("api/user/schemes/{id}/calculations")
    Call<Listmodel> getelevenplus_calculation(
            @Header("Authorization") String profileToken,
            @Path("id") String id,
            @Query("amount") String grams);

    //   @Query("mobile") String otpmobile
//    @Headers({
//            "Content-Type: application/json",
//            "Accept: application/json",
//            "User-Agent: Goldsikka"
//    })
//    @POST("api/auth/refresh")
//    Call<Listmodel> getrefresh(
//            @Header("Authorization") String refreshtoken);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}/content")
    Call<Listmodel> getcontent(@Header("Authorization") String token,
                               @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}/content")
    Call<Listmodel> gettermscontent(
            @Header("Authorization") String primaryAddress_token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}/content")
    Call<data> getfaqcontent(
            @Header("Authorization") String primaryAddress_token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}/content")
    Call<data> getsubcontent(
            @Header("Authorization") String primaryAddress_token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}/content")
    Call<data> getfeaturecontent(
            @Header("Authorization") String primaryAddress_token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/schemes")
    Call<List<Listmodel>> getschemes(
            @Header("Authorization") String schemes_token);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/address/{id}/makePrimary")
    Call<List<Listmodel>> get_primaryaddress(
            @Header("Authorization") String primaryAddress_token,
            @Path("id") String primary_address_id
    );

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/bankAccount/{id}/makePrimary")
    Call<List<Listmodel>> get_primarybankdetails(
            @Header("Authorization") String primarybank_token,
            @Path("id") String primary_bank_id
    );

    @FormUrlEncoded
    @POST("api/user/profile")
    Call<Listmodel> get_editprifile_details(
            @Header("Authorization") String edit_profile_token,
            @Field("name") String edit_name,
            @Field("email") String edit_email,
            @Field("mobile") String edit_mobile);

    @FormUrlEncoded
    @POST("api/organizations/profile/request")
    Call<Listmodel> org_editprifile_details(
            @Header("Authorization") String edit_profile_token,
            @Field("name") String edit_name,
            @Field("email") String edit_email,
            @Field("mobile") String edit_mobile);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/gold/current/price")
    Call<Listmodel> getlive_rates(
            @Header("Authorization") String liveratetoken);

    @GET("api/gold/current/22caratPrice")
    Call<Listmodel> getlive_rates22carats(
            @Header("Authorization") String liveratetoken);


    @GET("api/campaign/list")
    Call<PredictionListModel> PredictPageLoader(
            @Query("page") String id,
            @Header("Authorization") String passbook_token);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/wallet/gold/price/lock")
    Call<List<Listmodel>> getlock_rates(
            @Query("price") String price,
            @Header("Authorization") String liveratetoken);

    @GET("api/user/wallet/gold/price/lock")
    Call<List<Listmodel>> getlock_ratesforschems(
            @Query("price") String price,
            @Query("carat") String carat,
            @Header("Authorization") String liveratetoken);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/wallet/balance")
    Call<JsonElement> get_digitalwallet(@Header("Authorization") String DigitalwalletToken);


    @GET("api/auth/logout")
    Call<List<Listmodel>> get_logout(
            @Header("Authorization") String logouttoken);

    @GET("api/notifications")
    Call<Listmodel> notificationCount(
            @Header("Authorization") String logouttoken);


    @FormUrlEncoded
    @POST("api/user/wallet/gold/purchase")
    Call<Listmodel> get_purschasewithoutcoupon(
            @Header("Authorization") String purchasetoken,
            @Field("amount") String purchase_amount,
            @Field("payment_id") String paymentId,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("coupon") String coupon,
            @Field("referralCode") String referralCode);

    @FormUrlEncoded
    @POST("api/user/wallet/gold/purchase")
    Call<Listmodel> get_purschase(
            @Header("Authorization") String purchasetoken,
            @Field("amount") String purchase_amount,
            @Field("payment_id") String paymentId,
            @Field("coupon") String coupon,
            @Field("enteredWalletAmount") String enteredWalletAmount);

    @GET("api/user/transactions")
    Call<PassBookModel> getp_passbook(
            @Header("Authorization") String passbook_token);

    @GET("api/user/transactions")
    Call<PassBookModel> getp_filter(
            @Query("txn_type") String type,
            @Header("Authorization") String passbook_token);

    @GET("api/user/transactions")
    Call<PassBookModel> getp_passbook_page(
            @Query("page") String id,
            @Header("Authorization") String passbook_token);

    @GET("api/user/transactions")
    Call<PassBookModel> getFilterpagenation_passbook_page(
            @Query("page") String id,
            @Query("txn_type") String type,
            @Header("Authorization") String passbook_token);


    @FormUrlEncoded
    @POST("api/user/address")
    Call<Listmodel> getcustomeraddress(
            @Header("Authorization") String cusutomeraddress_token,
            @Field("title") String address_type,
            @Field("address") String customer_address,
            @Field("city") String customercity,
            @Field("state_id") String state,
            @Field("zip") String pincode);

    @GET("api/user/address")
    Call<data> get_addresslist(
            @Header("Authorization") String addresslist_token);

    @GET("api/user/address/{id}")
    Call<Listmodel> get_address(
            @Header("Authorization") String address_token,
            @Path("id") String address_id);

    @GET("api/states")
    Call<List<Listmodel>> get_states(
            @Header("Authorization") String states_token);

    @GET("api/user/bankAccount/bank/list")
    Call<List<Listmodel>> get_bank(
            @Header("Authorization") String states_token);


    @GET("api/countries")
    Call<List<Listmodel>> getcountries(
            @Header("Authorization") String states_token);

    @GET("api/user/address/primary")
    Call<Listmodel> get_profileaddress(
            @Header("Authorization") String profileaddress_token);

    @GET("api/user/bankAccount/primary")
    Call<Listmodel> get_profile_primarybankdetais(
            @Header("Authorization") String profilebank_token);

    @DELETE("api/user/address/{id}")
    Call<Listmodel> get_address_remove(
            @Header("Authorization") String address_token,
            @Path("id") String address_id);

    @FormUrlEncoded
    @POST("api/user/address/{id}")
    Call<Listmodel> get_address_edit(
            @Header("Authorization") String address_token,
            @Path("id") String address_id,
            @Field("title") String address_type,
            @Field("address") String customer_address,
            @Field("city") String customercity,
            @Field("state_id") String state,
            @Field("zip") String pincode);

    @FormUrlEncoded
    @POST("api/user/bankAccount")
    Call<Listmodel> get_bankdetails(
            @Header("Authorization") String address_token,
            @Field("account_number") String account_number,
            @Field("name_on_account") String account_name,
            @Field("bank_name") String bankname,
            @Field("bank_branch") String branch_name,
            @Field("ifsc_code") String ifsc_code);


    @GET("api/user/bankAccount/{id}")
    Call<Listmodel> get_profile_bankdetails(
            @Header("Authorization") String profile_token,
            @Path("id") String bank_id);


    @DELETE("api/user/bankAccount/{id}")
    Call<Listmodel> get_bankdetails_remove(
            @Header("Authorization") String remove_token,
            @Path("id") String bank_id);

    @GET("api/user/bankAccount")
    Call<data> get_banklist(
            @Header("Authorization") String banklist_token);


    @FormUrlEncoded
    @POST("api/user/bankAccount/{id}")
    Call<Listmodel> get_editbankdetails(
            @Header("Authorization") String address_token,
            @Path("id") String bank_id,
            @Field("account_number") String account_number,
            @Field("name_on_account") String account_name,
            @Field("bank_name") String bankname,
            @Field("bank_branch") String branch_name,
            @Field("ifsc_code") String ifsc_code);


    @FormUrlEncoded
    @POST("api/user/wallet/gold/withdraw")
    Call<Listmodel> get_redeem(
            @Header("Authorization") String redeem_token,
            @Field("grams") String redeem_grams,
            @Field("amount") String redeem_ammount);

    @FormUrlEncoded
    @POST("api/user/wallet/gold/sell")
    Call<Listmodel> get_sell(
            @Header("Authorization") String sell_token,
            @Field("grams") String sell_grams,
            @Field("amount") String sell_ammount,
            @Field("transferTo") String transferTo,
            @Field("bankId") String bankId);


    @FormUrlEncoded
    @POST("api/user/wallet/sell-validation")
    Call<Listmodel> get_validationsell(
            @Header("Authorization") String sell_token,
            @Field("grams") String sell_grams,
            @Field("transferTo") String transferTo);

    @FormUrlEncoded
    @POST("api/user/wallet/withdraw-validation")
    Call<Listmodel> get_validationreedem(
            @Header("Authorization") String sell_token,
            @Field("grams") String sell_grams);

    @FormUrlEncoded
    @POST("/api/user/wallet/redeem-gold-with-money-wallet")
    Call<Listmodel> get_redeem_gold_from_money_wallet(
            @Header("Authorization") String sell_token,
            @Field("amount") String amount,

            @Field("grams") String grams,
            @Field("enteredWalletAmount") String enteredWalletAmount);


    @FormUrlEncoded
    @POST("api/user/wallet/gold/transfer")
    Call<Listmodel> get_gift(
            @Header("Authorization") String gift_token,
            @Field("to") String gift_mobile,
            @Field("amount") String gift_ammount,
            @Field("quantity") String gift_quantity);

    @FormUrlEncoded
    @POST("api/user/wallet/gold/gift")
    Call<Listmodel> get_gifttranfer(
            @Header("Authorization") String gift_token,
            @Field("to") String gift_mobile,
            @Field("amount") String gift_ammount,
            @Field("quantity") String gift_quantity);


    @FormUrlEncoded
    @POST("api/user/changePassword")
    Call<Listmodel> get_changepassword(
            @Header("Authorization") String changepassword_token,
            @Field("currentPassword") String current_password,
            @Field("newPassword") String new_password,
            @Field("confirmNewPassword") String confirm_newpassword);

    @FormUrlEncoded
    @POST("api/user/kyc")
    Call<Listmodel> get_kyc(
            @Header("Authorization") String kyc_token,
            @Field("gender") String kyc_gender,
            @Field("father_name") String kyc_fathername,
            @Field("spouse_name") String kyc_spousename,
            @Field("aadhaar_card") String kyc_aadhaar,
            @Field("pan_card") String kyc_pannumber,
            @Field("alternate_phone") String kyc_alternate_phone);


    @GET("api/user/kyc")
    Call<Listmodel> get_kyc_details(
            @Header("Authorization") String kyc_token);


    @FormUrlEncoded
    @POST("api/user/nominee")
    Call<Listmodel> get_nominee(
            @Header("Authorization") String nominee_token,
            @Field("name") String nominee_name,
            @Field("phone") String nominee_phone,
            @Field("address") String nominee_address,
            @Field("country_id") String nominee_country,
            @Field("state_id") String nominee_state,
            @Field("city") String nominee_city,
            @Field("zip") String nominee_zip,
            @Field("relation") String nominee_relation);


    @GET("api/user/nominee")
    Call<data> get_nominee_details(
            @Header("Authorization") String nominee_token
    );

    @DELETE("api/user/nominee/{id}")
    Call<Listmodel> delete_nominee_details(
            @Header("Authorization") String nominee_token,
            @Path("id") String nominee_id);

    @FormUrlEncoded
    @POST("api/user/schemes/{id}/subscribe")
    Call<Listmodel> getsubscribedetails(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("payment_id") String paymentid,
            @Field("total_installments") String total_installments,
            @Field("emi_grams") String emi_grams,
            @Field("amount") String amount,
            @Field("liveAmount") String liveAmount,
            @Field("walletAmount") String walletAmount,
            @Field("referralCode") String referralCode,
            @Field("coupon") String coupon);

    @FormUrlEncoded
    @POST("api/user/schemes/{id}/scheme-with-money-wallet")
    Call<Listmodel> buywalletAmount_scheme(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("total_installments") String total_installments,
            @Field("emi_grams") String emi_grams,
            @Field("amount") String amount,
            @Field("liveAmount") String liveAmount,
            @Field("walletAmount") String walletAmount,
            @Field("referralCode") String referralCode,
            @Field("coupon") String coupon);


    @FormUrlEncoded
    @POST("api/user/schemes/{id}/subscribe")
    Call<Listmodel> getelevensubscribedetails(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("payment_id") String paymentid,
            @Field("total_installments") String total_installments,
            @Field("amount") String emi_grams,
            @Field("liveAmount") String liveAmount,
            @Field("walletAmount") String walletAmount,
            @Field("referralCode") String referralCode,
            @Field("coupon") String coupon);

    @FormUrlEncoded
    @POST("api/user/schemes/{id}/scheme-with-money-wallet")
    Call<Listmodel> buywalletAmount_elevenscheme(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("total_installments") String total_installments,
            @Field("amount") String amount,
            @Field("liveAmount") String liveAmount,
            @Field("walletAmount") String walletAmount,
            @Field("coupon") String coupon);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/{id}")
    Call<SchemeModel> get_usersubscribed(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id);


    @POST("api/user/schemes/{id}/scheme-nickname")
    Call<SchemeModel> addnickname(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id,
            @Query("nick_name") String nick_name);

    @POST("api/user/schemes/{id}/remove-nickname")
    Call<SchemeModel> removenickname(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id);


    @GET("api/tickets/scheme/{id}")
    Call<Enquiryformmodel> getticketlist(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id);

    @GET("api/user/schemes/{id}")
    Call<SchemeModel> get_usersubscribedpage(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id,
            @Query("page") String page);

    @GET("api/tickets/scheme/{id}")
    Call<Enquiryformmodel> getticketnextpage(
            @Header("Authorization") String userscheme_token,
            @Path("id") String id,
            @Query("page") String page);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/info/{id}")
    Call<SchemeModel> getsubscribeplanedetails(
            @Header("Authorization") String token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/user/schemes/info/{id}")
    Call<SchemeModel> getsubscribeplane_details(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/user/schemes/{id}/payment-details")
    Call<SchemeModel> nextpaymentmmi(
            @Header("Authorization") String token,
            @Path("id") String id);


    @FormUrlEncoded
    @POST("api/user/schemes/mmi-payment/{id}")
    Call<SchemeModel> nextpaymentmmisucccess(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("live_price") String live_price,
            @Field("payment_id") String payment_id,
            @Field("walletAmount") String enteredWalletAmount,
            @Field("referralCode") String referralCode);

    @FormUrlEncoded
    @POST("api/user/schemes/mmi-payment/{id}/money-wallet")
    Call<SchemeModel> buywalletAmount_mmipayment(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("live_price") String live_price,
            @Field("walletAmount") String enteredWalletAmount);


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/splashScreen")
    Call<List<Listmodel>> getonboardimages();

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    /////////////////// Ecomm Apis/////////////
    @GET("api/ecom/categories")
    Call<List<Listmodel>> get_ecomcotegorylist();

    @GET("api/ecom/banners")
    Call<List<Listmodel>> getEcomBanners();

    @GET("api/ecom/newarrivals")
    Call<List<Listmodel>> get_ecomnewarrivalslist();

    /* @GET("api/ecom/newarrivals")
     Call<Object> get_ecomnewarrivalslist();
 */
    @GET("api/ecom/topsellingproducts")
    Call<List<Listmodel>> get_ecomTopSellinglist();


    /* @GET("api/ecom/topsellingproducts")
     Call<Object> get_ecomTopSellinglist();
 */

    @GET("api/ecom/priceunderboxes")
    Call<List<Listmodel>> getEcompriceunderbox(
            @Header("Authorization") String token);

    @GET("api/ecom/priceunderboxes/{id}")
    Call<List<Listmodel>> getEcompriceunderboxprice(
            @Header("Authorization") String token, @Path("id") String id);

    @GET("api/ecom/{cid}/subcategories")
    Call<List<Listmodel>> get_ecomsubcategory(
            @Path("cid") String cid);

    @GET("api/ecom/subcategories/{id}/products")
    Call<List<Listmodel>> get_ecomsubcategoryproducts(
            @Path("id") String id);

    @POST("api/ecom/favourites")
    Call<Listmodel> postfavourites(
            @Header("Authorization") String token,
            @Query("pid") String pid,
            @Query("size") String size);

    @DELETE("api/ecom/favourites/{id}")
    Call<Listmodel> deletefavourite(
            @Header("Authorization") String token,
            @Path("id") String id);


    @GET("api/ecom/favourites")
    Call<List<Listmodel>> getEcomFavvouritelist(
            @Header("Authorization") String token
    );

    @GET("api/ecom/silverprice")
    Call<ResponseBody> getEcomsilverprice(
            @Header("Authorization") String token
    );


    @GET("api/ecom/products/{id}")
    Call<List<Listmodel>> getproductid(
            @Path("id") String id);

    @GET("api/ecom/brands")
    Call<List<Listmodel>> getEcomBrands();

    @GET("api/ecom/brands/{id}")
    Call<List<Listmodel>> postEcomBrands(@Header("Authorization") String token,
                                         @Path("id") String id);

    @POST("api/ecom/cart")
    Call<Listmodel> postEcomcartitem(
            @Header("Authorization") String token,
            @Query("pid") String pid,
            @Query("size") String size);

    @GET("api/ecom/cart")
    Call<List<Listmodel>> getEcomcartitem(@Header("Authorization") String token);

    @DELETE("api/ecom/cart/{id}")
    Call<Listmodel> deletecommcartproduct(@Header("Authorization") String token,
                                          @Path("id") String id);


    @POST("api/ecom/movetofavourites/{id}/{size}")
    Call<Listmodel> postEcomcartitemtofavourite(
            @Header("Authorization") String token,
            @Path("id") String quantity,
            @Path("size") String size);


    @POST("api/ecom/movetocart/{id}/{size}")
    Call<Listmodel> postEcomfavouritiestocart(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Path("size") String size);


    @GET("api/ecom/checkout")
    Call<Object> getEcommcheckout(@Header("Authorization") String token);

    @GET("api/ecom/checkout")
    Call<Object> getEcommcheckoutwitcardis(@Header("Authorization") String token, @Query("card_id") String quantity);


    @FormUrlEncoded
    @POST("api/ecom/checkout")
    Call<CheckoutModel> postcheckout(
            @Header("Authorization") String purchasetoken,
            @Field("pid") String pid,
            @Field("live_price") String liveprice,
            @Field("no_of_products") String no_of_products,
            @Field("payment_id") String payment_id,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("silver_liveprice") String silver_liveprice,
            @Field("size") String size,
            @Field("live_price24") String live_price24,
            @Field("stoneid") String stoneid);

    //
    @FormUrlEncoded
    @POST("api/ecom/checkout/moneywallet")
    Call<CheckoutModel> postcheckoutmoneywallet(
            @Header("Authorization") String purchasetoken,
            @Field("pid") String pid,
            @Field("live_price") String liveprice,
            @Field("no_of_products") String no_of_products,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("silver_liveprice") String silver_liveprice,
            @Field("size") String size,
            @Field("live_price24") String live_price24,
            @Field("stoneid") String stoneid);


    @FormUrlEncoded
    @POST("api/ecom/ratings")
    Call<Listmodel> postratingform(
            @Header("Authorization") String token,
            @Field("review") String email,
            @Query("rating") String password,
            @Query("pid") String pid);

    @GET("api/ecom/ratings/{pid}")
    Call<List<ReviewsModel>> getratings(
            @Header("Authorization") String token,
            @Path("pid") String pid);

    @GET("api/ecom/myorders")
    Call<List<Listmodel>> getecommyorders(
            @Header("Authorization") String token);


    @GET("api/ecom/ordertracking/{order_id}")
    Call<List<Listmodel>> getecommorderstracking(
            @Header("Authorization") String token, @Path("order_id") String pid);


    @GET("api/ecom/orderdetails/{order_id}")
    Call<List<Listmodel>> getecommproductinfo(
            @Header("Authorization") String token, @Path("order_id") String pid);


    @GET("api/ecom/invoice/{order_id}")
    Call<ResponseBody> getpdfdownload(
            @Header("Authorization") String token,
            @Path("order_id") String pid);

    ///////////////////////////


    @GET("api/ecom/categories")
    Call<data> get_cotegorylist();

    @GET("api/Jewellery/categories")
    Call<List<Listmodel>> getJewCats();

    @GET("api/Jewellery/products")
    Call<List<Listmodel>> getJewProducts();

    @GET("api/Jewellery/productbycats/{id}")
    Call<List<Listmodel>> getProductsByCat(
            @Path("id") String id
    );

    @GET("api/Jewellery/product/{id}")
    Call<List<Listmodel>> getProductById(
            @Path("id") String id
    );

    @GET("api/Jewellery/passbook/{pid}")
    Call<List<Listmodel>> getJewelleryPassbook(
            @Header("Authorization") String token,
            @Path("pid") String id
    );

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @POST("api/Jewellery/wishlist")
    Call<Listmodel> postwishlist(
            @Header("Authorization") String token,
            @Query("pids") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @PUT("api/Jewellery/wishlist/{id}")
    Call<Integer> postredeemwish(
            @Header("Authorization") String token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/Jewellery/banners")
    Call<List<Listmodel>> getJewBanners(
            @Header("Authorization") String token);


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @POST("api/Jewellery/passbook")
    Call<JsonObject> buyJewelleryWithGold(
            @Header("Authorization") String token,
            @Query("pid") String id,
            @Query("goldtransfer") String goldtransfer);


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @DELETE("api/Jewellery/wishlist/{id}")
    Call<Listmodel> deletewishlist(
            @Header("Authorization") String token,
            @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/Jewellery/wishlist")
    Call<List<Listmodel>> getWishlist(
            @Header("Authorization") String token
    );


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/ecom/categories/{id}/sub-categories")
    Call<Listmodel> get_subcotegorylist(
            @Path("id") String categoryid
    );


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/ecom/categories/{id}/products")
    Call<data> getproductlist(
            @Path("id") String subcategoryid);

    @POST("api/ecom/cart/{id}/add")
    Call<Listmodel> addproduct(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @POST("api/ecom/cart/{id}/remove")
    Call<Listmodel> removeproduct(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @GET("api/ecom/product-info/{id}")
    Call<Listmodel> get_prod_info(
            @Path("id") String id);

    @GET("api/ecom/carts")
    Call<data> cart_list(@Header("Authorization") String token);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "User-Agent: Goldsikka"
    })
    @GET("api/ecom/carts")
    Call<List_Model> cart_listfortotalamount(
            @Header("Authorization") String token);

    @GET("api/ecom/save-list")
    Call<data> cart_savedlist(
            @Header("Authorization") String token);

    @DELETE("api/ecom/cart/{id}/delete")
    Call<Listmodel> cart_item_deete(
            @Header("Authorization") String token,
            @Path("id") String id);

    @POST("api/ecom/save-product/{id}")
    Call<Listmodel> cart_saveditem(
            @Header("Authorization") String token,
            @Path("id") String id);

    @DELETE("api/ecom/save/{id}/delete")
    Call<Listmodel> cart_saveditem_delete(
            @Header("Authorization") String token,
            @Path("id") String id);


    @POST("api/ecom/move-to-cart/{id}")
    Call<Listmodel> movetocart(
            @Header("Authorization") String token,
            @Path("id") String id);

    @POST("api/ecom/place-order")
    Call<Listmodel> cart_payment(
            @Header("Authorization") String token,
            @Query("address_id") String Address_id,
            @Query("payment_id") String paymentid);

    @GET("api/ecom/orders-list")
    Call<Ecommerce_DataClass> order_list(
            @Header("Authorization") String token);

// **** prediction module methodes *****//

    @GET("api/user/wallet/modules")
    Call<MainModuleModel> predict(
            @Header("Authorization") String token);

    @GET("api/campaign/prediction-access")
    Call<List_Model> predictAccess(
            @Header("Authorization") String token);


    @GET("api/campaign/list")
    Call<PredictionListModel> prediction_list(
            @Header("Authorization") String token);

    @GET("api/poplist")
    Call<EventModel> advertaisepopup(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/campaign/add/predict-price")
    Call<List_Model> predict_pricesubmit(
            @Header("Authorization") String token,
            @Field("price_predicted") String price);

    @GET("api/campaign/tc")
    Call<Listmodel> getpredict_termscontent(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/campaign/update/predict-price/{id}")
    Call<Listmodel> predict_updatedpricesubmit(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Field("price_predicted") String price);


    @GET("api/campaign/timings")
    Call<Listmodel> getpredict_timeings(
            @Header("Authorization") String token);

    @POST("api/campaign/screen-update/{id}")
    Call<Listmodel> init_seescreen(
            @Header("Authorization") String token,
            @Path("id") String id);


    @GET("api/campaign/screens")
    Call<List_Model> getpopupscreens(
            @Header("Authorization") String bannerToken);


    @GET("api/enquiry-types")
    Call<List<Enquiryformmodel>> loadplansforenquiry(
            @Query("type") String type);

    @FormUrlEncoded
    @POST("api/purchase-enquiry")
    Call<Enquiryformmodel> purchaseenquiryform(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("purchase_type") String purchase_type,
            @Field("grams") String grams,
            @Field("address") String address,
            @Field("description") String description);

    @FormUrlEncoded
    @POST("api/business-enquiry")
    Call<Enquiryformmodel> businessenquiryform(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("business_type") String business_type,
            @Field("designation") String designation,
            @Field("address") String address,
            @Field("description") String description
            //@Field("company_name") String company_name
    );

    @FormUrlEncoded
    @POST("api/contact")
    Call<Enquiryformmodel> contactusenquiryform(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("city") String address,
            @Field("description") String description,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/contact")
    Call<Enquiryformmodel> womensplapi(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("description") String description,
            @Field("type") String type
    );

    // **** Referral Api's****//


    @GET("api/user/referral/earnings")
    Call<ReferandEarnModel> loadreferraldeatils(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/user/money-wallet/withdraw")
    Call<ReferandEarnModel> loadwithdraw(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("bank_id") String bank_id);

    // Transaction pdf Downlaod
    @GET("api/user/one-transaction-pdf/{id}")
    Call<ResponseBody> transactionpdfdownload(
            @Header("Authorization") String token,
            @Path("id") String transactionid);

    @GET("api/user/schemes/{id}/scheme-pdf")
    Call<ResponseBody> Schhemetransactionpdfdownload(
            @Header("Authorization") String token,
            @Path("id") String transactionid);


    //** Events Api's *//

    //@FormUrlEncoded
    @Multipart
    @POST("api/user/events/create")
    Call<EventModel> createevent(
            @Header("Authorization") String maindetails_Token,
            @Part("event_type") RequestBody event_type,
            @Part("event_name") RequestBody event_name,
            @Part("event_date") RequestBody event_date,
            @Part("muhurtham_time") RequestBody muhurtham_time,
            @Part("description") RequestBody description,
            @Part("other_event_type") RequestBody other_event_type,
            @Part("venue") RequestBody venue,
            @Part("gender") RequestBody gender,
            @Part("bride_name") RequestBody bride_name,
            @Part("groom_name") RequestBody groom_name,
            @Part("holder_name") RequestBody holder_name,
            @Part List<MultipartBody.Part> coupleothereventimage,
            @Part List<MultipartBody.Part> weddingimage);

    @GET("api/user/events/list")
    Call<EventModel> eventlist(
            @Header("Authorization") String token);


    @GET("api/user/money-wallet/transactions")
    Call<EventModel> wallet_transction_list(
            @Header("Authorization") String token);

    @GET("api/user/testimonials/happiest/customers")
    Call<EventModel> happy_customers_list(
            @Header("Authorization") String token);


    @GET("api/user/testimonials/happiest/customers")
    Call<EventModel> happyCustomerlistnextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/user/money-wallet/transactions")
    Call<EventModel> walletlistnextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/notifications/list")
    Call<EventModel> NotificationList_nextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/notifications/list")
    Call<EventModel> NotificationList(
            @Header("Authorization") String token);

    @GET("api/user/referral/referralList")
    Call<EventModel> EarnedList(
            @Header("Authorization") String token);

    @GET("api/user/referral/referralList")
    Call<EventModel> EarnedList_nextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/notifications/read-one/{id}")
    Call<EventModel> NotificationSeen(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/notifications/read-all")
    Call<EventModel> AllNotificationsSeen(
            @Header("Authorization") String token);

    @GET("api/user/money-wallet/amount")
    Call<Listmodel> walletAmount(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/user/wallet/buy-gold-with-money-wallet")
    Call<Listmodel> buywalletAmount(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("coupon") String coupon);

    @FormUrlEncoded
    @POST("/api/user/wallet/gold/withdraw")
    Call<Listmodel> getrazorpayforredeem(
            @Header("Authorization") String token,
            @Field("payment_id") String payment_id,
            @Field("amount") String amount,
            @Field("grams") String grams,
            @Field("purity") String purity,
            @Field("fraction") String fraction);

    @GET("api/user/referral/referral-validation")
    Call<Listmodel> referCodeValidation(
            @Header("Authorization") String token,
            @Query("referralCode") String referralCode);

    @FormUrlEncoded
    @POST("/api/user/events/gift/money-wallet")
    Call<Listmodel> buywalletAmount_nocoupon(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("eventId") String coupon);

    @FormUrlEncoded
    @POST("api/user/events/gift/money-wallet")
    Call<Listmodel> buywalletAmount_events(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("eventId") String coupon);

    @FormUrlEncoded
    @POST("api/organizations/donate-gold-money-wallet")
    Call<Listmodel> buywalletAmount_org(
            @Header("Authorization") String token,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String enteredWalletAmount,
            @Field("organizationId") String coupon);

    @GET("api/user/events/list")
    Call<EventModel> eventlistnextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/user/events/invitations")
    Call<EventModel> recivedeventlist(
            @Header("Authorization") String token);

    @GET("api/user/events/invitations")
    Call<EventModel> recivedeventlistnextpage(
            @Header("Authorization") String token,
            @Query("page") String pagenumber);

    @GET("api/user/events/invited-list/{id}")
    Call<EventModel> sentlist(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/user/events/invited-list/{id}")
    Call<EventModel> sentlistnextpage(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Query("page") String pagenumber);


    @GET("api/user/events/validation/{id}")
    Call<EventModel> eventverify(
            @Header("Authorization") String token,
            @Path("id") String eventId);

    @GET("api/user/events/qr-code/{id}")
    Call<ResponseBody> GetqrCode(
            @Header("Authorization") String Token,
            @Path("id") String eventid);

    @FormUrlEncoded
    @POST("api/user/events/send-invitation")
    Call<ContactsModal> sendcontacts(
            @Header("Authorization") String Token,
            @Field("event_id") String eventid,
            @Field("guest_mobile") String guest_mobile);

    @DELETE("api/user/events/delete/{id}")
    Call<EventModel> DeleteEvent(
            @Header("Authorization") String Token,
            @Path("id") String eventid);


    @Multipart
    @POST("api/user/events/update/{id}")
    Call<EventModel> UpdateEvent(
            @Header("Authorization") String maindetails_Token,
            @Path("id") String eventid,
            @Part("event_type") RequestBody event_type,
            @Part("event_name") RequestBody event_name,
            @Part("event_date") RequestBody event_date,
            @Part("muhurtham_time") RequestBody muhurtham_time,
            @Part("description") RequestBody description,
            @Part("other_event_type") RequestBody other_event_type,
            @Part("venue") RequestBody venue,
            @Part("gender") RequestBody gender,
            @Part("bride_name") RequestBody bride_name,
            @Part("groom_name") RequestBody groom_name,
            @Part("holder_name") RequestBody holder_name,
            @Part List<MultipartBody.Part> coupleothereventimage,
            @Part List<MultipartBody.Part> weddingimage);

    @GET("api/user/events/get/{id}")
    Call<EventModel> geteventdetails(
            @Header("Authorization") String Token,
            @Path("id") String eventid);

    @FormUrlEncoded
    @POST("api/user/events/gift-gold")
    Call<EventModel> eventgift(
            @Header("Authorization") String token,
            @Field("eventId") String eventId,
            @Field("payment_id") String payment_id,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String walletamount);


    @FormUrlEncoded
    @POST("api/organizations/donate-gold")
    Call<EventModel> Donategold(
            @Header("Authorization") String token,
            @Field("organizationId") String organizationId,
            @Field("payment_id") String payment_id,
            @Field("amount") String amount,
            @Field("enteredWalletAmount") String etwallet);

    // ** Coupons Api's

    @GET("api/coupon/online-coupons")
    Call<CouponsModel> getcouponlist(
            @Header("Authorization") String Token);

    @FormUrlEncoded
    @POST("api/coupon/offline-validation")
    Call<CouponsModel> offlinecoupon(
            @Header("Authorization") String Token,
            @Field("coupon") String coupon);

    @FormUrlEncoded
    @POST("api/coupon/online-validation")
    Call<CouponsModel> couponvalidation(
            @Header("Authorization") String Token,
            @Field("coupon") String coupon,
            @Field("amount") String amount);


    @GET("api/coupon/online-coupons")
    Call<CouponsModel> getnextcouponlist(
            @Header("Authorization") String Token,
            @Query("page") String pagenumber);

    @FormUrlEncoded
    @POST("api/coupon/code")
    Call<CouponsModel> ApplyCouponcode(
            @Header("Authorization") String Token,
            @Field("coupon") String couponcode,
            @Field("is_purchased") String frommain);

    @FormUrlEncoded
    @POST("api/coupon/code")
    Call<CouponsModel> ApplyCouponcodeforBuygold(
            @Header("Authorization") String Token,
            @Field("coupon") String couponcode);

    // *** SchemeTitle**
    @FormUrlEncoded
    @POST("api/tickets/add")
    Call<Enquiryformmodel> RiseTicket(
            @Header("Authorization") String Token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("message") String description,
            @Field("scheme_id") String scheme_id);


    // Orgnazation Module

    @GET("api/organizations/view")
    Call<UserOrganizationDetailsModel> get_orgdetails(
            @Header("Authorization") String bannerToken);


    @GET("api/organizations/list")
    Call<UserOrganizationDetailsModel> org_list(
            @Header("Authorization") String token,
            @Query("type") String type);

    @GET("api/organizations/list")
    Call<UserOrganizationDetailsModel> org_nextlist(
            @Header("Authorization") String token,
            @Query("page") String pagenumber,
            @Query("type") String type);

    @GET("api/organizations/info/{id}")
    Call<UserOrganizationDetailsModel> get_orginfo(
            @Header("Authorization") String token,
            @Path("id") String org_id);

    @FormUrlEncoded
    @POST("api/auth/social-login")
    Call<Listmodel> get_google_login(
            @Field("email") String email,
            @Field("name") String password,
            @Field("provider") String provider,
            @Field("device_ip") String deviceip,
            @Field("device_id") String deviceid,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("zipcode") String zipcode,
            @Field("address") String address);


    @GET("api/directories/category")
    Call<List<Listmodel>> get_catogorieslist();

    @GET("api/directories/banners")
    Call<List<Listmodel>> get_bannerslist();


    @GET("api/directories/store")
    Call<List<Listmodel>> gettagslisting();


    @GET("api/directories/top/list")
    Call<List<Listmodel>> get_toplisting();

    @GET("api/directories/store/category/{id}")
    Call<List<Listmodel>> getcatogorieslistid(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/directories/tags/{id}")
    Call<List<Listmodel>> gettagslistid(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/directories/storetimings")
    Call<List<Listmodel>> getstoretimings();

    @GET("api/directories/storereview")
    Call<List<Listmodel>> get_ratings();

    @GET("api/directories/mediabycatid/{id}")
    Call<List<Listmodel>> get_directoriesstoreimages(@Header("Authorization") String token, @Path("id") String id);

    @GET("api/ecom/giftcard")
    Call<List<Listmodel>> getEcomgiftcart(@Header("Authorization") String token);

    @POST("api/ecom/giftcard")
    Call<Listmodel> getecomupdategiftcart(@Header("Authorization") String token, @Query("card") String couponcode);

    @POST("api/ecom/applygiftcard")
    Call<Listmodel> postgiftcard(
            @Header("Authorization") String token, @Query("card") String pid);

    @GET("api/ecom/showgiftcards")
    Call<List<Listmodel>> getEcomTotalGiftCards();


    @GET("api/ecom/giftcard/{id}")
    Call<Listmodel> getEcomTotalGiftCardsAmountdecut(@Header("Authorization") String token, @Path("id") String couponcode);


    @FormUrlEncoded
    @POST("api/directories/storereview")
    Call<Listmodel> poststorereview(
            @Header("Authorization") String token,
            @Field("sid") String sid,
            @Field("rating") String rating,
            @Field("review") String review);

    @FormUrlEncoded
    @POST("api/directories/reporting")
    Call<Listmodel> postreportissue(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("address") String address,
            @Field("mobileno") String mobileno,
            @Field("message") String message);

    @FormUrlEncoded
    @POST("api/directories/store/owner")
    Call<Listmodel> poststoreowner(
            @Header("Authorization") String token,
            @Field("pan_card") String name,
            @Field("aadhar_card") String emailid,
            @Field("sid") String message);


    @GET("api/directories/store/{id}")
    Call<Listmodel> getstoredataa(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/directories/storereview/{id}")
    Call<List<Listmodel>> getretaingsdata(
            @Header("Authorization") String token,
            @Path("id") String id);

    @GET("api/directories/storeowner")
    Call<List<Listmodel>> getownerdetails(
            @Header("Authorization") String bannerToken);


    @FormUrlEncoded
    @POST("api/directories/enquiry")
    Call<Listmodel> postenquiry(
            @Header("Authorization") String token,
            @Field("email") String email,
            @Field("subject") String subject,
            @Field("name") String name,
            @Field("body") String body);

    @FormUrlEncoded
    @POST("api/directories/businessform")
    Call<Listmodel> postbusinessform(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("emailid") String emailid,
            @Field("mobileno") String mobileno,
            @Field("message") String message);


}
