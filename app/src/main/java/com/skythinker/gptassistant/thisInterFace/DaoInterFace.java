package com.skythinker.gptassistant.thisInterFace;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.skythinker.gptassistant.entity.base.HistoryEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;

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
}
