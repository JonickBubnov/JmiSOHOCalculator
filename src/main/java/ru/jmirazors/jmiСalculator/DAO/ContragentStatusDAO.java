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
import ru.jmirazors.jmiСalculator.entity.ContragentStatus;

/**
 *
 * @author User
 */
public class ContragentStatusDAO extends DAO {
    
        public ContragentStatus getStatusById(long id) {
            ContragentStatus status = null;
        try {
            begin();
            status = (ContragentStatus)getSession().get(ContragentStatus.class, id);
            commit();
            //return status;
        } catch (HibernateException ex) {            
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска статуса контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return status;
    }  
    public ContragentStatus getStatusByName(String name) {
        ContragentStatus status = null;
        try{
            begin();
            Query query = getSession().createQuery("From ContragentStatus where name= :name");
            query.setParameter("name", name);
            status = (ContragentStatus)query.uniqueResult();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска статуса контрагента", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return status;        
    }
    
    public List<ContragentStatus> list() {
        List<ContragentStatus> status = new ArrayList<>();
        try {
            begin();
            status = getSession().createQuery("From ContragentStatus", ContragentStatus.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка извлечения списка статуса контрагентов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return status;
    }     
    
}
