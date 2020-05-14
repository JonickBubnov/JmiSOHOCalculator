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
@Table(name="act_product")
public class ActProduct extends DocumentProduct implements Serializable { 
  
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
private float cost;
@OneToOne(cascade=CascadeType.DETACH)
private Product product;
@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
private Act act;  
private int discount;
    
public ActProduct(){} 

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

@Override
    public float getCost() {
        return cost;
    }

@Override
    public void setCost(float cost) {
        this.cost = cost;
    }

@Override
    public Product getProduct() {
        return product;
    }

@Override
    public void setProduct(Product product) {
        this.product = product;
    }

@Override
    public int getDiscount() {
        return discount;
    }

@Override
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public ActProduct getActProduct(DocumentProduct product) {
        ActProduct ap = new ActProduct();
            ap.setCost(product.getCost());
            ap.setCount(product.getCount());
            ap.setDiscount(product.getDiscount());
            ap.setProduct(product.getProduct());
        return ap;
    }

}
