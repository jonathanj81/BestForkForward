package com.example.jon.bestforkforward.DataHandling;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jon.bestforkforward.NetworkUtils.RetrofitFetcher;

import java.util.concurrent.Executors;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao mDao();

    private static RecipeDatabase mDB;
    private static Context mContext;

    public synchronized static RecipeDatabase getDatabase(Context context) {
        Log.i("AHDFJGHJADFJHJDFH", "Database checked get");
        mContext = context;
        if (mDB == null) {
            mDB = buildDatabase(context);
            mDB.populateData();
        }
        return mDB;
    }

    private static RecipeDatabase buildDatabase(final Context context) {

        Log.i("AHDFJGHJADFJHJDFH", "Database building");

        RecipeDatabase tempDB = Room.databaseBuilder(context, RecipeDatabase.class, "recipes")
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        /*Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("AHDFJGHJADFJHJDFH", "Database running");
                                RetrofitFetcher.insertToDB(context);
                            }
                        }); */
                    }
                }).build();
        return tempDB;
    }

    private void populateData() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Log.i("AHDFJGHJADFJHJDFH", "Database running");
                if (mDao().count() == 0) {
                    RetrofitFetcher.insertToDB(mContext);
                }
            }
        });
    }
}
