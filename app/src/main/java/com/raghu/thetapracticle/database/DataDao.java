package com.raghu.thetapracticle.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.raghu.thetapracticle.model.Data;

import java.util.List;

@Dao
public interface DataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertData(DataTable... dataTables);

    @Query("SELECT * FROM data_table LIMIT 1")
    DataTable findDataTable();

    @Query("DELETE FROM data_table")
    void deleteAllData();

    @Query("UPDATE data_table SET dataList =:dataList WHERE dataId =:dataId")
    void updateData(List<Data> dataList, int dataId);

    @Query("SELECT * FROM data_table")
    LiveData<DataTable> getDataList();
}
