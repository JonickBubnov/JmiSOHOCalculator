/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Table(name="contragent")
public class Contragent implements Serializable {
   
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
private String name;
private String phone;
private String www;
private String email;
private String addres1;
private String addres2;
private String OGRN;
private String INN;
private String KPP;
private String OKPO;
private String rs;
private String ks;
private String BIK;
private String bank;
private int rating;
private String descr;
//private int status;
private byte del;
@OneToMany(fetch = FetchType.LAZY, mappedBy="contragent", cascade=CascadeType.ALL, orphanRemoval = true)
private List<Contacts> contacts;
@OneToOne
@JoinColumn(name="status", insertable = false, updatable = false)
private ContragentStatus cstatus;

//@JoinColumn(name="id")
//private Invoice invoice;

public Contragent(){}

    public ContragentStatus getCstatus() {
        return cstatus;
    }

    public void setCstatus(ContragentStatus cstatus) {
        this.cstatus = cstatus;
    }


    public List<Contacts> getContacts() {
        //System.out.println("********** CONTACTS ********" + contacts.size());
        return contacts;
    }

    public void setContacts(List<Contacts> contacts) {
        this.contacts = contacts;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddres1() {
        return addres1;
    }

    public void setAddres1(String addres) {
        this.addres1 = addres;
    }

    public String getAddres2() {
        return addres2;
    }

    public void setAddres2(String address2) {
        this.addres2 = address2;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String description) {
        this.descr = description;
    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }

    public void setDel(byte del) {
        this.del = del;
    }
    public byte getDel() {
        return del;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getKPP() {
        return KPP;
    }

    public void setKPP(String KPP) {
        this.KPP = KPP;
    }

    public String getOKPO() {
        return OKPO;
    }

    public void setOKPO(String OKPO) {
        this.OKPO = OKPO;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getBIK() {
        return BIK;
    }

    public void setBIK(String BIK) {
        this.BIK = BIK;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOGRN() {
        return OGRN;
    }

    public void setOGRN(String OGRN) {
        this.OGRN = OGRN;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
