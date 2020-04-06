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
import ru.jmirazors.jmiСalculator.entity.Group;

/**
 *
 * @author User
 */
public class GroupDAO extends DAO {
  
//    public Group create(Group group) {
//        try {
//            begin();
//            getSession().save(group);
//            commit();
//        } catch (HibernateException ex) {
//            rollback();
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, 
//                    "Ошибка сохранения группы \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            close();
//        }
//        return group;
//    }
    public Group update(Group group) {
        try {
            begin();
            getSession().saveOrUpdate(group);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения группы \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return group;
    }
    public void delete(Group group) {
        try{
            begin();
            getSession().delete(group);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка удаления группы \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
    }
    public Group getById(long id) {
        Group group = null;
        try {
            begin();
            group = (Group)getSession().get(Group.class, id);
            commit();
        } catch (HibernateException ex) {            
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска группы \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return group;
    }
    
    
    public Group find(String name) {
        Group group = null;
        try{
            begin();
            Query query = getSession().createQuery("From Group Where name= :name");
            query.setParameter("name", name);
            group = (Group)query.uniqueResult();
            commit();
            return group;
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска группы \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return group;
    }
    
    public List<Group> list() {
        List<Group> groups = new ArrayList<>();
        try {
            begin();
            groups = getSession().createQuery("From Group", Group.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка получения списка групп. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return groups;
    }
    public List<Group> list(String options) {
        List<Group> groups = new ArrayList<>();
        try {
            begin();
            groups = getSession().createQuery(options, Group.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка получения списка групп. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return groups;
    }
}
