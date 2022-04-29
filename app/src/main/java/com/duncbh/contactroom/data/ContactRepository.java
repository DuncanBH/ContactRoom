package com.duncbh.contactroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.duncbh.contactroom.model.Contact;
import com.duncbh.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData() {
        return allContacts;
    }

    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.insert(contact));
    }

    public LiveData<Contact> get(int id) {
        return contactDao.get(id);
    }

    public void update(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.update(contact));
    }

    public void delete(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }

}
