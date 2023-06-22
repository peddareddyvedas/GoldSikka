package com.goldsikka.goldsikka.Fragments.NewDesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.LOGIN.ChangePassword;
import com.goldsikka.goldsikka.LOGIN.CreatePIN;
import com.goldsikka.goldsikka.Activitys.FeedbackForm;
import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.LOGIN.MPIN;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Directory.GiftCardActivity;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.NewDesignsActivity.EnquiryandSupport;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.LOGIN.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends Fragment implements View.OnClickListener {

    TextView tvchangepass, tvlogout, tvterms, tvsupport, tvrefer, TVPIN, tvfeedback, tvgiftcards;
    TextView tvmoneywallet, tvtest2, tvtest3;
    //    ImageView ivprofile;
    ApiDao apiDao;
    shared_preference sharedPreference;
    private Activity activity;

    String stavathar;
    String stprofileimg;

    TextView uidTv, unameTv;

    SwitchCompat lockswitch;

    TextView usetname;
    TextView useremail;
    TextView userid;


    TextView tveditprofile, tvnominee, tvkyc, tvbank, tvaddress, tvemail, tvname, tvcustomerid;
    CircleImageView ivprofileb;
    String st_name, st_mobile, st_email;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        sharedPreference = new shared_preference(activity);

        intilizeview(view);

        CustomerDetails();


        uidTv = view.findViewById(R.id.uid);
        unameTv = view.findViewById(R.id.uname);
        usetname = view.findViewById(R.id.usetname);
        useremail = view.findViewById(R.id.useremail);
        userid = view.findViewById(R.id.userid);
        tveditprofile = view.findViewById(R.id.tveditprofile);
        uidTv.setText(AccountUtils.getCustomerID(activity));
        unameTv.setText(AccountUtils.getName(activity));
        useremail.setText(AccountUtils.getEmail(activity));
        tvkyc = view.findViewById(R.id.tvkyc);
        tvbank = view.findViewById(R.id.tvbankdetails);
        tvnominee = view.findViewById(R.id.tvnominee);
        tvaddress = view.findViewById(R.id.tvaddress);
        ivprofileb = view.findViewById(R.id.profile);

        usetname.setText(AccountUtils.getName(activity));
        userid.setText(AccountUtils.getCustomerID(activity));
        lockswitch = view.findViewById(R.id.applockswitch);
        tvgiftcards = view.findViewById(R.id.tvgiftcards);

        Boolean checkpin1 = AccountUtils.getIsPin(activity);
        Boolean checkpin = sharedPreference.readpinstatus();
        if (checkpin) {
            lockswitch.setChecked(true);
        } else {
            lockswitch.setChecked(false);
        }

        lockswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!checkpin1) {
                        Intent i = new Intent(getContext(), CreatePIN.class);
                        startActivity(i);
                    } else
                        sharedPreference.writepinstatus(true);
                } else {
                    sharedPreference.writepinstatus(false);
                }
            }
        });

        tveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isConnected(getActivity())) {
                    ToastMessage.onToast(getActivity(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), Edit_coustomer_details.class);
                   /* st_name = AccountUtils.getName(getActivity());
                    st_email = AccountUtils.getEmail(getActivity());
                    st_mobile = AccountUtils.getMobile(getActivity());*/
                    startActivity(intent);
                }
            }
        });

        tvkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_kycdetails();
            }
        });

        tvbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(activity, Customer_BankDetailslist.class);
                    startActivity(intent);
                }
            }
        });
        tvgiftcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent = new Intent(activity, GiftCardActivity.class);
                    startActivity(intent);
                }
            }
        });
        tvnominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_nomineedetails();
            }
        });

        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent = new Intent(activity, CustomerAddressList.class);
                    startActivity(intent);
                }
            }
        });
       /* if (AccountUtils.getAvathar(activity) != null) {
            Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofileb);
            Glide.with(activity)
                    .load(AccountUtils.getProfileImg(activity))
                    .into(ivprofileb);
        } else {
            ivprofileb.setImageResource(R.drawable.user);
        }*/

        try {
            if (!AccountUtils.getAvathar(activity).equals("")) {
                Glide.with(activity)
                        .load(AccountUtils.getProfileImg(activity))
                        .into(ivprofileb);
            } else {
                ivprofileb.setImageResource(R.drawable.profile);
            }
        } catch (Exception e) {
            ivprofileb.setImageResource(R.drawable.profile);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


//        try {
//            if (!AccountUtils.getAvathar(activity).equals("")) {
//                Glide.with(activity)
//                        .load(AccountUtils.getProfileImg(activity))
//                        .into(ivprofileb);
//            } else {
//                ivprofileb.setImageResource(R.drawable.profile);
//            }
//        } catch (Exception e) {
//            ivprofileb.setImageResource(R.drawable.profile);
//        }

        CustomerDetails();


    }

    public void CustomerDetails() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> getprofile = apiDao.getprofile_details("Bearer " + AccountUtils.getAccessToken(activity));
            getprofile.enqueue(new Callback<Listmodel>() {

                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = Collections.singletonList(response.body());
                        if (list != null) {
                            for (Listmodel listmodel : list) {
                                AccountUtils.setIsPin(activity, listmodel.getIsgspin());


                                String name = listmodel.getName();
                                String id = listmodel.getCustomerId();
                                String Email = listmodel.getEmail();
                                AccountUtils.setEmail(activity, Email);
                                AccountUtils.setCustomerID(activity, id);
                                AccountUtils.setName(activity, name);
                                useremail.setText(listmodel.getEmail());
                                userid.setText(listmodel.getCustomerId());
                                usetname.setText(listmodel.getName());
                                stavathar = listmodel.getAvatar();
                                AccountUtils.setAvathar(activity, stavathar);
                                if (stavathar != null) {
                                    stprofileimg = listmodel.getAvatarImageLink();
                                    AccountUtils.setProfileImg(activity, stprofileimg);
                                    Glide.with(activity)
                                            .load(listmodel.getAvatarImageLink())
                                            .into(ivprofileb);
//                                    Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);

                                } else {
                                    AccountUtils.setAvathar(activity, null);

                                    ivprofileb.setImageResource(R.drawable.profile);
                                }
                                if (listmodel.isTerminated()) {
                                    onlogout();
                                }

                            }
                        }

                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    //   ToastMessage.onToast(activity, "We Have Some Issues", ToastMessage.ERROR);
                    Log.e("Error", t.toString());
                }

            });

        }

    }


    public void intilizeview(View view) {
        tvlogout = view.findViewById(R.id.tvlogout);
        tvlogout.setOnClickListener(this);
        tvchangepass = view.findViewById(R.id.tvchangepass);
        tvchangepass.setOnClickListener(this);
//        ivprofile = view.findViewById(R.id.ivprofileimg);
        tvterms = view.findViewById(R.id.tvterms);
        tvterms.setOnClickListener(this);
        tvrefer = view.findViewById(R.id.tvrefer);
        tvrefer.setOnClickListener(this);
        tvsupport = view.findViewById(R.id.tvsupport);
        tvsupport.setOnClickListener(this);

        TVPIN = view.findViewById(R.id.tvpin);
        TVPIN.setOnClickListener(this);

        tvmoneywallet = view.findViewById(R.id.tvmoneywallet);
        tvmoneywallet.setOnClickListener(this);
        tvfeedback = view.findViewById(R.id.tvfeedback);
        tvfeedback.setOnClickListener(this);
        tvtest3 = view.findViewById(R.id.tvtest3);
        tvtest3.setOnClickListener(this);
        /////

//        if (AccountUtils.getAvathar(activity) != null){
//            Glide.with(activity)
//                    .load(AccountUtils.getProfileImg(activity))
//                    .into(ivprofile);
//
////            Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofile);
//        }else {
//                ivprofile.setImageResource(R.drawable.profile);
//        }
//        ivprofile.setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvlogout:
                onlogout();
                break;
            case R.id.tvchangepass:
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    Intent intent = new Intent(activity, ChangePassword.class);
                    startActivity(intent);
                }
                break;
