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
import ru.jmirazors.jmiСalculator.jmiframes.Documents.Document;
import ru.jmirazors.jmiСalculator.entity.Subordin;

/**
 *
 * @author User
 */
public class SubordinDAO extends DAO {
    
    public List<Subordin> list(long mid, long mdid) throws Exception {
        List<Subordin> documents = new ArrayList<>();
        try {
            begin();
            Query query = getSession().createQuery("FROM Subordin WHERE main_id = :mid AND maindoc_id = :mdid");
            query.setParameter("mid", mid);
            query.setParameter("mdid", mdid);
            commit();
            documents = query.list();
        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка чтения списка подчиненных Документов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return documents;
    } 
    public Subordin saveSub(Subordin document) throws Exception{
        try {
            begin();
            getSession().saveOrUpdate(document);
            getSession().flush();
            getSession().clear();
            commit();

        } catch (HibernateException ex) {
            rollback();
            JOptionPane.showMessageDialog(null, 
                    "Ошибка сохранения Документа. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } 
        return document;
    }    

    public Subordin getSubDocument(Document doc) {
        Subordin subDocument = null;
        try {
            begin();
            Query query = getSession().createQuery("FROM Subordin WHERE sub_id = :sid AND subdoc_id = :sdid");
            query.setParameter("sid", doc.getId());
            query.setParameter("sdid", doc.getDocuments().getId());
            subDocument = (Subordin) query.uniqueResult();
            commit();
        } catch (HibernateException ex) {            
            JOptionPane.showMessageDialog(null, 
                    "Ошибка поиска Документа. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return subDocument;
    }
    
}
