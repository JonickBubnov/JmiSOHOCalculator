/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import ru.jmirazors.jmiСalculator.entity.Sklad;

/**
 *
 * @author User
 */
public class SkladDAO extends DAO {
    
    public Sklad updateSklad(Sklad sklad) {

        try {
            begin();
            getSession().save(sklad);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения склада \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return sklad;
        
    }    
    
}
