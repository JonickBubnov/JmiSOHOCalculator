/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import org.hibernate.query.Query;

/**
 *
 * @author User
 */
public class DBManagerDAO extends DAO {
    
    public boolean createDB(String dbname) {
        Query query;
        begin();
            getSession().createQuery("CREATE SCHEMA IF NOT EXISTS "+dbname).executeUpdate();                    
        commit();
        return true;
    }
}
