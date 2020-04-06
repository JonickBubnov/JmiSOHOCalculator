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
@Table(name="Sklad")
public class Sklad implements Serializable {

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
@OneToOne
private Product product;
@Basic
@Temporal(TemporalType.TIMESTAMP)
private java.util.Date indate;
private float prihod;
private float rashod;
private float cost;
@OneToOne
private DocumentType documents;
private long docNumber;
    
    
public Sklad() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public float getPrihod() {
        return prihod;
    }

    public void setPrihod(float prihod) {
        this.prihod = prihod;
    }

    public float getRashod() {
        return rashod;
    }

    public void setRashod(float rashod) {
        this.rashod = rashod;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public DocumentType getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }

    public long getDoc() {
        return docNumber;
    }

    public void setDoc(long doc) {
        this.docNumber = doc;
    }


}
