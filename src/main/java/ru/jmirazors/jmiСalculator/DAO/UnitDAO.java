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
import ru.jmirazors.jmiСalculator.entity.Unit;

/**
 *
 * @author User
 */
public class UnitDAO extends DAO {

    public List<Unit> list(String options) {
        List<Unit> units = new ArrayList<>();
        try {
            begin();
            units = getSession().createQuery(options, Unit.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка получения списка единиц измерения. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return units;
    }

    public void save(List<Unit> unitList) {
        try {
            begin();
            for(Unit ul : unitList) {
                getSession().saveOrUpdate(ul);
            }
            getSession().flush();
            getSession().clear();        
            commit(); 
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения единиц измерения. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
//        finally {
//            close();
//        }
    }    
    
}
