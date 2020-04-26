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
import ru.jmirazors.jmiСalculator.entity.Log;
import ru.jmirazors.jmiCalculator.MainFrame;

/**
 *
 * @author User
 */
public class LogDAO extends DAO {
    public void writeLog(Log log) {
        try {
            begin();
            getSession().save(log);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            MainFrame.infoPanel.append("\n Не удалось записать лог.");
        }    
    }
    public List<Log> list() {
        List<Log> log = new ArrayList<>();
        try {
            begin();
            log = getSession().createQuery("FROM Log", Log.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка извлечения списка журнал событий", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
//        finally {
//            close();
//        }
        return log;        
    }
}
