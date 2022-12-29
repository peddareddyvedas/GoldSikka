package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.List;

public class Scheme_Faqs_Adapter extends RecyclerView.Adapter<Scheme_Faqs_Adapter.ViewHolder> {

    Context context;
    List<Listmodel> list;

    public Scheme_Faqs_Adapter(Context context,List<Listmodel> listmodels){
        this.context = context;
        this.list = listmodels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.scheme_faqs_adapter,parent,false);
            return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Listmodel listmodel = list.get(position);
            holder.tv_faqexplanation.setText(listmodel.getAnswers());
            holder.tv_schemefaqs.setText(listmodel.getQuestions());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_schemefaqs,tv_faqexplanation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_faqexplanation = itemView.findViewById(R.id.tv_faqexplanation);
            tv_schemefaqs = itemView.findViewById(R.id.tv_schemefaqs);
        }
    }
}
