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
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */
public class StorageDAO extends DAO{
    
    
     public Storage createStorage(Storage storage) throws Exception{
        try {
            begin();
            getSession().save(storage);
            commit();
            return storage;
        } catch (HibernateException ex) {
            rollback();
            throw new Exception("Could't create storage name: " + storage.getName(), ex);
        }
    }
    public Storage updateStorage(Storage storage) throws Exception {
        try {
            begin();
            getSession().update(storage);
            commit();
            return storage;
        } catch (HibernateException ex) {
            rollback();
            throw new Exception("Could't update storage");
        }
    }
    public void deleteStorage(Storage storage) throws Exception{
        try{
            begin();
            getSession().delete(storage);
            commit();
        } catch (HibernateException ex) {
            rollback();
            throw new Exception("Could't delete storage " + storage.getName(), ex);
        }
    }
    public Storage getStorage(String id) throws Exception {
        try {
            begin();
            Storage storage = (Storage)getSession().get(Storage.class, Long.valueOf(id));
            commit();
            return storage;
        } catch (HibernateException ex) {            
            rollback();
            throw new Exception("No results whis id="+id, ex);
        }
    }
    
    
    public Storage findStorage(String name) throws Exception {
        Storage storage = null;
        try{
            begin();
            Query query = getSession().createQuery("FROM Storage WHERE name= :name AND del=1");
            query.setParameter("name", name);
            storage = (Storage)query.uniqueResult();            
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                "Ошибка поиска склада. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        }
        return storage;
    }
    
    public List<Storage> list() {
        List<Storage> storages = new ArrayList<>();
        try {
            begin();
            storages = getSession().createQuery("FROM Storage", Storage.class).getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[StorageDAO] \nОшибка чтения списка Складов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return storages;
    }
    
    public List<Storage> list(char option) throws Exception {
        List<Storage> storages =  null;
        try {
            begin();
            Query query = getSession().createQuery("FROM Storage WHERE del = 1", Storage.class);
//            switch (option) {
//                case 'a':
//                    query.setParameter("op", "WHERE del <> 3");
//                    break;
//                case 'e':
//                    query.setParameter("op", "WHERE del = 1");
//                    break;
//            }

            storages = query.getResultList();
            commit();
            return storages;
        } catch (HibernateException ex) {
            rollback();
            throw new Exception ("Error!", ex);
        }
    }
}
