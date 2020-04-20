/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

//import com.ibm.icu.util.Calendar;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.HibernateException;
import ru.jmirazors.jmiСalculator.entity.DocStatus;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.DocumentType;
import ru.jmirazors.jmiСalculator.entity.Kassa;

/**
 *
 * @author Jonick
 */
public class DocumentDAO extends DAO {
        
    
    public List<Document> list(Class docType, Map properties) throws Exception {
        List<Document> documents = new ArrayList<>();
        try {
            begin();
            String hql = "FROM " + docType.getSimpleName()
                    + " WHERE status_id <> :del"
                    + " AND organization.name LIKE :org"
                    + " AND usr.name LIKE :usr"
                    + " AND indate BETWEEN :begin AND :end";
            Query query;
            if (!properties.get("rowCount").equals("0"))
                query = getSession().createQuery(hql, docType).setMaxResults(Integer.parseInt(properties.get("rowCount").toString()));
            else
                query = getSession().createQuery(hql, docType);
                
                if (properties.get("showDel").equals("yes"))
                    query.setParameter("del", 0);
                else
                    query.setParameter("del", 3); // не показывать (3) удаленные
                    query.setParameter("org", properties.get("organization")); // фильтр по организации
                    query.setParameter("usr", properties.get("user")); //фильтр по пользователю
                
                query.setParameter("begin", new SimpleDateFormat("dd.MM.yyyy").parse(properties.get("firstDate").toString()));
    
                if (!properties.get("lastDate").toString().equals("curdate"))
                    query.setParameter("end", new SimpleDateFormat("dd.MM.yyyy").parse(properties.get("lastDate").toString()));
                else {
                   
                    query.setParameter("end", DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DATE), 1));
                     }                
                
            documents = query.getResultList();
            commit();            
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка чтения списка Документов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return documents;
    }
    public DocumentType getDocumentType(Long id) {
        DocumentType docType = null;
        try {
            begin();
            docType = (DocumentType)getSession().get(DocumentType.class, id);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка чтения типа Документа", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return docType;
    }
    
    public List<DocumentType> listDocumenTypes() {
        List<DocumentType> docTypes = new ArrayList<>();
        try {
            begin();
            docTypes = getSession().createQuery("FROM DocumentType WHERE payready=1", DocumentType.class).getResultList();
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка чтения типа Документа", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }             
        return docTypes;
    }
    
    public Document getDocument(String id, Class doc) throws Exception {
        Document document = null;
        try {
            begin();
            document = (Document)getSession().get(doc, Long.valueOf(id));
            commit();              
        }catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка чтения Документа", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return document;
    }
        
    public Document updateDocument(Document document) throws Exception{

        Document d = document;
        try {
            begin();
            getSession().saveOrUpdate(document);
            //d = (Document)getSession().merge(document);               
            commit(); 

        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка сохранения Документа. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return d;
    }    
    
    public DocStatus getStatus(Long id) {
        DocStatus status = null;
        try {
            begin();
            status = getSession().get(DocStatus.class, id);
            commit();            
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка чтения статуса Документа", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return status;
    } 
    
    public Kassa updateKassa(Kassa kassa) {
        try {
            begin();
            getSession().save(kassa);
            commit();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "[DocumentDAO] \nОшибка сохранения касса. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return kassa;    
    }

    public void ev(Document doc) {
        begin();
        getSession().evict(doc);
        commit();
    }

}
