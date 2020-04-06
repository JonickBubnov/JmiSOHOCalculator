 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.jmirazors.jmiСalculator.entity.ArrivalProduct;
import ru.jmirazors.jmiСalculator.entity.DeductProduct;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Kassa;
import ru.jmirazors.jmiСalculator.entity.OfferProduct;
import ru.jmirazors.jmiСalculator.entity.ReceiptProduct;
import ru.jmirazors.jmiСalculator.entity.Sklad;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.TransferProduct;

/**
 *
 * @author User
 */

public class UtilDAO extends DAO {

    public UtilDAO(){}
    
    StockDAO sDAO = new StockDAO();    
    
    
    // вычесть со склада товары реализации
    public void stockMinus(List<OfferProduct> products, Storage storage) {
        for (OfferProduct product : products) {
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
    public void stockMinus1(List<DeductProduct> products, Storage storage) {
        for (DeductProduct product : products) {
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
        public void stockMinus2(List<TransferProduct> products, Storage storage) {
        for (TransferProduct product : products) {
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
    // добавить на склад товары поступления
    public void stockPlus(List<ArrivalProduct> products, Storage storage) {
        for (ArrivalProduct product : products) {
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
    public void stockPlus1(List<ReceiptProduct> products, Storage storage) {
        for (ReceiptProduct product : products) {
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
        public void stockPlus2(List<TransferProduct> products, Storage storage) {
        for (TransferProduct product : products) {
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
    
    // Записать таблицу склад для разных документов
    // ********************************************************************
    public void updateSkladOffer(List<OfferProduct> products, Document doc) { 
        Sklad sklad;
        Date date = new Date();        
        for (DocumentProduct product : products) {
            sklad = new Sklad();
            sklad.setDoc(doc.getId());
            sklad.setIndate(date);
            sklad.setProduct(product.getProduct());
            sklad.setDocuments(doc.getDocuments());
            sklad.setCost(product.getCost());
            sklad.setPrihod(0);
            sklad.setRashod(product.getCount());            
            new SkladDAO().updateSklad(sklad);
        }
    }
    public void updateSkladArrival(List<ArrivalProduct> products, Document doc) { 
        Sklad sklad;
        Date date = new Date();        
        for (DocumentProduct product : products) {
            sklad = new Sklad();
            sklad.setDoc(doc.getId());
            sklad.setIndate(date);
            sklad.setProduct(product.getProduct());
            sklad.setDocuments(doc.getDocuments());
            sklad.setCost(product.getCost());
            sklad.setPrihod(product.getCount());
            sklad.setRashod(0);            
            new SkladDAO().updateSklad(sklad);
        }
    }    
    public void updateSkladDeduct(List<DeductProduct> products, Document doc) { 
        Sklad sklad;
        Date date = new Date();        
        for (DocumentProduct product : products) {
            sklad = new Sklad();
            sklad.setDoc(doc.getId());
            sklad.setIndate(date);
            sklad.setProduct(product.getProduct());
            sklad.setDocuments(doc.getDocuments());
            sklad.setCost(product.getCost());
            sklad.setPrihod(0);
            sklad.setRashod(product.getCount());            
            new SkladDAO().updateSklad(sklad);
        }
    }   
    public void updateSkladReceipt(List<ReceiptProduct> products, Document doc) { 
        Sklad sklad;
        Date date = new Date();        
        for (DocumentProduct product : products) {
            sklad = new Sklad();
            sklad.setDoc(doc.getId());
            sklad.setIndate(date);
            sklad.setProduct(product.getProduct());
            sklad.setDocuments(doc.getDocuments());
            sklad.setCost(product.getCost());
            sklad.setPrihod(0);
            sklad.setRashod(product.getCount());            
            new SkladDAO().updateSklad(sklad);
        }
    }     
    // ******************************
    // запись в кассу
    public void updateKassa(Document doc, boolean isDebt) {
          Kassa kassa = new Kassa();
          kassa.setDoc_id(doc.getId());
          kassa.setDocumenttype(doc.getDocuments());
          kassa.setIndate(doc.getIndate());
          if (isDebt) {
            kassa.setCredit(0);
            kassa.setDebt(doc.getTotal()); 
          } else {
              kassa.setCredit(doc.getTotal()); 
              kassa.setDebt(0);
          }
            try {
                new DocumentDAO().updateKassa(kassa);
            } catch (Exception ex) {
                Logger.getLogger(Object.class.getName()).log(Level.SEVERE, null, ex);
            }         
    }
    
    }
    
