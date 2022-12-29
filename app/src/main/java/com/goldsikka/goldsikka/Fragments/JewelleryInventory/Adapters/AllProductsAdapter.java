package com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.JewelleryHome;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.ProductDescription;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;
    String from = "sdf";

    ApiDao apiDao;
    Listmodel pflist;

    List<Listmodel> flistwish = new ArrayList<>();
    List<String> checklist = new ArrayList<String>();

    int abc = 10;

    public AllProductsAdapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener, String from) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
        this.from = from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.productitemhome, parent, false);

        return new ViewHolder(view);
    }

    public List<String> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<String> checklist) {
        this.checklist = checklist;
    }

    public List<Listmodel> getFlistwish() {
        return flistwish;
    }

    public void setFlistwish(List<Listmodel> flistwish) {
        this.flistwish = flistwish;
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
        String image = listmodel.getPimg();
        String weight = listmodel.getPweight();
        holder.pid = listmodel.getId();

        holder.pname.setText(name);
        holder.pweightTv.setText("  " + weight + " gms");
        Log.e("pimage", listmodel.getPimg());
//        Log.e("checklist", String.valueOf(checklist));

        boolean checkwish = checklist.contains(holder.pid);

        if (checkwish) {
//            Log.e("checklist", String.valueOf(checkwish));
            holder.selectwish.setImageResource(R.drawable.ic_like_on);
        } else {
//            Log.e("checklist", String.valueOf(checkwish));
            holder.selectwish.setImageResource(R.drawable.ic_like_offfgt);
        }

        try {
            Glide.with(context).load(image).into(holder.pimg);
        } catch (Exception ignored) {
            Glide.with(context).load(R.drawable.background_image).into(holder.pimg);
        }

        holder.pimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDescription.class);
                intent.putExtra("pid", holder.pid);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        holder.pimg,
                        "pimgtransition");

                ActivityCompat.startActivity(context, intent, options.toBundle());
                Log.e("pid", holder.pid);
            }
        });

        holder.selectwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postwishlist(holder, listmodel.getId(), listmodel.getSum());
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void postwishlist(ViewHolder holder, String pid, int sum) {
        Log.e("sum value", String.valueOf(sum));

//        final ProgressDialog dialog = new ProgressDialog(context);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
        Log.e("token", AccountUtils.getAccessToken(context));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(context)).create(ApiDao.class);
        if (holder.selectwish.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_like_on).getConstantState()) {
//           if(sum!=0){
//               ToastMessage.onToast(context, "You can't Delete this Jewellery", ToastMessage.ERROR);
//           }else{
            Call<Listmodel> deletewishlist = apiDao.deletewishlist("Bearer " + AccountUtils.getAccessToken(context), pid);
            deletewishlist.enqueue(new Callback<Listmodel>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("poststatuscode", String.valueOf(statuscode));
                    Log.e("poststatuscodeerror", String.valueOf(response));
//                    dialog.dismiss();
                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        holder.selectwish.setImageResource(R.drawable.ic_like_offfgt);
                        if (from.equals("home"))
                            ((JewelleryHome) context).getWishlistCount();
                        Log.e("context", from);
                    } else if (statuscode == 422) {
//                        dialog.dismiss();
//                           String message = response.body().getMessage();
                        Log.e("cv", String.valueOf(statuscode));
                        ToastMessage.onToast(context, "try again", ToastMessage.ERROR);

                    } else if (statuscode == 404) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String st = jsonObject.getString("message");
                            ToastMessage.onToast(context, st, ToastMessage.ERROR);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Log.e("cv", String.valueOf(statuscode));
                       // ToastMessage.onToast(context, "You can't delete this Jewellery as you are accumalated", ToastMessage.ERROR);
//                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
//                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
//           }
        } else {
            Call<Listmodel> postwishlist = apiDao.postwishlist("Bearer " + AccountUtils.getAccessToken(context), pid);
            postwishlist.enqueue(new Callback<Listmodel>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("poststatuscode", String.valueOf(statuscode));
                    Log.e("poststatuscodeerror", String.valueOf(response));
                    pflist = response.body();
//                    dialog.dismiss();
                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        holder.selectwish.setImageResource(R.drawable.ic_like_on);
                        if (from.equals("home"))
                            ((JewelleryHome) context).getWishlistCount();
                        Log.e("context", from);
                    } else if (statuscode == 422) {
//                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
//                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
//                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView pimg, selectwish;
        TextView pname, pweightTv;
        String pid = "24";


        ApiDao apiDao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pname = itemView.findViewById(R.id.pname);
            pweightTv = itemView.findViewById(R.id.pweightTv);
            pimg = itemView.findViewById(R.id.pimg);
            selectwish = itemView.findViewById(R.id.selectwish);
//            selectwish.setOnClickListener(this);
            pimg.setOnClickListener(this);
            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {

//            if(v.getId()==R.id.selectwish) {
////                addToWishlist(pid);
//                Log.e("wishlist", "sgs");
//                itemClickListener.onItemClick(v, getAdapterPosition());
//            }
//            }else if(v.getId()==R.id.pimg) {
//            }
        }
    }
}