//            case R.id.ivprofileimg:
//                BottomNavigation(v);
//                break;
            case R.id.tvterms:
                initterms();
                break;

            case R.id.tvsupport:
                initsupport();
                break;
            case R.id.tvrefer:
                initreferandearn();
                break;
            case R.id.tvmoneywallet:
                openmoneywallet();
                break;
            case R.id.tvfeedback:
                openfeedback();
                break;
            case R.id.tvpin:
                createpin();
                break;
            case R.id.tvtest3:
                opencomingsoon();
                break;

        }
    }

    public void openfeedback() {
        Intent intent = new Intent(activity, FeedbackForm.class);
        startActivity(intent);
    }

    public void openmoneywallet() {
        Intent intent = new Intent(activity, AddMonet_to_Wallet.class);
        startActivity(intent);
    }

    public void createpin() {

        Intent intent = new Intent(activity, MPIN.class);
        intent.putExtra("from", "Generatepin");
        intent.putExtra("token", "");
        intent.putExtra("isPin", AccountUtils.getIsPin(activity));
//        sharedPreference.writepinstatus(true);
        startActivity(intent);
//        Intent intent  = new Intent(activity, ComingSoon.class);
//        startActivity(intent);
    }

    public void opencomingsoon() {
        Intent intent = new Intent(activity, ComingSoon.class);
        startActivity(intent);
    }

    public void initreferandearn() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(activity, ReferAndEarnActivity.class);
            startActivity(intent);
        }
    }

    public void initsupport() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(activity, EnquiryandSupport.class);
            startActivity(intent);
        }
    }

    public void initterms() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            String uri = String.format(Locale.ENGLISH, "https://goldsikka.com/privacy-policy");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }


    public void onlogout() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(activity));
            initlogout.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus(false);
