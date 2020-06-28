package com.raghu.thetapracticle.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.raghu.thetapracticle.model.Data;

import java.util.List;

@Entity(tableName = "user_table")
public class UserTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "emailID")
    public String emailID;

    @ColumnInfo(name = "photoPath")
    public String photoPath;
}
