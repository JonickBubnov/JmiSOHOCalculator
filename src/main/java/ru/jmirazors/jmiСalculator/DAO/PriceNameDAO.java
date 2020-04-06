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
import ru.jmirazors.jmiСalculator.entity.PriceName;

/**
 *
 * @author User
 */
public class PriceNameDAO extends DAO {
    
    public List<PriceName> list(String options) {
        List<PriceName> priceNames = new ArrayList<>();
        try {
            begin();
            priceNames = getSession().createQuery(options, PriceName.class).getResultList();
            commit();
            return priceNames;
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[PricenameDAO] \nОшибка почения списка типов цен. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return priceNames;
    } 
    
    public PriceName findPriceName(String name) {
        PriceName priceName = null;
        try{
            begin();
            Query query = getSession().createQuery("FROM PriceName WHERE name= :name");
            query.setParameter("name", name);
            priceName = (PriceName)query.uniqueResult();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[PricenameDAO] \nОшибка поиска типа цены. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        }
        return priceName;
    }
    
    public PriceName getPriceName(long id) throws Exception {
        PriceName priceName = null;
        try {
            begin();
            priceName = (PriceName) getSession().getReference(PriceName.class, id);
            commit();
            return priceName;
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[PricenameDAO] \nОшибка поиска типа цены. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return priceName;
    }
    
    public void save(List<PriceName> priceNameList) {
        try {
            begin();
            for(PriceName pn : priceNameList) {
                getSession().saveOrUpdate(pn);
            }       
            commit(); 
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[PricenameDAO] \nОшибка сохранения типов цен. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);            
        } 
    }
}
