/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import ru.jmirazors.jmiСalculator.jmiframes.Documents.Document;
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
@Table(name="Inventory")
public class Inventory extends Document implements Serializable {
   
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
@OneToOne
private Storage storage;
@OneToMany(fetch = FetchType.LAZY, mappedBy="inventory", cascade=CascadeType.ALL)
private List<InventoryProduct> inventoryProducts;
@OneToOne
private Organization organization;
@OneToOne
private DocumentType documents; 
@OneToOne
private Department department;

public Inventory(){}

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
    public Storage getStorage() {
        return storage;
    }

@Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<InventoryProduct> getInventoryProducts() {
        return inventoryProducts;
    }

    public void setInventoryProducts(List<InventoryProduct> inventoryProducts) {
        this.inventoryProducts = inventoryProducts;
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

@Override
    public Department getDepartment() {
        return department;
    }

@Override
    public void setDepartment(Department department) {
        this.department = department;
    }


}
