/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import ru.jmirazors.jmiСalculator.DAO.PriceDAO;
import ru.jmirazors.jmiСalculator.DAO.StockDAO;

/**
 *
 * @author User
 */

@Entity
@Table(name="product")
public class Product implements Serializable {
    
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
    private long id;
    private String articul;
    private String name;
    private String description;
    @Column(name="image", nullable=true, columnDefinition="mediumblob")
    private byte[] image;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<Price> price;
    private byte del;
    private byte onstock;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL)
    private List<Stock> stock;
    @OneToOne
    private Unit unit;
    private float weight;
    private String barcode;
    @ManyToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
    @JoinColumn(name="parent", insertable=true, updatable=true)
    private Group group;
    
    
    public Product(){}
        
    public long getId(){
        return id;
    }
    protected void setId(long id){
        this.id = id;
    }
    public String getArticul(){
        return articul;
    }   
    public void setArticul (String articul){
        this.articul = articul;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public List<Price> getPrice(){
        return price;
    }
    public void setPrice(List<Price> price){
        this.price = price;
    }
//    public Price getActualPrice(PriceName priceName) {
//        Price priceLocal = new Price();
//        priceLocal.setPrice(0);
//        priceLocal.setIndate(new Date(1990));
//        List<Price> priceList = getPrice();
//        for (int i = 0; i < priceList.size(); i++) {
//            if (priceList.get(i).getPricename().getId() == priceName.getId() && priceLocal.getIndate().before(priceList.get(i).getIndate())) {
//                priceLocal = priceList.get(i);
//            }
//        }
//        return priceLocal;
//    }
    
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    
    public byte[] getImage(){
        return image;
    }
    
    public void setImage(byte[] image) throws IOException{
        this.image = image;
    }
    
    public byte getDel() {
        return del;
    }
    public void setDel(byte del) {
        this.del = del;
    }

    public byte getOnstock() {
        return onstock;
    }

    public void setOnstock(byte onstock) {
        this.onstock = onstock;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }
    
    public void addStock(Stock stock) {
            getStock().add(stock);
    }
    
    
    public float getTotal() {
        float total = 0;        
        List<Stock> stocks = getStock();
        if (stocks != null)
            for (int i = 0; i < stocks.size(); i++) {
                total += stocks.get(i).getCount();
            }
        return total;
    }
    
    public List<Price> getActualPriceList() {
        return new PriceDAO().getActualPriceList(getId());
    }
    public Price getActualPrice(PriceName pricename) {
        return new PriceDAO().getActualPrice(getId(), pricename);
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    
}
