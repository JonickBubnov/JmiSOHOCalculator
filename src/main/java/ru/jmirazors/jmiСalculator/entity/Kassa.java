/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Table(name="Kassa")
public class Kassa implements Serializable {
    @Id
    @GeneratedValue(
        strategy= GenerationType.AUTO, 
        generator="native")
    @GenericGenerator(
        name = "native", 
        strategy = "native")
    private long id;
    private long doc_id;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date indate;    
    @OneToOne
    private DocumentType documents;
    private float debt;
    private float credit;
    @OneToOne
    private Contragent contragent;
    @OneToOne
    private Organization organization;
    
    public Kassa(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(long doc_id) {
        this.doc_id = doc_id;
    }

    public DocumentType getDocumenttype() {
        return documents;
    }

    public void setDocumenttype(DocumentType documenttype) {
        this.documents = documenttype;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public Contragent getContragent() {
        return contragent;
    }

    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public DocumentType getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }
    
}
