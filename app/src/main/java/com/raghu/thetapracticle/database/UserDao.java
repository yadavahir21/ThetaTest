package com.raghu.thetapracticle.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.raghu.thetapracticle.model.Data;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(UserTable userTables);

    @Query("SELECT * FROM user_table WHERE emailID=:emailID LIMIT 1")
    UserTable findEmail(String emailID);

    @Query("SELECT * FROM user_table WHERE emailID =:emailID AND password=:password")
    UserTable loginValidation(String emailID, String password);

    @Query("UPDATE user_table SET username =:username, password=:password,emailID =:emailID,photoPath=:photoPath WHERE userID =:userID")
    void updateUser(String username, String password, String emailID, String photoPath, int userID);

    @Query("DELETE FROM user_table")
    void deleteAllUser();
}
