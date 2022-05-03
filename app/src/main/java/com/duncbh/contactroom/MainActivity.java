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
import com.duncbh.contactroom.model.Contact;
import com.duncbh.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

//            StringBuilder builder = new StringBuilder();
//            for (Contact contact : contacts) {
//                Log.d("TAG", "onCreate: " + contact.getName());
//                builder.append(" - ").append(contact.getName()).append(" ").append(contact.getOccupation());
//            }
        });

        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);

            newContactResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> newContactResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        String name = data.getStringExtra(NewContact.NAME_REPLY);
                        String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

                        Contact contact = new Contact(name, occupation);

                        ContactViewModel.insert(contact);
                    }
                }
            }
    );

    @Override
    public void onContactClick(int position) {
        Contact contact = contactViewModel.allContacts.getValue().get(position);
        Log.d(TAG, "onContactClick: " + contact.getName());
        //startActivity(new Intent(MainActivity.this, NewContact.class));

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }
}