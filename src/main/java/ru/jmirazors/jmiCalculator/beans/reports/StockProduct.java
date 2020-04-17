/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans.reports;

import ru.jmirazors.jmiСalculator.entity.Product;
import ru.jmirazors.jmiСalculator.entity.Storage;

/**
 *
 * @author User
 */
public class StockProduct {
    
    Product product;
    Storage storage;
    Float count;

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

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }
    
    public String getName() {
        return getProduct().getName();
    }
    public String getArticul() {
        return getProduct().getArticul();        
    }
    public String getUnit() {
        return getProduct().getUnit().getName();
    }
    public String getSt() {        
        return storage.getName();
    }
    
}
