/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

/**
 *
 * @author User
 */
public class SetPriceBean {
    
    private String rowId;
    private String articul;
    private String name;
    private float price;
    private String curency;
    
    public SetPriceBean(String rowId, String articul, String name, float price, String curency) {
        this.rowId = rowId;
        this.articul = articul;
        this.name = name;
        this.price = price;
        this.curency = curency;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurency() {
        return curency;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }
    
}
