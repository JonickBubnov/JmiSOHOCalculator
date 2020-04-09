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
import javax.persistence.ManyToOne;
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
@Table(name="Pay")
public class Pay extends Document implements Serializable {
    
    public Pay() {}
    
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
@Basic
@Temporal(TemporalType.TIMESTAMP)
private java.util.Date indate;
@ManyToOne
private Contragent contragent;
//private long docId;
//@OneToOne
//@JoinColumn(name="doctype")
//private DocumentType docType;
@OneToOne
private PayType paytype;
private float total;
private byte debcr;
@OneToOne
private User usr;
private String descr;
@OneToOne
private Organization organization;
@OneToOne
private DocumentType documents;
@OneToOne
private DocStatus status;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Date getIndate() {
        return indate;
    }

    @Override
    public void setIndate(Date indate) {
        this.indate = indate;
    }

    @Override
    public Contragent getContragent() {
        return contragent;
    }

    @Override
    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }

//    public long getDocId() {
//        return docId;
//    }
//
//    public void setDocId(long docId) {
//        this.docId = docId;
//    }
//
//    public DocumentType getDocumentType() {
//        return docType;
//    }
//
//    public void setDocumentType(DocumentType docType) {
//        this.docType = docType;
//    }

    @Override
    public float getTotal() {
        return total;
    }

    @Override
    public void setTotal(float summa) {
        this.total = summa;
    }

    public byte getDebcr() {
        return debcr;
    }

    public void setDebcr(byte debcr) {
        this.debcr = debcr;
    }

    public PayType getPaytype() {
        return paytype;
    }

    public void setPaytype(PayType paytype) {
        this.paytype = paytype;
    }

    @Override
    public User getUsr() {
        return usr;
    }

    @Override
    public void setUsr(User user) {
        this.usr = user;
    }

    @Override
    public DocumentType getDocuments() {
        return documents;
    }

    @Override
    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }

    @Override
    public String getDescr() {
        return descr;
    }

    @Override
    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public Organization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(Organization organization) {
        this.organization = organization;
    } 

    @Override
    public DocStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DocStatus status) {
        this.status = status;
    }
    
  
    
    
}
