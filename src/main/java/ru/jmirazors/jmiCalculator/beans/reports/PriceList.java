/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans.reports;

import java.util.List;
import ru.jmirazors.jmiСalculator.entity.Price;

/**
 *
 * @author User
 */
public class PriceList {
    
    String articul;
    String name;
    Float price1 = null;
    Float price2 = null;
    Float price3 = null;
    Float price4 = null;
    boolean prName1 = true;
    boolean prName2 = true;
    boolean prName3 = true;
    boolean prName4 = true;

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

    public Float getPrice1() {
        return price1;
    }

    public void setPrice1(float price1) {
        this.price1 = price1;
    }

    public Float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public Float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }

    public Float getPrice4() {
        return price4;
    }

    public void setPrice4(float price4) {
        this.price4 = price4;
    }

    public boolean isPrName1() {
        return prName1;
    }

    public void setPrName1(boolean prName1) {
        this.prName1 = prName1;
    }

    public boolean isPrName2() {
        return prName2;
    }

    public void setPrName2(boolean prName2) {
        this.prName2 = prName2;
    }

    public boolean isPrName3() {
        return prName3;
    }

    public void setPrName3(boolean prName3) {
        this.prName3 = prName3;
    }

    public boolean isPrName4() {
        return prName4;
    }

    public void setPrName4(boolean prName4) {
        this.prName4 = prName4;
    }    
    
    public void setPriceList(List<Price> price) {
        int size = price.size();
        for (int i = 0; i < size; i++) {
            if (price.get(i).getPricename().getId() == 1)
                setPrice1(price.get(i).getPrice());
            if (price.get(i).getPricename().getId() == 2)
                setPrice2(price.get(i).getPrice());
            if (price.get(i).getPricename().getId() == 3)
                setPrice3(price.get(i).getPrice());
            if (price.get(i).getPricename().getId() == 4)
                setPrice4(price.get(i).getPrice());
        }
    }
}
