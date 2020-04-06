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
public class InvoiceBean {
    
    private String rowId;
    private String articul;
    private String name;
    private float count;
    private String unit;
    private float price;
    private float sum;
    
    public InvoiceBean (String rowId, String articul, String name, float count, 
            String unit, float price, float sum) {
        this.rowId = rowId;
        this.articul = articul;
        this.name = name;
        this.count = count;
        this.unit = unit;
        this.price = price;
        this.sum = sum;
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

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
    
}
