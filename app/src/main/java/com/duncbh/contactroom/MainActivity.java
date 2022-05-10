package com.duncbh.contactroom;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.duncbh.contactroom.adapter.RecyclerViewAdapter;
import com.duncbh.contactroom.data.AgeAsyncResponse;
import com.duncbh.contactroom.data.AgeGenderRetriever;
import com.duncbh.contactroom.data.GenderAsyncResponse;
import com.duncbh.contactroom.model.Contact;
import com.duncbh.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    private static final String TAG = "clicked";
    public static final String CONTACT_ID = "contact_id";
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this);
            recyclerView.setAdapter(recyclerViewAdapter);

        });

        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);

            newContactResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> newContactResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data.hasExtra(NewContact.SNACKBAR_TEXT)) {
                        //Log.d("TAG", String.valueOf(data.getIntExtra(NewContact.SNACKBAR_TEXT, 0)));

                        Snackbar.make(recyclerView, data.getIntExtra(NewContact.SNACKBAR_TEXT, 0), BaseTransientBottomBar.LENGTH_SHORT)
                                .setAction("Show Image", view -> {
                                    Intent intent = new Intent(MainActivity.this, ShowImage.class);
                                    startActivity(intent);
                                })
                                .show();
                    }
                    if (data.hasExtra(NewContact.NAME_REPLY) && data.hasExtra(NewContact.OCCUPATION_REPLY)) {

                        String name = data.getStringExtra(NewContact.NAME_REPLY);
                        String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

                        Contact contact = new Contact(name, occupation);

                        AgeGenderRetriever ageGenderRetriever = new AgeGenderRetriever();

                        //Get first name if multiple names given
                        String firstName = contact.getName().split(" ")[0];

                        //Get age estimate from api
                        ageGenderRetriever.getAge(new AgeAsyncResponse() {
                            @Override
                            public void processFinished(int result) {
                                contact.setAge(result);
                            }

                            //On success callback: get age from 2nd api
                            @Override
                            public void onSuccess() {
                                ageGenderRetriever.getGender(gender -> {
                                    contact.setGender(gender);
                                    //Save new contact
                                    ContactViewModel.insert(contact);
                                }, firstName);
                            }
                        }, firstName);
                    }
                }
            }
    );

    @Override
    public void onContactClick(int position) {
        Contact contact = contactViewModel.allContacts.getValue().get(position);
        //Log.d(TAG, "onContactClick: " + contact.getName());

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        newContactResultLauncher.launch(intent);
    }
}