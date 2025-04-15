package com.skythinker.gptassistant.thisInterFace;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skythinker.gptassistant.entity.base.HistoryEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.entity.user.User;

import java.util.List;

@Dao
public interface DaoInterFace {
    @Insert
    void insert(TextTemplate textTemplate);

    @Query("SELECT * FROM text_template")
    List<TextTemplate> getAll();

    @Query("SELECT * FROM text_template WHERE id = :id")
    TextTemplate getById(int id);

    @Query("DELETE FROM text_template WHERE id = :id")
    void deleteById(int id);

    @Insert
    void insertHistory(HistoryEntity historyEntity);

    @Query("SELECT * FROM history_entity")
    List<HistoryEntity> getAllHistory();

    @Query("SELECT * FROM history_entity WHERE id = :id")
    HistoryEntity getHistoryById(int id);

    @Query("DELETE FROM history_entity WHERE id = :id")
    void deleteHistoryById(int id);

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUser();

    @Query("SELECT * FROM user LIMIT 1")
    User getFirstUser();

    @Query("SELECT * FROM user WHERE id = :id")
    User getUserById(int id);

    @Query("DELETE FROM user WHERE id = :id")
    void deleteUserById(int id);

    @Update
    void update(User user);
    // 删除所有用户
    @Query("DELETE FROM user")
    void deleteAllUsers();

}
