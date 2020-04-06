/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.jmirazors.jmiСalculator.entity.Organization;

/**
 *
 * @author User
 */
public class OrganizationDAO extends DAO {
    Session session;
    Transaction tx = null;
    
    public List<Organization> list(String options) throws SQLException {
        List<Organization> organizations = new ArrayList<>();
        try {
            begin();            
            organizations = getSession().createQuery(options, Organization.class).getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения списка организаций", "Ошибка", JOptionPane.ERROR_MESSAGE);                        
        } 
        return organizations;
    }  
    public Organization findOrganization(String name) throws SQLException {
        Organization organization = null;
        try{
            begin();            
            Query query = getSession().createQuery("From Organization where name= :name");
            query.setParameter("name", name);
            organization = (Organization)query.uniqueResult();
            commit();            
        } catch (HibernateException ex) {
            rollback();            
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска организации <"+name+">", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
        return organization;
    }    
    public Organization getOrganization(String id) throws SQLException {
        Organization organization = null;
        try{
            begin();            
            long i = Long.valueOf(id);
            organization = getSession().get(Organization.class, i);
            commit();            
        } catch (HibernateException ex) {
            rollback();            
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска организации <"+id+">", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
        return organization;
    }    
    public Organization updateOrganization(Organization org) {
        try {
            begin();
            getSession().saveOrUpdate(org);
            commit();
        } catch (HibernateException ex) {
            rollback();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка создания оплаты \n" + ex, "Ошика", JOptionPane.ERROR_MESSAGE);          
        } 
        return org;
    } 
    
}
