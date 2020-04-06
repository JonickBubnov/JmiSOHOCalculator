/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Table(name="stock")
public class Stock implements Serializable {
    
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
//private long storage_id;
//private long product_id;
private float count;
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//@JoinColumn(name="product_id", nullable=true, insertable=false, updatable=false)
private Product product;
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//@JoinColumn(name="storage_id", nullable=true, insertable=false, updatable=false)
private Storage storage;

public Stock() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public long getStorage_id() {
//        return storage_id;
//    }

//    public void setStorage_id(long strage_id) {
//        this.storage_id = strage_id;
//    }

//    public long getProduct_id() {
//        return product_id;
//    }
//
//    public void setProduct_id(long product_id) {
//        this.product_id = product_id;
//    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
  

}
