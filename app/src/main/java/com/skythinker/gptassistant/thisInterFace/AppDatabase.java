package com.skythinker.gptassistant.thisInterFace;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.skythinker.gptassistant.entity.base.HistoryEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.entity.user.User;

@Database(entities = {TextTemplate.class, HistoryEntity.class, User.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoInterFace qrCodeInfoDao();
    private static Context context;

    private static volatile AppDatabase INSTANCE;
    public static void initAppDatabase(Context context){
        AppDatabase.context = context;
    }

    public static AppDatabase getInstance() {
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