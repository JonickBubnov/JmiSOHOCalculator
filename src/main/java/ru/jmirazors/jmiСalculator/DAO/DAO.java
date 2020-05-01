/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import ru.jmirazors.jmiCalculator.MainFrame;
import ru.jmirazors.jmiСalculator.entity.Log;

/**
 *
 * @author Jonick
 */
public class DAO {
    
    private static Session sessionObj;
    private static SessionFactory sessionFactoryObj;    
    private static final Logger log = Logger.getAnonymousLogger();
    
    
        private static SessionFactory buildSessionFactory() {
            System.out.println("BUILD");
        // Creating Configuration Instance & Passing Hibernate Configuration File.
            Configuration configObj = new Configuration();
            configObj.configure(DAO.class.getResource("/hibernate.cfg.xml"));
            configObj.setProperty("hibernate.connection.url", MainFrame.sessionParams.getDburl());
            configObj.setProperty("hibernate.dialect", MainFrame.sessionParams.getDbdialect());
            configObj.setProperty("hibernate.connection.driver_class", MainFrame.sessionParams.getDbdriver());
            configObj.setProperty("hibernate.connection.username", MainFrame.sessionParams.getDbusername());
            configObj.setProperty("hibernate.connection.password", MainFrame.sessionParams.getDbpass());
            
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Contacts.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Contragent.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.ContragentStatus.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Group.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Invoice.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.InvoiceProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Privileges.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Product.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Stock.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Storage.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Unit.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.User.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Arrival.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.ArrivalProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.DocStatus.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Offer.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.OfferProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Transfer.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.TransferProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.PriceName.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Price.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.DocumentType.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Subordin.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.SetPrice.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.SetPriceProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Organization.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Kassa.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Pay.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.PayType.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Bill.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Deduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.DeductProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Receipt.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.ReceiptProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Sklad.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Log.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Act.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.ActProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Sale.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.SaleProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Inventory.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.InventoryProduct.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Parameters.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Okv.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Cash.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Loyalty.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.LoyaltyLog.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Department.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.ChartGroup.class);
            configObj.addAnnotatedClass(ru.jmirazors.jmiСalculator.entity.Chart.class);
            
            
        try {
            ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 
            sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
            return sessionFactoryObj; 
            } catch (HibernateException e) {
                //JOptionPane.showMessageDialog(null, "[DAO]\nНе могу установить соединение с базой.\n"+e, "Ошибка",
                  //  JOptionPane.ERROR_MESSAGE);
//                    System.out.println("Создаем базу");
//                    configObj.setProperty("hibernate.connection.url", MainFrame.sessionParams.getDburl()+"?createDatabaseIfNotExist=true");
//                    ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 
                    //sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
                    
                                                             
                    //System.exit(0);
            }
        return null;
    }
    

    protected DAO() {}
    
    

    public Session getSession() {
        if (sessionObj == null) {
            sessionObj = buildSessionFactory().openSession();           
        } 
        if (!sessionObj.isOpen()) {
            sessionObj = sessionFactoryObj.openSession(); 
        }
        return sessionObj;
    }
    

    protected void begin() {
            getSession().beginTransaction();            
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    protected void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Cannot rollback", e);
        }
        try {
            getSession().close();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Cannot close", e);
        }
    }

    public void close() {
        getSession().close();
    }
    
}