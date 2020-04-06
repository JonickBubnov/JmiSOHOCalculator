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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Table(name="inventory_product")
public class InventoryProduct extends DocumentProduct implements Serializable {
   
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
private float count;
private float onstock;
@OneToOne(cascade=CascadeType.DETACH)
private Product product;
@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
private Inventory inventory; 

public InventoryProduct() {}

@Override
    public long getId() {
        return id;
    }

@Override
    public void setId(long id) {
        this.id = id;
    }

@Override
    public float getCount() {
        return count;
    }

@Override
    public void setCount(float count) {
        this.count = count;
    }

    public float getOnstock() {
        return onstock;
    }

    public void setOnstock(float onstock) {
        this.onstock = onstock;
    }


@Override
    public Product getProduct() {
        return product;
    }

@Override
    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


}
