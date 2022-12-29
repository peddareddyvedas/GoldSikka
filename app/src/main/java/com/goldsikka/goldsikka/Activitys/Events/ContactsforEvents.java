package com.goldsikka.goldsikka.Activitys.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactRVAdapter;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactsModal;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsforEvents extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ContactsModal> contactsModalArrayList;
    RecyclerView contactRV;
    MultiContactsAdapter adapter;
    Cursor cursor;
    Button btcontacts;
    String stcontactslist, steventid;
    ApiDao apiDao;

    RelativeLayout backbtn;


    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactsfor_events);
        titleTv = findViewById(R.id.title);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Event");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prepareContactRV();
        requestPermissions();
        searchview();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            steventid = bundle.getString("eventid");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void searchview() {

        SearchView searchView = (SearchView) findViewById(R.id.searchview);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                filter(query.toLowerCase());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase());
                return false;
            }
        });
    }

    private void filter(String text) {
        // in this method we are filtering our array list.
        // on below line we are creating a new filtered array list.
        ArrayList<ContactsModal> filteredlist = new ArrayList<>();
        // on below line we are running a loop for checking if the item is present in array list.
        for (ContactsModal item : contactsModalArrayList) {
            if (item.getContacytName().toLowerCase().contains(text.toLowerCase())) {
                // on below line we are adding item to our filtered array list.
                filteredlist.add(item);

            }
        }
        // on below line we are checking if the filtered list is empty or not.
        if (filteredlist.isEmpty()) {
            //Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
        } else {
            // passing this filtered list to our adapter with filter list method.
            adapter.filterList(filteredlist);
        }
    }

    private void prepareContactRV() {
        contactRV = findViewById(R.id.idRVContacts);
        // in this method we are preparing our recycler view with adapter.
        contactsModalArrayList = new ArrayList<>();
        adapter = new MultiContactsAdapter(this, contactsModalArrayList);
        // on below line we are setting layout manager.
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        // on below line we are setting adapter to our recycler view.
        contactRV.setAdapter(adapter);
        btcontacts = findViewById(R.id.btcontacts);
        btcontacts.setOnClickListener(this);
    }

    private void requestPermissions() {

        if (ContextCompat.checkSelfPermission(ContactsforEvents.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactsforEvents.this,
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


            Toast.makeText(this, "Permission denied ", Toast.LENGTH_SHORT).show();
            onBackPressed();
            //  requestPermissions();
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
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btcontacts) {
            // contactsModalArrayList.clear();
            if (adapter.getSelected().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < adapter.getSelected().size(); i++) {
                    stringBuilder.append(adapter.getSelected().get(i).getPhoneNumber());
                    stringBuilder.append(",");
                }
                stcontactslist = stringBuilder.toString().trim();
                //    Toast.makeText(this, stcontactslist, Toast.LENGTH_SHORT).show();
                sendcontacts();

            } else {
                ToastMessage.onToast(this, "No Contacts selected", ToastMessage.ERROR);
            }
        }
    }

    public void sendcontacts() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<ContactsModal> sendcontacts = apiDao.sendcontacts("Bearer " + AccountUtils.getAccessToken(this), steventid, stcontactslist);
            sendcontacts.enqueue(new Callback<ContactsModal>() {
                @Override
                public void onResponse(Call<ContactsModal> call, Response<ContactsModal> response) {
                    int statuscode = response.code();
                    ContactsModal modal = response.body();
                    Log.e("statuscode", String.valueOf(statuscode));

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        ToastMessage.onToast(ContactsforEvents.this, "Events Shared Successfully", ToastMessage.SUCCESS);
                        Intent intent = new Intent(ContactsforEvents.this, Eventlist.class);
                        startActivity(intent);


                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(ContactsforEvents.this, "We have some issues try after some time", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ContactsModal> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("contasrt o nfail", t.toString());
                    ToastMessage.onToast(ContactsforEvents.this, "Events Shared Successfully", ToastMessage.SUCCESS);
                    Intent intent = new Intent(ContactsforEvents.this, Eventlist.class);
                    startActivity(intent);
//                    ToastMessage.onToast(ContactsforEvents.this,"We have some issues try after some time",ToastMessage.ERROR);
                }
            });
        }
    }


//    *** Adapter Class


    public class MultiContactsAdapter extends RecyclerView.Adapter<MultiContactsAdapter.ViewHolder> {
        // implements Filterable

        // creating variables for context and array list.
        private Context context;
        private ArrayList<ContactsModal> contactsModalArrayList;
        ArrayList<ContactsModal> listfilter;

        // creating a constructor
        public MultiContactsAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
            this.context = context;
            this.contactsModalArrayList = contactsModalArrayList;
            this.listfilter = contactsModalArrayList;
        }

        @NonNull
        @Override
        public MultiContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // passing our layout file for displaying our card item
            return new MultiContactsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false));

        }

        // below method is use for filtering data in our array list
        public void filterList(ArrayList<ContactsModal> filterllist) {
            // on below line we are passing filtered
            // array list in our original array list
            contactsModalArrayList = filterllist;
            // getSelected();
            notifyDataSetChanged();
            // getFilter();

        }

        @Override
        public void onBindViewHolder(@NonNull MultiContactsAdapter.ViewHolder holder, int position) {
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

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });

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
            private ImageView ivcontactimg, imageView;
            RelativeLayout rllayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing our image view and text view.
                tvnumber = itemView.findViewById(R.id.tvnumber);
                tvcontactname = itemView.findViewById(R.id.tvcontactname);
                ivcontactimg = itemView.findViewById(R.id.ivcontactimg);
                imageView = itemView.findViewById(R.id.imageView);
                rllayout = itemView.findViewById(R.id.rllayout);
            }


            void bind(final ContactsModal contactsModal) {

                imageView.setVisibility(contactsModal.isChecked() ? View.VISIBLE : View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contactsModal.setChecked(!contactsModal.isChecked());
                        imageView.setVisibility(contactsModal.isChecked() ? View.VISIBLE : View.GONE);
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

}