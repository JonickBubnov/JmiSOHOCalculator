/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans.reports;

import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.DocumentType;

/**
 *
 * @author User
 */
public class SaleDoc {
    
    DocumentType docType;
    long num;
    String data;
    float debt;
    float crdt;
    float sumd;
    Contragent contr;

    public String getContr() {
        return contr.getName();
    }

    public void setContr(Contragent contr) {
        this.contr = contr;
    }
    
    
    public String getDoc() {
        return getDocType().getName();
    }
    
    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Float getDebt() {
        if (debt != 0)
            return debt;
        else
            return null;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public Float getCrdt() {
        if (crdt != 0)
            return crdt;
        else
            return null;
    }

    public void setCrdt(float crdt) {
        this.crdt = crdt;
    }
    public float sumd() {
        return sumd;
    }
    
    
}
