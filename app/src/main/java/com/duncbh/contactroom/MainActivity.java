package com.duncbh.contactroom;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.duncbh.contactroom.model.Contact;
import com.duncbh.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            StringBuilder builder = new StringBuilder();
            for (Contact contact : contacts) {
                Log.d("TAG", "onCreate: " + contact.getName());
                builder.append(" - ").append(contact.getName()).append(" ").append(contact.getOccupation());
            }
            textView.setText(builder.toString());
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
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();

                        String name = data.getStringExtra(NewContact.NAME_REPLY);
                        String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

                        Contact contact = new Contact(name, occupation);

                        ContactViewModel.insert(contact);//TODO: pick up from #68
                    }
                }
            }
    );
}