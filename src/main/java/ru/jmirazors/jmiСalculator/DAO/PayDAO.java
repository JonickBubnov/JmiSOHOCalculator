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
import ru.jmirazors.jmiСalculator.entity.Pay;
import ru.jmirazors.jmiСalculator.entity.PayType;

/**
 *
 * @author User
 */
public class PayDAO extends DAO {
    
    public List<PayType> payTypeList() throws Exception {
        List<PayType> documents = new ArrayList<>();
        try {
            begin();
            documents = getSession().createQuery("FROM PayType").getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения списка Документов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return documents;
    }
    public PayType getPayType(String name) {
        PayType payType = null;
        try{
            begin();
            Query query = getSession().createQuery("From PayType where name= :name");
            query.setParameter("name", name);
            payType = (PayType)query.uniqueResult();
            commit();           
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска типа операции \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
         return payType;
    }        
        
    public Pay updatePay(Pay pay) {
        try {
            begin();
            getSession().saveOrUpdate(pay);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка создания оплаты \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);          
        } 
        return pay;
    }    
    
}
