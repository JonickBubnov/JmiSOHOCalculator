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
@Table(name="Setprice")
public class SetPrice extends Document implements Serializable{

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
private String descr;
@OneToOne
private User usr;
@ManyToOne
private PriceName pricename;
@OneToMany(fetch = FetchType.LAZY, mappedBy="setPrice", cascade=CascadeType.ALL)
private List<SetPriceProduct> setPriceProducts;
@OneToOne
private DocumentType documents;
@OneToOne
private Organization organization;


public SetPrice() {}

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
    public PriceName getPriceName() {
        return pricename;
    }

@Override
    public void setPriceName(PriceName pricename) {
        this.pricename = pricename;
    }

    public List<SetPriceProduct> getSetPriceProducts() {
        return setPriceProducts;
    }

    public void setSetPriceProducts(List<SetPriceProduct> setPriceProducts) {
//        if (this.setPriceProducts != null) {
//            this.setPriceProducts.clear();
//            this.setPriceProducts.addAll(setPriceProducts);            
//        } else
            this.setPriceProducts = setPriceProducts;
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
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


}
