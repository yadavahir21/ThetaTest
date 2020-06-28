package com.raghu.thetapracticle.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.raghu.thetapracticle.model.Data;

import java.util.List;

@Entity(tableName = "data_table")
public class DataTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dataId")
    public int dataId;

    @ColumnInfo(name = "dataList")
    public List<Data> dataList;
}
