/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.text.DecimalFormat;

/**
 *
 * @author User
 */

public class ProductBean {
    
    DecimalFormat df = new DecimalFormat("#.##");
    
    private int rowId;
    private String articul;
    private String barcode;
    private String name;
    private float count;
    private String unit;
    private float discount;
    private float price;
    private float sum; 
    
    public ProductBean(int rowId, String articul, String barcode,
            String name, float count, String unit,
            float price, float discount, float sum) {
        this.rowId = rowId;
        this.articul = articul;
        this.barcode = barcode;
        this.name = name;
        this.count = count;
        this.unit = unit;
        this.discount = discount;
        this.price = price;
        this.sum = sum;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCount() {
        return  Float.valueOf(df.format(count));
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

    public float getDiscount() {
        return Float.valueOf(df.format(discount));
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPrice() {
        return Float.valueOf(df.format(price));
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSum() {
        return Float.valueOf(df.format(sum));
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
    
    
}
