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
@Table(name="arrival_product")
public class ArrivalProduct extends DocumentProduct implements Serializable {

public ArrivalProduct() {}
    

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
private Arrival arrival;
private float discount;
    

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

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

@Override
    public float getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    
}
