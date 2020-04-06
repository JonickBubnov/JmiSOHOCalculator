/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiCalculator.beans.SessionParams;
import ru.jmirazors.jmiСalculator.DAO.SubordinDAO;
import ru.jmirazors.jmiСalculator.jmiframes.DocInvoice;
import ru.jmirazors.jmiСalculator.jmiframes.MainFrame;
import ru.jmirazors.jmiСalculator.jmiframes.SubordinDocDialog;

/**
 *
 * @author User
 */
public class Document {
    private long id;
    private Date indate;
    private DocStatus status;
    private Contragent contragent;
    private float total;
    private String descr;
    private User usr;
    private Storage storage;
    private PriceName priceName;
    private List<DocumentProduct> documentProducts;
    private DocumentType documents;
    private Organization organization;
    private final SessionParams parameters = MainFrame.sessionParams;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public DocStatus getStatus() {
        return status;
    }

    public void setStatus(DocStatus status) {
        this.status = status;
    }

    public Contragent getContragent() {
        return contragent;
    }

    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }
    
    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public User getUsr() {
        return usr;
    }

    public void setUsr(User user) {
        this.usr = user;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
    
    public List<DocumentProduct> getDocumentProducts() {
        return documentProducts;
    }

    public void setDocumentProducts(List<DocumentProduct> documentProducts) {
        if (this.documentProducts != null) {
            this.documentProducts.clear();
            this.documentProducts.addAll(documentProducts);
        } else
            this.documentProducts = documentProducts;
    }  

    public DocumentType getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }

    public PriceName getPriceName() {
        return priceName;
    }

    public void setPriceName(PriceName priceName) {
        this.priceName = priceName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    public String getDocumentName() {
        return getDocuments().getName();
    }
    
    /**
     *
     * @return
     */
    public Class getDocumentClass() {
        return new DocumentUtil().getDocumentClass(getDocuments().getId());
    }
    
    public boolean isSaved() {
        return getId() != 0;
    }
    
    public User getSessionUser() {
        return parameters.getUser();
    }
    public Organization getSessionOrganization() {
        return parameters.getOrganization();
    }
    
    public void showSubordins() {
        try {           
            List<Subordin> subdocs_l1 = new SubordinDAO().list(getId(), getDocuments().getId());
            if (subdocs_l1 == null || subdocs_l1.isEmpty())
                JOptionPane.showMessageDialog(null, 
                    "Нет подчиненных документов", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            else
                new SubordinDocDialog(null, true, subdocs_l1).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(DocInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

}
