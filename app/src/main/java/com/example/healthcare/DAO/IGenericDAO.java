package com.example.healthcare.DAO;


import com.example.healthcare.DAO.SQLite.DatabaseHelper;

public interface IGenericDAO {
    boolean createTable();
    IGenericDAO setHelper(DatabaseHelper helper);
    void deleteTable();
}
