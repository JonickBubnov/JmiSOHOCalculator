/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import ru.jmirazors.jmiСalculator.entity.Privileges;
import ru.jmirazors.jmiСalculator.entity.User;

/**
 *
 * @author User
 */
public class UserDAO extends DAO {
    
    
    public User createUser(User user) throws SQLException{
        try {
            System.out.println("create user"); //DEB
            begin();
            getSession().saveOrUpdate(user);
            commit();            
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка создания пользователя", "Ошика", JOptionPane.ERROR_MESSAGE);           } 
//        } finally {
//            close();
//        }
        return user;
    }    
    
    public List<Privileges> listPrivileges() throws SQLException {
        List<Privileges> priv = new ArrayList<>();
        try {
            System.out.println("list privileges"); //DEB
            begin();
            priv = getSession().createQuery("from Privileges", Privileges.class).getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения привилегий пользователей", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
//        finally {
//            close();
//        }
        return priv;
    }
    
    public List<User> list(String options) throws SQLException {
        List<User> users = new ArrayList<>();          
        try {
            begin();            
            users = getSession().createQuery(options, User.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Не могу получить список пользователей.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return users;
    }  
    
    public User getById(String id) {
            User user = null;
        try {
            begin();
            user = (User)getSession().get(User.class, Long.valueOf(id));
            commit();
            //return contragent;
        } catch (HibernateException ex) {            
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return user;
    }            
    
    public User findUser(String name) throws SQLException {
        User user =null;      
        try{
            begin();
            Query query = getSession().createQuery("FROM User WHERE name= :name");
            query.setParameter("name", name);
            //user = (User)criteria.uniqueResult();
            user = (User)query.uniqueResult();            
            commit();           
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска пользователя <"+name+">", "Ошибка", JOptionPane.ERROR_MESSAGE);             
        } 
//        finally {            
//            close();
//        }
        return user;
    }    
    
}
