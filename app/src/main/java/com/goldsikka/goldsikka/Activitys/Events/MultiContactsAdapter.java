package com.goldsikka.goldsikka.Activitys.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactsModal;
import com.goldsikka.goldsikka.R;

import java.util.ArrayList;

public class MultiContactsAdapter extends RecyclerView.Adapter<MultiContactsAdapter.ViewHolder> {

    // creating variables for context and array list.
    private Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;

    // creating a constructor
    public MultiContactsAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false));

    }

    // below method is use for filtering data in our array list
    public void filterList(ArrayList<ContactsModal> filterllist) {
        // on below line we are passing filtered
        // array list in our original array list
        contactsModalArrayList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // getting data from array list in our modal.
        ContactsModal modal = contactsModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.tvcontactname.setText(modal.getContacytName());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getRandomColor();


        // below text drawable is a circular.
        TextDrawable drawable2 = TextDrawable.builder().beginConfig()
                .width(100)  // width in px
                .height(100) // height in px
                .endConfig()
                // as we are building a circular drawable
                // we are calling a build round method.
                // in that method we are passing our text and color.
                .buildRound(modal.getContacytName().substring(0, 1), color);
        // setting image to our image view on below line.
        holder.ivcontactimg.setImageDrawable(drawable2);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
              /*  Intent i = new Intent(context, ContactDetailActivity.class);
                i.putExtra("name", modal.getUserName());
                i.putExtra("contact", modal.getContactNumber());
                // on below line we are starting a new activity,
                context.startActivity(i);*/
            }
        });

        holder.bind(contactsModalArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.

        private TextView tvcontactname, tvnumber;
        private ImageView ivcontactimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvcontactname = itemView.findViewById(R.id.tvcontactname);
            ivcontactimg = itemView.findViewById(R.id.ivcontactimg);

        }


        void bind(final ContactsModal contactsModal) {

            ivcontactimg.setVisibility(contactsModal.isChecked() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactsModal.setChecked(!contactsModal.isChecked());
                    ivcontactimg.setVisibility(contactsModal.isChecked() ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    public ArrayList<ContactsModal> getSelected() {
        ArrayList<ContactsModal> selected = new ArrayList<>();
        for (int i = 0; i < contactsModalArrayList.size(); i++) {
            if (contactsModalArrayList.get(i).isChecked()) {
                selected.add(contactsModalArrayList.get(i));
            }
        }
        return selected;
    }
}