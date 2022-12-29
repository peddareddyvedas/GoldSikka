package com.goldsikka.goldsikka.Activitys.Predict_price;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.Fragments.MainFragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.PredictionListModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class Predicted_List extends AppCompatActivity implements OnItemClickListener, View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rv_predictedlist;
    ArrayList<PredictionListModel> arrayList;
    predictlist_adapter adapter;
    ApiDao apiDao;
    AlertDialog alertDialogdialog;

    TextView tv_liverete,tv_showerror,tv_price_timeing,tvsubmission;
    EditText et_updateprice;
    Button btn_submit;
    ImageView img_close,iv_close;
    String st_predictprice,st_data;

    LinearLayout ll_success,linearlayout;

    ImageView imageView_close;
    TextView tv_msg;
    String liveprice;

    SwipeRefreshLayout swipeRefresh;
    // ArrayList<Listmodel> pagination;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;

    int page_no;
    int next_page;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predicted__list);

        swipeRefresh =findViewById(R.id.swipeRefresh);
        intilizerecyclerview();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Prediction list");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Prediction List");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        linearlayout = findViewById(R.id.linearlayout);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        ll_success = findViewById(R.id.ll_success);
        tvsubmission =findViewById(R.id.tvsubmission);

        swipeRefresh.setOnRefreshListener(this);
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            getdata();
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getdata(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            swipeRefresh.setRefreshing(false);
            return;
        }else {
            arrayList.clear();
            final ProgressDialog dialog = new ProgressDialog(Predicted_List.this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<PredictionListModel> call = apiDao.prediction_list("Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<PredictionListModel>() {

                @Override
                public void onResponse(@NonNull Call<PredictionListModel> call, @NonNull retrofit2.Response<PredictionListModel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        List<PredictionListModel> listmodel = response.body().getResult();
                        PredictionListModel list1 = response.body();


                        //   linearlayout.setVisibility(View.GONE);

                        if (listmodel.size() != 0) {

                            page_no = list1.getCurrent_page();
                            totalPage = list1.getLast_page();

                            next_page = page_no + 1;
                            for (PredictionListModel list : listmodel) {

                                final ArrayList<PredictionListModel> items = new ArrayList<>();


                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        //for(int i = 0; i <= 10; i++) {
                                        items.add(list);
                                        //}
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
//

                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);

                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {
                                            // adapter.addLoading();
                                            // isLastPage = true;
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);
                            }
                        } else {
                            linearlayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            ToastMessage.onToast(Predicted_List.this, "No Data Available ", ToastMessage.ERROR);
                        }

                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(Predicted_List.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<PredictionListModel> call, @NonNull Throwable t) {
                    dialog.dismiss();
                  //  Toast.makeText(Predicted_List.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public  void intilizerecyclerview(){

        rv_predictedlist = findViewById(R.id.rv_predictedlist);

        rv_predictedlist.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_predictedlist.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new predictlist_adapter(arrayList,this);
        rv_predictedlist.setAdapter(adapter);


        rv_predictedlist.addOnScrollListener(new PaginationListener(layoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
               // currentPage++;

                if(totalPage != page_no){
                    Apicall_page();
                }else{
                    adapter.removeLoading();
                    //   Toast.makeText(Predicted_List.this, "Nodata", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        // Apicall();



    }

    @Override
    public void onItemClick(View view, int position) {

    }
    public void initprice_edit(String id){
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.edit_predictedpricepopup,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog = alertdilog.create();

        init_pricetimeings();
        img_close = dialogview.findViewById(R.id.img_close);
      //  img_close.setOnClickListener(this::onClick);

        tv_liverete = dialogview.findViewById(R.id.tv_liverete);

        tv_price_timeing = dialogview.findViewById(R.id.tv_price_timeing);
        getliveprices();

        ImageView iv_liveprice = dialogview.findViewById(R.id.iv_liveprice);
        iv_liveprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_liverete.setVisibility(View.GONE);
                ProgressDialog  dialog = new ProgressDialog(Predicted_List.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait..");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getliveprices();
                        tv_liverete.setVisibility(View.VISIBLE);
                        tv_liverete.setText("Live Rate : "+getString(R.string.Rs)+liveprice);
                        dialog.dismiss();
                    }
                },1500);
            }
        });

        tv_showerror = dialogview.findViewById(R.id.tv_showerror);
        et_updateprice = dialogview.findViewById(R.id.et_updateprice);

        btn_submit = dialogview.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation(id);

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogdialog.dismiss();
              //  Toast.makeText(Predicted_List.this, "close", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogdialog.show();

    }
    public void getliveprices(){

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> getliverates = apiDao.getlive_rates("Bearer "+AccountUtils.getAccessToken(this));
        getliverates.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){

                    if (list != null){
                        for (Listmodel listmodel : list) {


//                            st_buyprice =
//                            st_sellprice = listmodel.getSell_price_per_gram();
//                            st_goldtype = listmodel.getGold_type();
//
//                            String purity = listmodel.getGold_purity();


                            liveprice = listmodel.getSell_price_per_gram();
                            tv_liverete.setText("Live Rate : "+getString(R.string.Rs)+liveprice);
                        }
                    }
                    else {
//                        tv_buyprice.setText(getString(R.string.Rs)+"0.000");
//                        tv_sellprice.setText(getString(R.string.Rs)+"0.000");
                    }
                }

                else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        JSONObject er = jObjError.getJSONObject("errors");
//                        Toast.makeText(Predicted_List.this, st, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
               // ToastMessage.onToast(Predicted_List.this,"We have some issues",ToastMessage.ERROR);
            }
        });
    }

    private void Apicall_page() {
          // arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(Predicted_List.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<PredictionListModel> call = apiDao.PredictPageLoader(String.valueOf(next_page), "Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<PredictionListModel>() {

                @Override
                public void onResponse(@NonNull Call<PredictionListModel> call, @NonNull retrofit2.Response<PredictionListModel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        List<PredictionListModel> listmodel = response.body().getResult();
                        PredictionListModel list1 = response.body();


                        if (listmodel.size() != 0) {

                            linearlayout.setVisibility(View.GONE);
                            page_no = list1.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNoP", String.valueOf(page_no));
                            Log.e("Next pageP + ", String.valueOf(next_page));

                            totalPage = list1.getLast_page();
                            Log.e("lastpageP", String.valueOf(totalPage));

                            for (PredictionListModel list : listmodel) {

                                final ArrayList<PredictionListModel> items = new ArrayList<>();

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        //  for (int i = 0; i < 10; i++) {
                                        //itemCount++;
                                        items.add(list);
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        //  }
                                        // do this all stuff on Success of APIs response

                                        /**
                                         * manage progress view
                                         */

                                        if (currentPage != PAGE_START) adapter.removeLoading();
                                        adapter.addItems(items);
                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {
                                            // adapter.addLoading();
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;
                                    }
                                }, 100);
                            }
                        } else {
                            linearlayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            ToastMessage.onToast(Predicted_List.this, "No Data Available ", ToastMessage.SUCCESS);
                        }
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(Predicted_List.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<PredictionListModel> call, @NonNull Throwable t) {
                    dialog.dismiss();
                  //  Toast.makeText(Predicted_List.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void validation(String id){
        tv_showerror.setVisibility(View.GONE);

        st_predictprice = et_updateprice.getText().toString().trim();

        if (st_predictprice.isEmpty()){
            tv_showerror.setVisibility(View.VISIBLE);
            tv_showerror.setText("Please enter the Price.");
        }
        else {
            init_updated_price_submition(id);
        }
    }
    public void init_pricetimeings(){

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Listmodel> predict_timeings = apiDao.getpredict_timeings("Bearer "+AccountUtils.getAccessToken(this));
        predict_timeings.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    String data  = listmodel.getData();
                    tv_price_timeing.setText(data);
                    //InItPriceTimeings(data);

                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
              //  ToastMessage.onToast(Predicted_List.this,"we have some issues",ToastMessage.SUCCESS);
            }
        });



    }

    public void init_updated_price_submition(String  id){

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> predict_price_update = apiDao.predict_updatedpricesubmit("Bearer " + AccountUtils.getAccessToken(this),
                    id, st_predictprice);
            predict_price_update.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        alertDialogdialog.dismiss();

                        st_data = listmodel.getData();
                        //ToastMessage.onToast(Predicted_List.this,"Updated.",ToastMessage.SUCCESS);
                        ll_success.setVisibility(View.VISIBLE);
                        tvsubmission.setText(st_data);

                    } else {
                        ll_success.setVisibility(View.GONE);
                        alertDialogdialog.dismiss();
                        assert response.errorBody() != null;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("data");
                            //ToastMessage.onToast(Predicted_List.this,st,ToastMessage.INFO);
                            initpopup(st);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    alertDialogdialog.dismiss();
                    Log.e("updated price", t.toString());
                }
            });

    }
    AlertDialog alertDialogdialog1;
    public  void  initpopup(String msg){

        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.popup_message_screen,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog1 = alertdilog.create();


        imageView_close = dialogview.findViewById(R.id.iv_close);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogdialog1.dismiss();
            }
        });

        tv_msg = dialogview.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        alertDialogdialog1.show();



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                alertDialogdialog.dismiss();
                ll_success.setVisibility(View.GONE);
                getdata();
                break;

        }
    }

    @Override
    public void onRefresh() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            swipeRefresh.setRefreshing(false);

        } else {
            itemCount = 0;
            currentPage = PAGE_START;
            isLastPage = false;
            adapter.clear();
            getdata();
        }
    }

    class predictlist_adapter extends RecyclerView.Adapter<BaseViewHolder> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;
        OnItemClickListener itemClickListener;

        ArrayList<PredictionListModel> list;

        public predictlist_adapter(ArrayList<PredictionListModel>  postItems,OnItemClickListener itemClickListener) {
            this.list = postItems;
            this.itemClickListener = itemClickListener;
        }

        @NonNull @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new ViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.predictlist, parent, false));
                case VIEW_TYPE_LOADING:
                    return new ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.onBind(position);



        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == list.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public void addItems(ArrayList<PredictionListModel>  postItems) {
            list.addAll(postItems);
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            list.add(new PredictionListModel());
            notifyItemInserted(list.size() - 1);
        }

        public void addLoading1() {
            isLoaderVisible = true;
            list.add(new PredictionListModel());
            notifyItemInserted(list.size());
        }

        public void removeLoading1() {
            isLoaderVisible = false;
            int position = list.size() -1;
            PredictionListModel item = getItem(position);
            if (item != null) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = list.size() - 1;
            PredictionListModel item = getItem(position);
            if (item != null) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            list.clear();
            notifyDataSetChanged();
        }

        PredictionListModel getItem(int position) {
            return list.get(position);
        }

        public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

            TextView tv_date,tv_predictedprice,tv_status,tvpredecttext;
            ImageView iv_priceedit;
            LinearLayout ll_predect,llpredectedit;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                tvpredecttext = itemView.findViewById(R.id.tvpredecttext);
                tv_date = itemView.findViewById(R.id.tv_updated);
                tv_predictedprice = itemView.findViewById(R.id.tv_predictedprice);
                tv_status = itemView.findViewById(R.id.tv_status);
                ll_predect = itemView.findViewById(R.id.ll_predect);
                llpredectedit = itemView.findViewById(R.id.llpredectedit);
                iv_priceedit = itemView.findViewById(R.id.iv_priceedit);
                iv_priceedit.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
            protected void clear() {

            }

            public void onBind(int position) {
                super.onBind(position);

                PredictionListModel listmodel = list.get(position);
                String  statuss = listmodel.getStatus();
              //  Log.e("statusas",statuss);

                tv_date.setText(listmodel.getCreated_date());
                tv_status.setText(statuss);
                tv_predictedprice.setText(Predicted_List.this.getResources().getString(R.string.Rs)+listmodel.getPrice_predicted());
                String update = listmodel.getUpdate();
                Log.e("edit ",update);
                if (update.equals("0")){
                    llpredectedit.setVisibility(View.GONE);
                }
                else {
                    llpredectedit.setVisibility(View.VISIBLE);
                }
                tvpredecttext.setText(listmodel.getPredicted_message());

                iv_priceedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NetworkUtils.isConnected(Predicted_List.this)){
                            ToastMessage.onToast(Predicted_List.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        }else {
                            initprice_edit(listmodel.getId());
                        }
                    }
                });

            }
        }

        public class ProgressHolder extends BaseViewHolder {
            ProgressHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            protected void clear() {
            }
        }

    }
}