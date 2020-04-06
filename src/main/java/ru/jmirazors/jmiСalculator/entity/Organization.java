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
@Table(name="organization")
public class Organization implements Serializable {
    
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native")
private long id;
private String name;
private String phone;
private String www;
private String email;
private String addresone;
private String addrestwo;
private String descr;
private String ogrn;
private String kpp;
private String inn;
private String okpo;
private String rs;
private String bank;
private String ks;
private String bik;
private byte del;
private int nds;

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

    public void setDescr(String descr) {
        this.descr = descr;
    }
    public String getDescr() {
        return descr;
    }

//    public String getOGRN() {
//        return ogrn;    public String getOKPO() {
//        return okpo;
//    }
//
//    public void setOKPO(String OKPO) {
//        this.okpo = OKPO;
//    }
//    }
//
//    public void setOGRN(String OGRN) {
//        this.ogrn = OGRN;
//    }

//

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getBIK() {
        return bik;
    }

    public void setBIK(String BIK) {
        this.bik = BIK;
    }

    public byte getDel() {
        return del;
    }

    public void setDel(byte del) {
        this.del = del;
    }

    public String getAddresone() {
        return addresone;
    }

    public void setAddresone(String addresone) {
        this.addresone = addresone;
    }

    public String getAddrestwo() {
        return addrestwo;
    }

    public void setAddrestwo(String addrestwo) {
        this.addrestwo = addrestwo;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public int getNds() {
        return nds;
    }

    public void setNds(int nds) {
        this.nds = nds;
    }
    
    
}
