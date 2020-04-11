/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.DAO.StockDAO;
import ru.jmirazors.jmiСalculator.DAO.SubordinDAO;
import ru.jmirazors.jmiСalculator.entity.Act;
import ru.jmirazors.jmiСalculator.entity.Arrival;
import ru.jmirazors.jmiСalculator.entity.Bill;
import ru.jmirazors.jmiСalculator.entity.Deduct;
import ru.jmirazors.jmiСalculator.entity.DocStatus;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Inventory;
import ru.jmirazors.jmiСalculator.entity.Invoice;
import ru.jmirazors.jmiСalculator.entity.Offer;
import ru.jmirazors.jmiСalculator.entity.Pay;
import ru.jmirazors.jmiСalculator.entity.Receipt;
import ru.jmirazors.jmiСalculator.entity.Sale;
import ru.jmirazors.jmiСalculator.entity.SetPrice;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.Subordin;
import ru.jmirazors.jmiСalculator.entity.Transfer;
import ru.jmirazors.jmiСalculator.jmiframes.MainFrame;

/**
 *
 * @author User
 */
public class DocumentUtil {
    
    DocumentDAO documentDAO = new DocumentDAO();
    //IFBean ifBean = new IFBean();
    
     // ************************************************************************
    // Получить класс документа
    // *************************************************************************
    public Class getDocumentClass(long id) {
        
        switch ((int)id) {
            case 1:
                return Invoice.class;
            case 2:
                return Arrival.class;
            case 3:
                return Offer.class;
            case 4:
                return Transfer.class;
            case 5:
                return SetPrice.class;
            case 6:
                return Deduct.class;
            case 7:
                return Inventory.class;
            case 8:
                return Pay.class;
            case 9:
                return Bill.class;
            case 10:
                return Receipt.class;
            case 11:
                return Act.class;
            case 12:
                return Sale.class;
        }
        return null;        
    }    
    // ***********   подчиненные документы **********************************
//    // Получить и сохранить объект подчиненный документ
    public void saveParent (Document parent, Document document) {
        Subordin subDoc = null;
        subDoc = new SubordinDAO().getSubDocument(document);
        if (subDoc == null && parent != null) {
                subDoc = new Subordin();
                subDoc.setMain_id(parent.getId());
                subDoc.setSub_id(document.getId());
                subDoc.setMaindoc_id(parent.getDocuments().getId());
                subDoc.setSubdoc_id(document.getDocuments().getId());
                try {
                    new SubordinDAO().saveSub(subDoc);
                } catch (Exception ex) {
                    Logger.getLogger(DocumentUtil.class.getName()).log(Level.SEVERE, null, ex);
                }            
        }       
    }
        
    // Получить документ основание
    public Document getMainDocument(Subordin subDocument) {
        Document doc = null;
        try {
            doc = new DocumentDAO().getDocument(String.valueOf(subDocument.getMain_id()), getDocumentClass(subDocument.getMaindoc_id()));
        } catch (Exception ex) {
            Logger.getLogger(DocumentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
    
    // получить документ основание
    public Document getMainDocument(Document subDocument) {
        Document doc = null;
        Subordin subordin = new SubordinDAO().getSubDocument(subDocument);
        if (subordin != null) 
            doc = getMainDocument(subordin);
        return doc;
    }
    
    // Создать подчиненный документ
    // Запись в таблицу подчиненных документов
    public Subordin createSubordin(Document mainDoc, Document subDoc) {
        Subordin sub = new Subordin();
            sub.setMain_id(mainDoc.getId());
            sub.setMaindoc_id(mainDoc.getDocuments().getId());
            sub.setSub_id(subDoc.getId());
            sub.setSubdoc_id(subDoc.getDocuments().getId());
        try {
            new SubordinDAO().saveSub(sub);
        } catch (Exception ex) {
            Logger.getLogger(DocumentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sub;
    }    
    
    // Удалить документ
    public DocStatus delete() {
        return new DocumentDAO().getStatus(3l);            
    }
    // создать счет
    public void createBillDocument(Document document) {
        Bill bill = new Bill();        
            bill.setContragent(document.getContragent());
            bill.setOrganization(document.getOrganization());
            bill.setIndate(new Date());
            bill.setDescr(document.getDescr());
            bill.setTotal(document.getTotal());
            bill.setUsr(MainFrame.sessionParams.getUser());
            bill.setDocuments(documentDAO.getDocumentType(9l));
            bill.setStatus(documentDAO.getStatus(1l));
        try {
            bill = (Bill)documentDAO.updateDocument(bill);
            createSubordin(document, bill);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Не удалось создать счет на оплату. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }             
            JOptionPane.showMessageDialog(null, "Счет успешно создан: \nСчет на оплату №"+bill.getId(), "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }
    // *************************************************************************
    // получить цену товара со скидкой
    // *************************************************************************
    public float getCostWhithDiscount(DocumentProduct product) {
        float cost = product.getCost();
        cost = product.getCost() - product.getCost()*product.getDiscount()/100;
        return cost;
    }
    
    //
    public List<Document> getSubordinDocuments(Bill bill) {
        List<Document> documents = new ArrayList<>();
        List<Subordin> subs = null;
        try {
            subs = new SubordinDAO().list(bill.getId(), bill.getDocuments().getId());
        if (subs != null) {
            for (Subordin sub : subs) {
                documents.add(new DocumentDAO().getDocument(String.valueOf(sub.getSub_id()), getDocumentClass(sub.getSubdoc_id())));
                    }
                }
        } catch (Exception ex) {
            Logger.getLogger(DocumentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
        
    
    public void stockMinus(List<DocumentProduct> products, Storage storage) {
        StockDAO sDAO = new StockDAO();
        for (DocumentProduct product : products) {
            Stock storeStock = sDAO.findStock(storage, product.getProduct());
            if (storeStock == null) {
                storeStock = new Stock();
                storeStock.setProduct(product.getProduct());
                storeStock.setStorage(storage);
                storeStock.setCount(0f);
            }
            if (product.getProduct().getOnstock() == 1)
                storeStock.setCount(storeStock.getCount() - product.getCount());                
            sDAO.updateStock(storeStock);
            }      
    }
    
    public void stockPlus(List<DocumentProduct> products, Storage storage) {
        StockDAO sDAO = new StockDAO();
        for (DocumentProduct product : products) {
            Stock storeStock = sDAO.findStock(storage, product.getProduct());
            if (storeStock == null) {
                storeStock = new Stock();
                storeStock.setProduct(product.getProduct());
                storeStock.setStorage(storage);
                storeStock.setCount(0f);
            }
            if (product.getProduct().getOnstock() == 1)
                storeStock.setCount(storeStock.getCount() + product.getCount());                
            sDAO.updateStock(storeStock);
            }      
    }    
}
