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
import ru.jmirazors.jmiСalculator.entity.Department;
import ru.jmirazors.jmiСalculator.entity.Okv;
import ru.jmirazors.jmiСalculator.entity.Organization;
import ru.jmirazors.jmiСalculator.entity.Parameters;

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
                    "[OrganizationDAO]\nОшибка чтения списка организаций", "Ошибка", JOptionPane.ERROR_MESSAGE);                        
        } 
        return organizations;
    }  
    
    // *******************************    **************************************
    public Organization findOrganization(String name) throws SQLException {
        Organization organization = null;
        try{
            begin();            
            Query query = getSession().createQuery("FROM Organization WHERE name= :name");
            query.setParameter("name", name);
            organization = (Organization)query.uniqueResult();
            commit();            
        } catch (HibernateException ex) {
            rollback();            
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка поиска организации <"+name+">", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
        return organization;
    }    
    public Organization getOrganization(Long id) throws SQLException {
        Organization organization = null;
        try{
            begin();            
            //long i = Long.valueOf(id);
            organization = getSession().get(Organization.class, id);
            commit();            
        } catch (HibernateException ex) {
            rollback();            
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка поиска организации <"+id+">", "Ошибка", JOptionPane.ERROR_MESSAGE);            
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
                    "[OrganizationDAO]\nОшибка создания организации\n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);          
        } 
        return org;
    }
    
    public Parameters getParam(Organization org) {
        Parameters param = null;
        try {
            begin();
            Query query = getSession().createQuery("FROM Parameters p WHERE p.organization=:org");
                query.setParameter("org", org);
            param = (Parameters)query.uniqueResult();
            commit();
        } catch (HibernateException ex) {
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка параметров организации\n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);}
        return param;
    }
    
    public List<Okv> getOkvList() {
        List<Okv> okv = new ArrayList<>();
        try {
            begin();            
            okv = getSession().createQuery("FROM Okv", Okv.class).getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка чтения списка организаций", "Ошибка", JOptionPane.ERROR_MESSAGE);                        
        } 
        return okv;        
    }
    public List<Department> getDepartments() {
        List<Department> department = new ArrayList();
        try {
            begin();
            department = getSession().createQuery("FROM Department WHERE del=1", Department.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка чтения списка подразделений", "Ошибка", JOptionPane.ERROR_MESSAGE);                        
        }
        return department;
    }
    public void updateDepartment(Department dep) {
        try {
            begin();
            getSession().saveOrUpdate(dep);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка создания подразделения\n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);          
        } 
    }  
    public Department getDepartment(Long id) {
        Department dep = null;
        try{
            begin();                        
            dep = getSession().get(Department.class, id);
            commit();            
        } catch (HibernateException ex) {
            rollback();            
            JOptionPane.showMessageDialog(null, 
                    "[OrganizationDAO]\nОшибка поиска подразделения <"+id+">", "Ошибка", JOptionPane.ERROR_MESSAGE);            
        } 
        return dep;
    }    
    
}
