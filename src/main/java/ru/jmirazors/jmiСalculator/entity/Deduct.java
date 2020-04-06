/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="Deduct")
public class Deduct extends Document implements Serializable {
    
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
@OneToOne
private DocStatus status;
private float total;
private String descr;
@OneToOne
private User usr;
@ManyToOne
private Storage storage;
@OneToMany(fetch = FetchType.LAZY, mappedBy="deduct", cascade=CascadeType.ALL)
private List<DeductProduct> deductProducts; 
@OneToOne
private Organization organization;
@OneToOne
private Contragent contragent;
@OneToOne
private DocumentType documents;
private float weight;

    public Deduct() {}

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
    public DocStatus getStatus() {
        return status;
    }

@Override
    public void setStatus(DocStatus status) {
        this.status = status;
    }

@Override
    public float getTotal() {
        return total;
    }

@Override
    public void setTotal(float total) {
        this.total = total;
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
    public User getUsr() {
        return usr;
    }

@Override
    public void setUsr(User usr) {
        this.usr = usr;
    }

@Override
    public Storage getStorage() {
        return storage;
    }

@Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<DeductProduct> getDeductProducts() {
        return deductProducts;
    }

    public void setDeductProducts(List<DeductProduct> deductProducts) {
            this.deductProducts = deductProducts;
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
    public DocumentType getDocuments() {
        return documents;
    }

@Override
    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

@Override
    public Contragent getContragent() {
        return contragent;
    }

@Override
    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }
 
    
}
