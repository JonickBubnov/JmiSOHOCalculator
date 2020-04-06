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
@Table(name="setprice_product")
public class SetPriceProduct extends DocumentProduct implements Serializable{
    
 @Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
private float price;
@OneToOne(cascade=CascadeType.ALL)
private Product product;
@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
private SetPrice setPrice;

public SetPriceProduct() {}

 @Override
    public long getId() {
        return id;
    }

 @Override
    public void setId(long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

 @Override
    public Product getProduct() {
        return product;
    }

 @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    public SetPrice getSetPrice() {
        return setPrice;
    }

    public void setSetPrice(SetPrice docPrice) {
        this.setPrice = docPrice;
    }

}
