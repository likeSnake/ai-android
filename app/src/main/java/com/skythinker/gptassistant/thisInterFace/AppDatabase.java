package com.skythinker.gptassistant.thisInterFace;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;

@Database(entities = {TextTemplate.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoInterFace qrCodeInfoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ai_template")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}