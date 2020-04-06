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
import ru.jmirazors.jmiСalculator.entity.Price;
import ru.jmirazors.jmiСalculator.entity.PriceName;

/**
 *
 * @author User
 */
public class PriceDAO extends DAO {
    
    
    public List<Price> getActualPriceList(long id) {
        
        List<Price> actualPriceList = new ArrayList<>();
        try {
            begin();            
            Query query = getSession().createQuery("FROM Price "
                    + "WHERE indate IN (SELECT MAX(indate) FROM Price WHERE product_id = :pid GROUP BY pricename_id) "
                    + "AND product_id = :pid GROUP BY pricename_id");            
            query.setParameter("pid", id);
            actualPriceList = query.list();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                "Ошибка поиска актуальных цен \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return actualPriceList;
    }
    
    public Price getActualPrice(long productid, PriceName priceid) {
        
        Price price = null;
       
        begin();        
        Query query = getSession().createQuery("FROM Price WHERE product_id = :pr AND pricename_id = :pn "
                +"AND indate = (SELECT MAX(indate) FROM Price WHERE product_id= :pr AND pricename_id= :pn)");
        query.setParameter("pr", productid);
        query.setParameter("pn", priceid.getId());
        price = (Price)query.uniqueResult();
        commit(); 
        if (price == null) {
            price = new Price();
            price.setPrice(0);
            price.setPricename(priceid);
        }
        return price;
    }
    
    public Price create(Price price) {
        try {
            begin();
            getSession().save(price);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения цены \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return price;
    }
    
    
}