//                        sharedPreference.writepinstatus(false);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        //  ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                        // Log.e("responce code", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("logout fail", t.toString());
                    //   ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
                }
            });
        }

    }


    public void BottomNavigation(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.profilebottomview, view.findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tveditprofile = bottomSheet.findViewById(R.id.tvprofileedit);
        tvnominee = bottomSheet.findViewById(R.id.tvnominee);
        tvkyc = bottomSheet.findViewById(R.id.tvkyc);
        tvbank = bottomSheet.findViewById(R.id.tvbankdetails);
        tvaddress = bottomSheet.findViewById(R.id.tvaddress);
        tvemail = bottomSheet.findViewById(R.id.tvemail);
        tvname = bottomSheet.findViewById(R.id.tvname);
        tvcustomerid = bottomSheet.findViewById(R.id.tvcustomerid);
        tvcustomerid.setText("CustomerId : " + AccountUtils.getCustomerID(activity));

        tvname.setText(AccountUtils.getName(activity));
        tvemail.setText(AccountUtils.getEmail(activity));
        tvkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_kycdetails();
            }
        });
        tveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(activity, Edit_coustomer_details.class);
                    intent.putExtra("name", AccountUtils.getName(activity));
                    intent.putExtra("email", AccountUtils.getEmail(activity));
                    intent.putExtra("mobile", AccountUtils.getMobile(activity));
                    intent.putExtra("profileimage", AccountUtils.getProfileImg(activity));
                    startActivity(intent);
                }
            }

        });
        tvnominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_nomineedetails();
            }
        });
        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent = new Intent(activity, CustomerAddressList.class);
                    startActivity(intent);
                }
            }
        });
        tvbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(activity, Customer_BankDetailslist.class);
                    startActivity(intent);
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void get_nomineedetails() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(activity, Nominee_Details.class);
            startActivity(intent);
        }
    }

    public void get_kycdetails() {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> iskyc = apiDao.checkkyc("Bearer " + AccountUtils.getAccessToken(activity));
            iskyc.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (listmodel.isKyc()) {
                            Intent intent = new Intent(activity, Get_kyc_details_fragment.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, Kyc_Details.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                }
            });


        }
    }

}