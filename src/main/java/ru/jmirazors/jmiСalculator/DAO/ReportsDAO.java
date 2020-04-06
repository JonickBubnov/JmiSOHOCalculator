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
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Sklad;
import ru.jmirazors.jmiСalculator.entity.Stock;

/**
 *
 * @author User
 */
public class ReportsDAO extends DAO {
    
    public List<Object> list(Class docType) {
        List<Object> objs = new ArrayList<>();
        try {
            begin();
            objs = getSession().createQuery("FROM "+docType.getSimpleName(), docType).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return objs;
    }   
    
    public List<Product> getPriceList() {
        List<Product> products = new ArrayList<>();
        try {
            begin();
            products = getSession().createQuery("FROM Product", Product.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return products;
        
    }
    
    public List<Stock> getStockReport() {
        List<Stock> obj = new ArrayList();
        begin();
        Query hql = getSession().createQuery("FROM Stock ORDER BY storage_id", Stock.class);
        //q.setParameter("id", 1);
        obj = hql.getResultList();
        commit();
        return obj;
    }
    
    public List<Sklad> getSkladReport() {
        List<Sklad> obj = new ArrayList();
        begin();
        Query hql = getSession().createQuery("FROM Sklad ORDER BY indate", Sklad.class);
        //q.setParameter("id", 1);
        obj = hql.getResultList();
        commit();
        return obj;        
    }
    
}
