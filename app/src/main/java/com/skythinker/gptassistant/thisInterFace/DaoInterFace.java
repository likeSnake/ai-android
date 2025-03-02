package com.skythinker.gptassistant.thisInterFace;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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
}
