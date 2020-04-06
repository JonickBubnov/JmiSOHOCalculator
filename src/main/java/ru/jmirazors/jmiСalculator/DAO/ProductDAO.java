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


/**
 *
 * @author User
 */
public class ProductDAO extends DAO{
    
//    public Product createProduct(Product product)  {
//        
//        try {
//            begin();
//            getSession().saveOrUpdate(product);
//            commit();
//        } catch (HibernateException ex) {
//            rollback();
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, 
//                    "Ошибка создания пользователя \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);          
//        } finally {
//            close();
//        }
//        return product;
//    }
    public Product updateProduct(Product product) {
        try {
            begin();
            getSession().saveOrUpdate(product);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка создания Продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);          
        } 
//        finally {
//            close();
//        }
        return product;
    }
    public void saveAll(List<Product> products) {
        try {
            begin();
            for (int i = 0; i < products.size(); i++)
                getSession().update(products.get(i));
            commit(); 
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения списка Продуктов \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
    }   
    public void deleteProduct(Product product) {
        try{
            begin();
            getSession().delete(product);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка удаления Продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
    }
    
    public Product findProduct(String name) {
        Product product = null;
        try{
            begin();
            Query query = getSession().createQuery("FROM Product pr WHERE pr.name = :name");
            query.setParameter("name", name);
            product = (Product)query.uniqueResult();
            commit();           
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска Продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
         return product;
    }
    public Product getProduct(String id) {
        Product product = null;
        try {
            begin();
            product = (Product)getSession().get(Product.class, Long.valueOf(id));
            commit();
        }catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска Продукта \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return product;
        
    }
    
    public List<Product> list() {
        List<Product> products = new ArrayList<>();
        try {
            begin();
            products = getSession().createQuery("From Product pr", Product.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка получения списка Продуктов \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return products;
    }
        
    public List<Product> list(String options) {
        List<Product> products = new ArrayList<>();
        try {
            begin();
            products = getSession().createQuery(options, Product.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка получения списка Продуктов \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return products;
    }

}
