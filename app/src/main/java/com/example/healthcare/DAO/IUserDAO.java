package com.example.healthcare.DAO;

import android.content.Context;

import com.example.healthcare.Model.User;

public interface IUserDAO extends IGenericDAO<User> {
     void register(String username, String email, String password );
     User login(String username, String password);
     User getUserByUsername(String username);
     User getCurrentUser(Context context);

     int getCurrentUserId(Context context);

     void logout(Context context);

}
