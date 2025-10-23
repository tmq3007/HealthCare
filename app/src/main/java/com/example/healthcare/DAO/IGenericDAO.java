package com.example.healthcare.DAO;


import android.database.Cursor;

import com.example.healthcare.DAO.SQLite.DatabaseHelper;

import java.util.List;

public interface IGenericDAO<T> {
    boolean createTable();
    IGenericDAO<T>  setHelper(DatabaseHelper helper);
    void deleteTable();

    boolean save(T object);

    boolean update(T object);

    boolean delete(int id);

    T get(int id);

    List<T> getAll();

    T fromCursor(Cursor cursor);
}
