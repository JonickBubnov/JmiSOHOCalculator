/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Table(name="Subordin")
public class Subordin implements Serializable {
    
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
private long main_id;
private long maindoc_id;

private long sub_id;
private long subdoc_id;
    
    public Subordin() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMain_id() {
        return main_id;
    }

    public void setMain_id(long main_id) {
        this.main_id = main_id;
    }

    public long getMaindoc_id() {
        return maindoc_id;
    }

    public void setMaindoc_id(long maindoc_id) {
        this.maindoc_id = maindoc_id;
    }

    public long getSub_id() {
        return sub_id;
    }

    public void setSub_id(long sub_id) {
        this.sub_id = sub_id;
    }

    public long getSubdoc_id() {
        return subdoc_id;
    }

    public void setSubdoc_id(long subdoc_id) {
        this.subdoc_id = subdoc_id;
    }
    
}
