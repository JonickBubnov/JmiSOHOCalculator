/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;


import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */
public class StockDAO extends DAO {
    
    
    public Stock findStock(Storage storage, Product product) {
        Stock stock = null;
        try {
            begin();
            Query query = getSession().createQuery("FROM Stock WHERE storage_id= :sid AND product_id= :pid");
            query.setParameter("sid", storage.getId());
            query.setParameter("pid", product.getId());
            stock = (Stock)query.uniqueResult();
            commit();         
         
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "[StockDAO]\nОшибка получения места хранения продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
        return stock;
    }
    
    public Stock updateStock(Stock stock) {

        try {
            begin();
            getSession().saveOrUpdate(stock);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "[StockDAO]\nОшибка сохранения места места хранения продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return stock;
        
    }
    
    public float getStockTotal(long id) {
        float total = 0;
        try {
            begin();
            Query query = getSession().createQuery("SELECT SUM(count) FROM Stock WHERE product_id="+id);
            total = (float)query.uniqueResult();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "[StockDAO]\nОшибка получить количество. \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);            
        } 

        return total;
    }
    

}
