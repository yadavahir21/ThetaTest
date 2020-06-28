package com.raghu.thetapracticle.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.raghu.thetapracticle.extra.DataConverter;

@androidx.room.Database(entities = {DataTable.class,UserTable.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    private static final String DB_NAME = "Database.db";

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new PopulateDbAsync(INSTANCE).execute();
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DataDao dataDao();
    public abstract UserDao userDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final DataDao dataDao;
        private final UserDao userDao;

        public PopulateDbAsync(Database instance) {
            dataDao = instance.dataDao();
            userDao = instance.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataDao.deleteAllData();
            userDao.deleteAllUser();
            return null;
        }
    }
}

