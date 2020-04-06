/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import ru.jmirazors.jmiСalculator.entity.Contacts;
import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.ContragentStatus;

/**
 *
 * @author User
 */
public class ContragentDAO extends DAO {
    
     public Contragent saveContragent(Contragent contragent) {
        try {
            begin();
            getSession().saveOrUpdate(contragent);
            getSession().flush();
            getSession().clear();            
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка создания контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return contragent;
    }
        public Contragent getById(String id) {
            Contragent contragent = null;
        try {
            begin();
            contragent = (Contragent)getSession().get(Contragent.class, Long.valueOf(id));
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
        return contragent;
    }        
     
    public void deleteContragent(Contragent contragent) {
        try{
            begin();
            getSession().delete(contragent);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка удаления контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
    }
    
    public Contragent findContragent(String name) {
        Contragent contragent = null;
        try{
            begin();
            Query query = getSession().createQuery("From Contragent where name= :name");
            query.setParameter("name", name);
            contragent = (Contragent)query.uniqueResult();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка удаления контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return contragent;
    }
    
    public List<Contragent> list() {
        List<Contragent> contragents = new ArrayList<>();
        try {
            begin();
            contragents = getSession().createQuery("FROM Contragent", Contragent.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка извлечения списка контрагентов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return contragents;
    } 
    public void saveContact(Contacts contact) {
        try {
        begin();
        getSession().save(contact);
        getSession().flush();
        getSession().clear();            
        commit();        
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения контакта", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        }
    }
      
}
