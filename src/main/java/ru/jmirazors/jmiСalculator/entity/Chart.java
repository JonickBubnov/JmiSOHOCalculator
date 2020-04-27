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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Jonick
 */
@Entity
@Table(name="chart")
public class Chart implements Serializable {

@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
private String num;
private String subnum;
private String name;
private String descr;
@OneToOne
private ChartGroup chartGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSubnum() {
        return subnum;
    }

    public void setSubnum(String subnum) {
        this.subnum = subnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public ChartGroup getChartGroup() {
        return chartGroup;
    }

    public void setChartGroup(ChartGroup chartGroup) {
        this.chartGroup = chartGroup;
    }


}
