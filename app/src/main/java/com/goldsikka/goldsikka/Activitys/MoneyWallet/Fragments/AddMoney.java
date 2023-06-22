package com.goldsikka.goldsikka.Activitys.MoneyWallet.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.RazorpayPayment;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Withdraw_popup;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class AddMoney extends Fragment implements View.OnClickListener {

    EditText et_add_money;
    Button btn_add;

    String st_money = "N/A ";

    ApiDao apiDao;

    TextView tv_withdraw;

    TextView tvwalletmoney;

    String Walletamount;

    TextView unameTv, uidTv;
    private Activity activity;

    Listmodel list;
    String stForm;
    float stRating = 0;
    Dialog dialog1;
    int ii = 0;
    //    ApiDao apiDao;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_money, container, false);
        ButterKnife.bind(view, activity);
        initlizeview(view);
        wallet_amount();
        //Checkout.preload(activity);

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        return view;
    }

    public void wallet_amount() {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.walletAmount("Bearer " + AccountUtils.getAccessToken(activity));
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        Walletamount = list.getAmount_wallet();
                        tvwalletmoney.setText("Account Balance : " + getString(R.string.Rs) + " " + Walletamount);

                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                        //   ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //  ToastMessage.onToast(activity, "We have some issue", ToastMessage.ERROR);
                }
            });
        }

    }

    public void initlizeview(View view) {
        tvwalletmoney = view.findViewById(R.id.tvwalletmoney);
        et_add_money = view.findViewById(R.id.et_addmoney);
        btn_add = view.findViewById(R.id.btn_addmoney);
        tv_withdraw = view.findViewById(R.id.tv_withdraw);
        tv_withdraw.setOnClickListener(this);
        btn_add.setOnClickListener(this::onClick);

        et_add_money.setHint(Html.fromHtml(getString(R.string.amount)));

        et_add_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("editonelakhamount", "" + s);
                int a = 0;
                String editmoney = "N/A";
                editmoney = s.toString().trim();
                try {
                    a = Integer.parseInt(editmoney);
                } catch (NumberFormatException nfe) {
                    // Handle the condition when str is not a number.
                }
                if (a > 100000) {
                    ToastMessage.onToast(activity, "Please add max 1,00,000", ToastMessage.ERROR);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addmoney:
                addmoney();
                break;

            case R.id.tv_withdraw:
                opneWithdrae();
                break;

        }
    }

    public void opneWithdrae() {
        Intent intent = new Intent(activity, Withdraw_popup.class);
        intent.putExtra("Walletamount", Walletamount);
        startActivity(intent);
    }

    public void addmoney() {
        st_money = et_add_money.getText().toString().trim();
        try {
            ii = Integer.parseInt(st_money);
        } catch (NumberFormatException nfe) {
            // Handle the condition when str is not a number.
        }

        if (st_money.isEmpty()) {
            ToastMessage.onToast(activity, "Please enter amount", ToastMessage.ERROR);
        } else if (ii < 100) {
            ToastMessage.onToast(activity, "Please add min 100rs", ToastMessage.ERROR);
        } else if (ii > 100000) {
            ToastMessage.onToast(activity, "Please add max 1,00,000rs", ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(activity, RazorpayPayment.class);
            intent.putExtra("amount", st_money);
            startActivityForResult(intent, 1212);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212) {
            String paymentid = data.getStringExtra("PaymentID");
            sendMoney(paymentid);
            Log.e("Payment Result", String.valueOf(paymentid));
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        super.onDetach();
    }


    public void sendMoney(String paymentid) {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.addmoney_wallet("Bearer " + AccountUtils.getAccessToken(activity), st_money, paymentid);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        et_add_money.setText("");
                        wallet_amount();

                        ToastMessage.onToast(activity, "Money Added", ToastMessage.SUCCESS);

                        successAlert();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        //    ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //   ToastMessage.onToast(activity, "We have some issue", ToastMessage.ERROR);
                }
            });
        }

    }


    private void successAlert() {
        dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.feedbackdilog);
        dialog1.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


        RatingBar rating = (RatingBar) dialog1.findViewById(R.id.rating);
        EditText etFeedback = (EditText) dialog1.findViewById(R.id.et_feedback_form);
        etFeedback.setHint(Html.fromHtml(getString(R.string.enterfeedbacktext)));

        Button dialogButton = (Button) dialog1.findViewById(R.id.close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        Button btn_submit = (Button) dialog1.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stForm = etFeedback.getText().toString().trim();
                stRating = rating.getRating();
                if (stForm.isEmpty()) {
                    ToastMessage.onToast(getApplicationContext(), "Please enter feedback form", ToastMessage.ERROR);

                } else if (stRating == 0.0) {
                    ToastMessage.onToast(getApplicationContext(), "Please rate ", ToastMessage.ERROR);

                } else {
                    sendFeedback();
                }
            }
        });

        dialog1.show();
    }


    public void sendFeedback() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(getContext())) {
            ToastMessage.onToast(getContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getContext())).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.feedbackForm("Bearer " + AccountUtils.getAccessToken(getContext()), stForm, String.valueOf(stRating));

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("Status COde Form", String.valueOf(statuscode));
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_CREATED) {
                        ToastMessage.onToast(getApplicationContext(), list.getMessage(), ToastMessage.SUCCESS);
                        dialog1.dismiss();
                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                        //ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    // ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }


}
