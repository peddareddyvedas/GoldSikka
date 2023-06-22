package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcomSubcategory extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {
    TextView unameTv, uidTv, titleTv, tvprice, grams, sgrams, sprice;
    RelativeLayout backbtn, rlprice, rlgrams, relativesort;
    shared_preference sharedPreference;
    RecyclerView productsrv, ecomsubcat;
    EcomSubCat_Adapter ecomsubcatadapter;
    EcomSubProductsAdapter subproductsAdapter;

    ArrayList<Listmodel> productsList;
    ArrayList<Listmodel> subcatList;
    ArrayList<Listmodel> goldList;
    ArrayList<Listmodel> silverList;
    ArrayList<Listmodel> tempList = new ArrayList<>();

    ApiDao apiDao, apiDao1;
    private Activity activity;

    List<Listmodel> flist, subflist;

    int selectedPosition = 0;
    RecyclerView recyclerView;
    Spinner pweightspinner;
    Bundle bundle;
    String cid = " ";
    String catName = "sdffv";
    SearchView searchtext;
    RelativeLayout notfound;
    String from = "sdv";
    SwipeRefreshLayout swipe_layout;
    //  ArrayList<Listmodel> catlist;

    String totalprice;

    ArrayList<Listmodel> filteredlist;
    ArrayList<Listmodel> pricefilteredlist;
    String spinnerweight = "gsdfd";
    String locaselectweight;

    String selectprice;
    String localselectprice = "gsdfd";


    String toolname = "";
    String subcat;
    EditText searchView;
    Button goldButton, silverButton;
    RelativeLayout bullionlayout;
    String fromto = "";
    TextView silverprice, silverpricetext;
    String silvertext = " ";
    String liveprice;


    String selectedidd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomsubcategory_activity);
        bundle = getIntent().getExtras();
        cid = bundle.getString("cid");
        toolname = bundle.getString("toolname");
        fromto = bundle.getString("fromto");

        Log.e("fromto", "" + fromto);
        Log.e("cid", "" + cid);

        init();
        intilizerecyclerview();
        getSubCats(cid);
        getliveprices();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(toolname);
        Log.e("toolname", "" + toolname);
        searchView = findViewById(R.id.subcategorysearchview);

        notfound = findViewById(R.id.notfound);
        backbtn = findViewById(R.id.backbtn);
        tvprice = findViewById(R.id.price);
        grams = findViewById(R.id.grams);
        sgrams = findViewById(R.id.sgrams);
        rlprice = findViewById(R.id.rlprice);
        rlgrams = findViewById(R.id.rlgrams);
        sprice = findViewById(R.id.selectprice);
        goldButton = findViewById(R.id.goldbtn);
        silverButton = findViewById(R.id.silverbtn);
        bullionlayout = findViewById(R.id.pricelayout);
        silverprice = findViewById(R.id.silverprice);
        silverpricetext = findViewById(R.id.silvertext);
        relativesort = findViewById(R.id.relativesort);

        if (fromto.equals("value")) {
            bullionlayout.setVisibility(View.VISIBLE);

        } else {
            bullionlayout.setVisibility(View.GONE);
        }
        goldButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                goldButton.setBackgroundResource(R.drawable.buy_amount_button);
                silverButton.setBackgroundResource(R.drawable.buy_grams_button);
                silverButton.setTextColor(getResources().getColor(R.color.black));
                goldButton.setTextColor(getResources().getColor(R.color.white));
                silverprice.setText("Gold Live Price :");
                if (goldButton.getText().equals("Gold")) {
                    Log.e("mygoldsubcats", "" + subcatList);
                    subcatList.clear();
                    subcatList.addAll(goldList);
                    ecomsubcatadapter.notifyDataSetChanged();
                    getliveprices();
                }
            }
        });

        silverButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                goldButton.setBackgroundResource(R.drawable.button_left_border);
                silverButton.setBackgroundResource(R.drawable.button_right_background);
                goldButton.setTextColor(getResources().getColor(R.color.black));
                silverButton.setTextColor(getResources().getColor(R.color.white));
                silverprice.setText("Silver Live Price :");
                if (silverButton.getText().equals("Silver")) {
                    subcatList.clear();
                    subcatList.addAll(silverList);
                    ecomsubcatadapter.notifyDataSetChanged();
                    Log.e("cvhkdsghgb", "" + silvertext);
                    getsilverliveprices();
                }
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pweightspinner = findViewById(R.id.pweightspinner);
        // create list of customer
        ArrayList<String> pweightsList = getPweightsList();
        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.pwightpsinner, pweightsList);
        //Set adapter
        pweightspinner.setAdapter(adapter);
        rlprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottompricealert();
            }
        });
        rlgrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomgramsalert();
            }
        });
        Log.e("selectoncreat1",""+selectedidd);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable name) {
               if (searchView.getText().toString().isEmpty()) {
                   Log.e("selectoncreat2",""+selectedidd);

                 //  getProductsByCats(selectedidd);
               }
                productcategoryFilter(name.toString());
                // subCategoryFilter(name.toString());
            }
        });
    }

    public void productcategoryFilter(String name) {
        ArrayList<Listmodel> filteredList = new ArrayList<>();
        for (Listmodel listmodel : productsList) {
            if (listmodel.getPname().toLowerCase().contains(name.toLowerCase())) {
                filteredList.add(listmodel);
            } else {
            }
            if (filteredList.isEmpty()) {
                productsrv.setVisibility(View.GONE);
                notfound.setVisibility(View.VISIBLE);
            } else {
                notfound.setVisibility(View.GONE);
                productsrv.setVisibility(View.VISIBLE);
            }
        }
        subproductsAdapter.productfilter(filteredList);
    }
    private ArrayList<String> getPweightsList() {
        ArrayList<String> plist = new ArrayList<>();
        plist.add("Select Grams");
        plist.add("1 - 5 gms");
        plist.add("6 - 10 gms");
        plist.add("11 - 15 gms");
        plist.add("16 - 20 gms");
        plist.add("21 - 25 gms");
        plist.add("Above 25 gms");
        return plist;
    }

    public void intilizerecyclerview() {
        ecomsubcat = findViewById(R.id.ecomsubcat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        ecomsubcat.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        ecomsubcat.setHasFixedSize(true);
        subcatList = new ArrayList<>();
        goldList = new ArrayList<>();
        silverList = new ArrayList<>();
        ecomsubcatadapter = new EcomSubCat_Adapter(activity, subcatList, this);
        ecomsubcat.setAdapter(ecomsubcatadapter);
        productsrv = findViewById(R.id.ecomproductsrv);
        productsrv.setHasFixedSize(true);
        productsrv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        productsList = new ArrayList<>();
        // productsList1 = new ArrayList<>();
        subproductsAdapter = new EcomSubProductsAdapter(this, productsList, this, "cats");
        productsrv.setAdapter(subproductsAdapter);
    }
    /////pricefilter
    ImageView imageView_close;
    private RadioGroup radiopiceGroup;
    private RadioButton radioprice;
    RadioButton rbprice, radioshowall, radiolessten, radiotwenty1, radiothirty, radiofifty, radioabovefifty;
    public void bottompricealert() {
        BottomSheetDialog dialog = new BottomSheetDialog(EcomSubcategory.this);
        dialog.setContentView(R.layout.ecompricealert);

        imageView_close = dialog.findViewById(R.id.iv_close);
        radioshowall = dialog.findViewById(R.id.radioshowall);
        radiolessten = dialog.findViewById(R.id.radiolessten);
        radiotwenty1 = dialog.findViewById(R.id.radiotwenty1);
        radiothirty = dialog.findViewById(R.id.radiothirty);
        radiofifty = dialog.findViewById(R.id.radiofifty);
        radioabovefifty = dialog.findViewById(R.id.radioabovefifty);

        radiopiceGroup = dialog.findViewById(R.id.piceradioGroup);
        radiopiceGroup.clearCheck();
        String sdf = AccountUtils.getGrams(getApplicationContext());
        Log.e("sdfsdff", "" + sdf);
        String ggg;
        ggg = sdf;

        int selectedId1 = radiopiceGroup.getCheckedRadioButtonId();
        rbprice = findViewById(selectedId1);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (ggg.contains("Select Price")) {
            radioshowall.setChecked(true);
        } else if (ggg.contains("Upto ₹30000")) {
            radiolessten.setChecked(true);
        } else if (ggg.contains("Upto ₹60000")) {
            radiotwenty1.setChecked(true);
        } else if (ggg.contains("Upto ₹100000")) {
            radiothirty.setChecked(true);
        } else if (ggg.contains("Upto ₹150000")) {
            radiofifty.setChecked(true);
        } else if (ggg.contains("Above ₹150000")) {
            radioabovefifty.setChecked(true);
        }

        radiopiceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Toast.makeText(EcomSubcategory.this, rb.getText(), Toast.LENGTH_SHORT).show();
                String price = rb.getText().toString().trim();
                sprice.setText(price);
                selectprice = price;
                localselectprice = selectprice;
                Log.e("selectprice", "" + selectprice);
                Log.e("localselectprice", "" + localselectprice);
                AccountUtils.setGrams(getApplicationContext(), price);

                if (price.equals("Above ₹150000")) {
                    filterPriceText(150000, 300000);
                } else if (price.equals("Upto ₹30000")) {
                    filterPriceText(0, 30000);
                } else if (price.equals("Upto ₹60000")) {
                    filterPriceText(0, 60000);
                } else if (price.equals("Upto ₹100000")) {
                    filterPriceText(0, 100000);
                } else if (price.equals("Upto ₹150000")) {
                    filterPriceText(0, 150000);
                } else if (price.equals("Select Price")) {
                    filterPriceText(0, 300000);
                } else {
                    innerpricefilter(price);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    /////gramsfilter
    ImageView imageView_close1;
    private RadioGroup gramsradioGroup;
    private RadioButton radiogramsButton;
    RadioButton rb, radioselect, radioone, radiosix, radioeleven, radiosixteen, radiotwenty, radioabovetwentyfive;
    public void bottomgramsalert() {
        BottomSheetDialog dialog = new BottomSheetDialog(EcomSubcategory.this);
        dialog.setContentView(R.layout.ecomgramsalert);
        imageView_close1 = dialog.findViewById(R.id.iv_close);
        radioselect = dialog.findViewById(R.id.radioselect);
        radioone = dialog.findViewById(R.id.radioone);
        radiosix = dialog.findViewById(R.id.radiosix);
        radioeleven = dialog.findViewById(R.id.radioeleven);
        radiosixteen = dialog.findViewById(R.id.radiosixteen);
        radiotwenty = dialog.findViewById(R.id.radiotwenty);
        radioabovetwentyfive = dialog.findViewById(R.id.radioabovetwentyfive);
        gramsradioGroup = dialog.findViewById(R.id.gramsradioGroup);

        String sdfsdff = AccountUtils.getGrams(getApplicationContext());
        Log.e("sdfsdff", "" + sdfsdff);
        String ggggg;
        ggggg = sdfsdff;

        int selectedId = gramsradioGroup.getCheckedRadioButtonId();
        rb = findViewById(selectedId);

        imageView_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (ggggg.contains("Select Grams")) {
            radioselect.setChecked(true);
            filterPriceText(150000, 300000);


        } else if (ggggg.contains("Upto 5 gms")) {
            radioone.setChecked(true);
        } else if (ggggg.contains("Upto 10 gms")) {
            radiosix.setChecked(true);
        } else if (ggggg.contains("Upto 15 gms")) {
            radioeleven.setChecked(true);
        } else if (ggggg.contains("Upto 20 gms")) {
            radiosixteen.setChecked(true);
        } else if (ggggg.contains("Upto 50 gms")) {
            radiotwenty.setChecked(true);
        } else if (ggggg.contains("Above 50 gms")) {
            radioabovetwentyfive.setChecked(true);
        }
        gramsradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                Toast.makeText(EcomSubcategory.this, rb.getText(), Toast.LENGTH_SHORT).show();
                spinnerweight = rb.getText().toString().trim();
                sgrams.setText(spinnerweight);
                locaselectweight = spinnerweight;
                AccountUtils.setGrams(getApplicationContext(), spinnerweight);
                if (spinnerweight.equals("Above 50 gms")) {
                    filterNewText(51.0f, 1000.0f);
                } else if (spinnerweight.equals("Upto 5 gms")) {
                    filterNewText(0.0f, 5.0f);
                } else if (spinnerweight.equals("Upto 10 gms")) {
                    filterNewText(0.0f, 10.0f);
                } else if (spinnerweight.equals("Upto 15 gms")) {
                    filterNewText(0.0f, 15.0f);
                } else if (spinnerweight.equals("Upto 20 gms")) {
                    filterNewText(0.0f, 20.0f);
                } else if (spinnerweight.equals("Upto 50 gms")) {
                    filterNewText(0.0f, 50.0f);
                } else if (spinnerweight.equals("Select Grams")) {
                    filterNewText(0.0f, 1000.0f);
                } else {
                    innerfilter(spinnerweight);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    @Override
    public void onBackPressed() {
        AccountUtils.setGrams(getApplicationContext(), " ");
        AccountUtils.setPrice(getApplicationContext(), " ");
        super.onBackPressed();
    }
    public void getSubCats(String catiddd) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        subcatList.clear();
        goldList.clear();
        silverList.clear();
        tempList.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_ecomsubcategory(catiddd);
        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                dialog.dismiss();
                int statuscode = response.code();
                String type = "";
                Log.e("statuscode", String.valueOf(statuscode));
                flist = response.body();
                subflist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : subflist) {
                            Log.e("typeeeee", "" + listmodel.getTransction_type());

                            if (listmodel.getTransction_type() == null) {
                                Log.e("typeeeee", "if");
                                String live = listmodel.getLivePrice();
                                Log.e("live", "" + live);
                                subcatList.add(listmodel);
                                ecomsubcatadapter.notifyDataSetChanged();
                                relativesort.setVisibility(View.VISIBLE);

                            } /*else if (listmodel.getTransction_type().equals("Gift Card")) {
                                Log.e("typeeeee", "elseif");
                                subcatList.add(listmodel);
                                ecomsubcatadapter.notifyDataSetChanged();
                                relativesort.setVisibility(View.GONE);

                            }*/ else {
                                Log.e("typeeeee", "else");
                                if (listmodel.getTransction_type().equals("gold")) {
                                    goldList.add(listmodel);
                                } else {
                                    silverList.add(listmodel);
                                }
                                Log.e("gold size", "" + goldList.size());
                                Log.e("silver size", "" + silverList.size());
                                type = listmodel.getTransction_type();
                                Log.e("catnametype", "" + type);
                                silvertext = type;
                                relativesort.setVisibility(View.VISIBLE);
                            }
                        }
                        subcatList.addAll(goldList);
                        ecomsubcatadapter.notifyDataSetChanged();


                    } else {
                        dialog.dismiss();
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });

    }

    public void getliveprices() {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getApplicationContext())).create(ApiDao.class);
        Call<Listmodel> getliverates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(getApplicationContext()));
        getliverates.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(@NonNull Call<Listmodel> call, @NonNull Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {

                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            liveprice = listmodel.getSell_price_per_gram();
                            Log.e("liveprice", liveprice);
                            //tv_sellprice.setText(getString(R.string.Rs) + liveprice);
                            silverpricetext.setText("₹" + liveprice + "/Grams");

                        }
                    } else {
                    }
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
                //    ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
            }
        });

    }

    public void getsilverliveprices() {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getApplicationContext())).create(ApiDao.class);
        Call<ResponseBody> getliverates = apiDao.getEcomsilverprice("Bearer " + AccountUtils.getAccessToken(getApplicationContext()));
        getliverates.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                int statuscode = response.code();
                try {
                    String result = response.body().string();
                    Log.e("result", "" + result);
                    silverpricetext.setText("₹" + result + "/Kg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //    ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
            }
        });

    }

    private void spinneralert() {
        pweightspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerweight = parent.getItemAtPosition(position).toString();

                if (spinnerweight.equals("Above 25 gms")) {
                    filterNewText(26.0f, 1000.0f);
                } else if (spinnerweight.equals("Select Grams")) {
                    filterNewText(0.0f, 10000.0f);
                } else {
                    innerfilter(spinnerweight);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    ////////////////////filters///////

    private void innerfilter(String spinnerweight) {
        Log.e("spinnervalue", "" + spinnerweight);

        if (spinnerweight.equals("")) {
            filterNewText(1.0f, 1000.0f);
            Log.e("spinnervalue", "" + spinnerweight);

        } else {
            String filter1 = spinnerweight.replace("gms", "");
            String[] parts = filter1.split("-", 2);
            String part1 = "no data";
            String part2 = "yes data";
            for (String a : parts) {
                part1 = part2;
                part2 = a;
            }

            Log.e("ffgramsp1", part1.trim());
            Log.e("ffgramsp2", part2.trim());

            filterNewText(Float.parseFloat(part1.trim()), Float.parseFloat(part2.trim()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterNewText(Float part1, Float part2) {
        Float lp = Float.valueOf(part1);
        Float hp = Float.valueOf(part2);
        Log.e("lp", " " + lp);
        Log.e("hp", "" + hp);
        filteredlist = new ArrayList<>();
        pricefilteredlist = new ArrayList<>();
        pricefilteredlist.clear();
        sprice.setText("Select Price");
        for (Listmodel item : flist) {
            try {
                if (Float.parseFloat(item.getWeight()) >= lp && Float.parseFloat(item.getWeight()) <= hp) {
                    filteredlist.add(item);

                }
            } catch (Exception e) {
                //  ToastMessage.onToast(getApplicationContext(), "Wait for some time dats is loading ", ToastMessage.ERROR);
            }
        }
        if (filteredlist.isEmpty()) {
            productsrv.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        } else {
            notfound.setVisibility(View.GONE);
            productsrv.setVisibility(View.VISIBLE);
            subproductsAdapter.filterList(filteredlist);
        }
    }

    private void innerpricefilter(String selectprice) {
        String filter1 = selectprice.replace("₹", "");
        String[] parts = filter1.split("-", 2);
        String part1 = "";
        String part2 = "";
        for (String a : parts) {
            part1 = part2;
            part2 = a;
        }
        Log.e("ffprice", part1.trim() + part2.trim());

        filterPriceText(Integer.parseInt(part1.trim()), Integer.parseInt(part2.trim()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterPriceText(Integer pricepart1, Integer pricepart2) {
        int lowprice = pricepart1;
        int highprice = pricepart2;
        pricefilteredlist = new ArrayList<>();
        filteredlist = new ArrayList<>();
        filteredlist.clear();
        sgrams.setText("Select Grams");
        for (Listmodel item : flist) {
            try {
                if (Integer.parseInt(item.getPrice()) >= lowprice && Integer.parseInt(item.getPrice()) <= highprice) {
                    pricefilteredlist.add(item);
                    Log.e("totalpricekkkkk", "" + item);
                }
            } catch (Exception e) {
                // ToastMessage.onToast(getApplicationContext(), "Wait for some time dats is loading ", ToastMessage.ERROR);
            }
        }
        if (pricefilteredlist.isEmpty()) {
            productsrv.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        } else {
            notfound.setVisibility(View.GONE);
            productsrv.setVisibility(View.VISIBLE);
            subproductsAdapter.filterList(pricefilteredlist);
        }
    }
    //////////

    ////EcomSubcatlist
    public class EcomSubCat_Adapter extends RecyclerView.Adapter<EcomSubCat_Adapter.ViewHolder> {
        private Context context;
        ArrayList<Listmodel> list;
        OnItemClickListener itemClickListener;

        public EcomSubCat_Adapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener) {
            this.context = context;
            this.list = list;
            this.itemClickListener = itemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ecomsubcat_list, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Listmodel listmodel = list.get(position);
            String name = listmodel.getSubcatname();
            Log.e("pname", "" + listmodel.getSubcatname());

            String image = listmodel.getImage_uri();
            holder.tv_category.setText(name);

            holder.tv_category.setText(name);
            Glide.with(context).load(image).into(holder.iv_categoryimg);

            if (selectedPosition == position) {
                Log.e("if", "" + position);
                holder.textselectline.setVisibility(View.VISIBLE);
                productsList.clear();
                Log.e("Sdfasdfasd", "" + AccountUtils.getproductselectid(getApplicationContext()));


                getProductsByCats(listmodel.getId());
                selectedidd = listmodel.getId();
                Log.e("oncreat",""+selectedidd);

            } else {
                holder.textselectline.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        Log.e("getidddddddd", "" + listmodel.getId());
                        productsList.clear();

                        getProductsByCats(listmodel.getId());
                        selectedidd = listmodel.getId();

                        Log.e("selectoncreat",""+selectedidd);

//                        getProductBullion( );
                    } else {

                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            Log.e("getsubcatlist", "" + list.size());
            return list.size();
        }

        public void subfilterList2(ArrayList<Listmodel> filteredList) {
            list = filteredList;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_categoryimg;
            TextView tv_category, textselectline;
            LinearLayout llsubcatlist;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_category = itemView.findViewById(R.id.tv_category);
                iv_categoryimg = itemView.findViewById(R.id.iv_categoryimg);
                llsubcatlist = itemView.findViewById(R.id.llsubcatlist);
                textselectline = itemView.findViewById(R.id.textselectline);
            }
        }
    }

    public void getProductsByCats(String id) {
        productsList.clear();
            /*final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();*/
        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        getproductsbycat = apiDao.get_ecomsubcategoryproducts(id);
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                //  dialog.dismiss();
                Log.e("subproductstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("subproductlist", String.valueOf(statuscode));
                    if (flist != null) {
                        productsList.clear();
                    //    searchView.setText("");
                        //   Collections.shuffle(flist);
                        for (Listmodel listmodel : flist) {
                            cid = listmodel.getId();
                            if (spinnerweight.equals(locaselectweight)) {
                                Log.e("ddddd", "" + spinnerweight);
                                Log.e("fffdddd", "" + locaselectweight);
                                if (spinnerweight.equals("Above 50 gms")) {
                                    filterNewText(51.0f, 1000.0f);
                                } else if (spinnerweight.equals("Select Grams")) {
                                    filterNewText(0.0f, 1000.0f);
                                } else {
                                    innerfilter(spinnerweight);
                                }
                            } else if (localselectprice.equals(selectprice)) {
                                if (localselectprice.equals("Above ₹150000")) {
                                    filterPriceText(150000, 300000);
                                } else if (localselectprice.equals("Upto ₹30000")) {
                                    filterPriceText(0, 30000);
                                } else if (localselectprice.equals("Select Price")) {
                                    filterPriceText(0, 300000);
                                } else {
                                    innerpricefilter(localselectprice);
                                }
                            } else {
                                productsList.add(listmodel);
                                subproductsAdapter.notifyDataSetChanged();
                            }
                            //  spinneralert();
                        }
                    } else {
                        // dialog.dismiss();
                        Log.e("catname", "No cats");
                    }
//                    openpopupscreen(listmodel.getDescription());
                } else if (statuscode == 422) {
                    // dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
                } else {
                    //  dialog.dismiss();
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Please try again", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });
    }

    ///Ecom sucatproductlist
    public class EcomSubProductsAdapter extends RecyclerView.Adapter<EcomSubProductsAdapter.ViewHolder> {

        private Context context;
        ArrayList<Listmodel> list;
        OnItemClickListener itemClickListener;
        String from = "sdf";
        List<Listmodel> flistwish = new ArrayList<>();
        List<String> checklist = new ArrayList<String>();

        public EcomSubProductsAdapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener, String from) {
            this.context = context;
            this.list = list;
            this.itemClickListener = itemClickListener;
            this.from = from;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ecomproductitemhome, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void filterList(ArrayList<Listmodel> filterllist) {
            list = filterllist;
            notifyDataSetChanged();
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Listmodel listmodel = list.get(position);
            String name = listmodel.getPname();
            String image = listmodel.getImage_uri();
            String weight = listmodel.getWeight();
            String tprice = listmodel.getPrice();
            holder.pid = listmodel.getId();
            Log.e("cid", listmodel.getCid());
            Log.e("id", listmodel.getId());
            Log.e("sid", listmodel.getSid());

            if (listmodel.getTransction_type().equals("Gift Card")) {
                holder.gramsimage.setVisibility(View.GONE);
                holder.pweightTv.setVisibility(View.GONE);
            } else {
                holder.gramsimage.setVisibility(View.VISIBLE);
                holder.pweightTv.setVisibility(View.VISIBLE);
            }

            if (listmodel.getTransction_type().equals("silver")) {
                holder.gramsimage.setBackgroundResource(R.drawable.ic_silver);
            } else {
                holder.gramsimage.setBackgroundResource(R.drawable.tsellgold);
            }

            holder.pname.setText(name);
            holder.pweightTv.setText("  " + weight + " gms");
            holder.pprice.setText("₹" + tprice);

            try {
                Glide.with(context).load(image).into(holder.pimg);
            } catch (Exception ignored) {
                Glide.with(context).load(R.drawable.background_image).into(holder.pimg);
            }

            holder.pimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Ecommproductdiscreption.class);
                    intent.putExtra("id", holder.pid);
                    intent.putExtra("catrgoryname", toolname);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            holder.pimg,
                            "pimgtransition");

                    ActivityCompat.startActivity(context, intent, options.toBundle());
                    Log.e("pid", holder.pid);
                }
            });
            holder.linearsubitemcatlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Ecommproductdiscreption.class);
                    intent.putExtra("id", holder.pid);
                    intent.putExtra("catrgoryname", toolname);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            holder.pimg,
                            "pimgtransition");

                    ActivityCompat.startActivity(context, intent, options.toBundle());
                    Log.e("pid", holder.pid);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void productfilter(ArrayList<Listmodel> filteredList) {
            list = filteredList;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView pimg, selectwish, gramsimage;
            TextView pname, pweightTv, pprice;
            String pid = "24";

            CardView linearsubitemcatlist;
            ApiDao apiDao;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                pname = itemView.findViewById(R.id.pname);
                pweightTv = itemView.findViewById(R.id.pweightTv);
                pimg = itemView.findViewById(R.id.pimg);
                selectwish = itemView.findViewById(R.id.selectwish);
                pprice = itemView.findViewById(R.id.pprice);
                linearsubitemcatlist = itemView.findViewById(R.id.linearsubitemcatlist);
                gramsimage = itemView.findViewById(R.id.gramsimage);
//            selectwish.setOnClickListener(this);


            }


        }
    }

}
