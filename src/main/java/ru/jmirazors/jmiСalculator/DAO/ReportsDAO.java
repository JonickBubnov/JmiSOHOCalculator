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
import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.Group;
import ru.jmirazors.jmiСalculator.entity.Kassa;
import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Sklad;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */
public class ReportsDAO extends DAO {
    
    // ******************  отчет 
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
    
    // **************  Отчет продажи по документам *****************************
    public List<Kassa> getKassa(List<Contragent> sel) {
        List<Kassa> kassa = new ArrayList<>();
        try {
            begin();
            Query query = getSession().createQuery("FROM Kassa WHERE contragent = :selC", Kassa.class);
                query.setParameter("selC", sel);
            kassa = query.getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return kassa;        
    }    
    // *************************************************************************
    // ********   отчет Прайс Лист *********************************************
    public List<Product> getPriceList(List<Group> sel) {
        List<Product> products = new ArrayList<>();
        try {
            begin();
            Query query = getSession().createQuery("FROM Product WHERE parent IN :selG", Product.class);
                query.setParameter("selG", sel);
            products = query.getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return products;        
    }
    // *************************************************************************
    // *****************  остатки на складах  **********************************
    public List<Stock> getStockReport(List<Group> grl, List<Storage> stl) {
        List<Stock> stock = new ArrayList<>();
        try {
            begin();
            Query query = getSession().createQuery("FROM Stock WHERE product.group IN :selG AND storage IN :selSt ORDER BY storage", Stock.class);
                query.setParameter("selG", grl);
                query.setParameter("selSt", stl);
            stock = query.getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return stock;
    }
    // *************************************************************************
    
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
