package com.duncbh.contactroom.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.duncbh.contactroom.data.ContactDao;
import com.duncbh.contactroom.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = (Contact.class), version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;

    public abstract ContactDao contactDao();

    private static volatile ContactRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                public void OnCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(); //TODO: start back up on video 64
                    databaseWriteExecutor.execute(() -> {
                        ContactDao contactDao = INSTANCE.contactDao();
                        contactDao.deleteAll();

                        Contact contact = new Contact("Jon", "Teacher");
                        contactDao.insert(contact);

                        contact = new Contact("Bond", "Spy");
                        contactDao.insert(contact);

                    });
                }
            };

    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class, "contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
