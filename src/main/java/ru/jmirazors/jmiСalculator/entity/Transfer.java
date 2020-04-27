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
@Table(name="Transfer")
public class Transfer extends Document implements Serializable {

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
@ManyToOne
private Storage storage_from;
@ManyToOne
private Storage storage_to;
private float total;
@OneToOne
private User usr;
private String descr;
@OneToMany(fetch = FetchType.LAZY, mappedBy="transfer", cascade=CascadeType.ALL)
private List<TransferProduct> transferProducts;
private float weight;
@OneToOne
private DocumentType documents;
@OneToOne
private Organization organization;
@OneToOne
private Department department;

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

    public Storage getStorage_from() {
        return storage_from;
    }

    public void setStorage_from(Storage storage_from) {
        this.storage_from = storage_from;
    }

    public Storage getStorage_to() {
        return storage_to;
    }

    public void setStorage_to(Storage storage_to) {
        this.storage_to = storage_to;
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
    public String getDescr() {
        return descr;
    }

@Override
    public void setDescr(String descr) {
        this.descr = descr;
    }

    public List<TransferProduct> getTransferProducts() {
        return transferProducts;
    }

    public void setTransferProducts(List<TransferProduct> transferProducts) {
            this.transferProducts = transferProducts;    
    }

@Override
    public float getTotal() {
        return total;
    }

@Override
    public void setTotal(float total) {
        this.total = total;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

@Override
    public void setOrganization(Organization organization) {
        this.organization = organization;
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
