package com.goldsikka.goldsikka.NewDesignsActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactListItemSelector;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactRVAdapter;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactsModal;
import com.goldsikka.goldsikka.Activitys.GiftModuleActivity;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class GiftContactList extends AppCompatActivity {

    private ArrayList<ContactsModal> contactsModalArrayList;
    private RecyclerView contactRV;
    private ContactlistAdapter contactRVAdapter;
    Cursor cursor;

    TextView unameTv, uidTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giftcontactlist);
        contactsModalArrayList = new ArrayList<>();
        contactRV = findViewById(R.id.idRVContacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        prepareContactRV();


        requestPermissions();

        searchview();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchview() {

        SearchView searchView = (SearchView) findViewById(R.id.searchview);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contactRVAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                contactRVAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private void prepareContactRV() {
        // in this method we are preparing our recycler view with adapter.
        contactRVAdapter = new ContactlistAdapter(this, contactsModalArrayList);
        // on below line we are setting layout manager.
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        // on below line we are setting adapter to our recycler view.
        contactRV.setAdapter(contactRVAdapter);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(GiftContactList.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GiftContactList.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        } else {
            requestPermissions();
        }
    }

    private void getContacts() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {


                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contactsModalArrayList.add(new ContactsModal(phonenumber, name));

            }
        }

        cursor.close();
        contactRVAdapter.notifyDataSetChanged();
    }

//
//    public void gotonext(){
//        Intent intent = new Intent(GiftContactList.this, GiftModuleActivity.class);
//        intent.putExtra("phone_number", number);
//        startActivity(intent);
//    }


    public class ContactlistAdapter extends RecyclerView.Adapter<ContactlistAdapter.ViewHolder> implements Filterable {


        Context context;
        ArrayList<ContactsModal> list;
        ArrayList<ContactsModal> listfilter;

        public ContactlistAdapter(Context context, ArrayList<ContactsModal> arrayList) {
            this.context = context;
            this.list = arrayList;
            this.listfilter = arrayList;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.transfercontactlistitems, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ContactsModal modal = listfilter.get(position);
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getRandomColor();

            // below text drawable is a circular.
            TextDrawable drawable2 = TextDrawable.builder().beginConfig()
                    .width(100)  // width in px
                    .height(100) // height in px
                    .endConfig()
                    .buildRound(modal.getContacytName().substring(0, 1), color);
            // setting image to our image view on below line.
            holder.ivcontact.setImageDrawable(drawable2);
            String number = modal.getPhoneNumber();
            holder.tvname.setText(modal.getContacytName());
            holder.tvnumber.setText(number);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GiftModuleActivity.class);
                    intent.putExtra("phone_number", number);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listfilter.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout layout;
            TextView tvname, tvnumber;
            ImageView ivcontact;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.rllayout);
                tvnumber = itemView.findViewById(R.id.tvnumber);
                tvname = itemView.findViewById(R.id.tvcontactname);
                ivcontact = itemView.findViewById(R.id.ivcontactimg);
            }
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        listfilter = list;
                    } else {
                        ArrayList<ContactsModal> filteredList = new ArrayList<>();
                        for (ContactsModal row : list) {


                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getContacytName().toLowerCase().contains(charString.toLowerCase()) || row.getPhoneNumber().contains(charSequence)) {
                                filteredList.add(row);
                            }
                        }

                        listfilter = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listfilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    listfilter = (ArrayList<ContactsModal>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}
