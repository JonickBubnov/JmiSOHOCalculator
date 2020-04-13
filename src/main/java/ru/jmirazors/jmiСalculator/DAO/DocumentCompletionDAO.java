/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiСalculator.entity.Arrival;
import ru.jmirazors.jmiСalculator.entity.ArrivalProduct;
import ru.jmirazors.jmiСalculator.entity.Deduct;
import ru.jmirazors.jmiСalculator.entity.DeductProduct;
import ru.jmirazors.jmiСalculator.entity.Document;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.Invoice;
import ru.jmirazors.jmiСalculator.entity.Kassa;
import ru.jmirazors.jmiСalculator.entity.Offer;
import ru.jmirazors.jmiСalculator.entity.OfferProduct;
import ru.jmirazors.jmiСalculator.entity.Pay;
import ru.jmirazors.jmiСalculator.entity.Price;
import ru.jmirazors.jmiСalculator.entity.Receipt;
import ru.jmirazors.jmiСalculator.entity.ReceiptProduct;
import ru.jmirazors.jmiСalculator.entity.Sale;
import ru.jmirazors.jmiСalculator.entity.SaleProduct;
import ru.jmirazors.jmiСalculator.entity.SetPrice;
import ru.jmirazors.jmiСalculator.entity.SetPriceProduct;
import ru.jmirazors.jmiСalculator.entity.Sklad;
import ru.jmirazors.jmiСalculator.entity.Stock;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.Subordin;
import ru.jmirazors.jmiСalculator.entity.Transfer;
import ru.jmirazors.jmiСalculator.entity.TransferProduct;
import ru.jmirazors.jmiСalculator.jmiframes.MainFrame;

/**
 *
 * @author User
 */


public class DocumentCompletionDAO extends DAO {

    
    //IFBean ifBean = new IFBean();
    DocumentUtil docUtil =  new DocumentUtil();   
    
    public void completion(Sale document, List<SaleProduct> products) {
            try { 
                ArrayList<DocumentProduct> pr = new ArrayList<>();
                pr.clear(); 
                pr.addAll(products);
                
                updateSklad(document, pr, false);
                updateStock(document.getStorage(), pr, false);
                updateKassa(document, false);
                
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}            
        MainFrame.ifManager.infoMessage("Исполнение документа 'Розничная продажа' №"+document.getId()+" [OK]");
    }
    
    public void completion(Invoice document) {
        MainFrame.ifManager.infoMessage("Исполнение документа 'Заказ покупателя' №"+document.getId()+" [OK]");        
    }
    
    public void completion(Transfer transfer, ArrayList<TransferProduct> products) {
        ArrayList<DocumentProduct> pr = new ArrayList<>();
        pr.clear(); 
        pr.addAll(products);
        
        updateStock(transfer.getStorage_from(), pr, false);
        updateStock(transfer.getStorage_to(), pr, true);
        
        MainFrame.ifManager.infoMessage("Исполнение документа 'Перемещение товаров' №"+transfer.getId()+" [OK]");
    }
    
    public void completion(SetPrice document, List<SetPriceProduct> products) {
            PriceDAO pDAO = new PriceDAO();
            Price price;
            
            for (int i = 0; i < products.size(); i++) {
                price = new Price();
                price.setIndate(document.getIndate());
                price.setPrice(products.get(i).getPrice());
                price.setPricename(document.getPriceName());
                price.setProduct(products.get(i).getProduct());
            try {
                pDAO.create(price);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}             
        }
        MainFrame.ifManager.infoMessage("Исполнение документа 'Установка цен номенклатуры' №"+document.getId()+" [OK]");
    }
    
    public void completion(Deduct document, List<DeductProduct> products) {

            try {
                ArrayList<DocumentProduct> pr = new ArrayList<>();
                pr.clear(); 
                pr.addAll(products);
                
                updateSklad(document, pr, false);
                updateStock(document.getStorage(), pr, false);
                //updateKassa(document, true);                

            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
               
        MainFrame.ifManager.infoMessage("Исполнение документа 'Списание товаров' №"+document.getId()+" [OK]");
    }
    
    public void completion(Receipt document, List<ReceiptProduct> products) {           
            
            try {
                ArrayList<DocumentProduct> pr = new ArrayList<>();
                pr.clear(); 
                pr.addAll(products);
                
                updateSklad(document, pr, true);
                updateStock(document.getStorage(), pr, true);
                //updateKassa(document, true);  
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
              
        MainFrame.ifManager.infoMessage("Исполнение документа 'Оприходование товаров' №"+document.getId()+" [OK]");
    }

    public void completion(Offer document, List<OfferProduct> products) {
            
            try {
                ArrayList<DocumentProduct> pr = new ArrayList<>();
                pr.clear(); 
                pr.addAll(products);
                
                updateSklad(document, pr, false);
                updateStock(document.getStorage(), pr, false);
                updateKassa(document, false);  
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        
        MainFrame.ifManager.infoMessage("Исполнение документа 'Реализация товаров' №"+document.getId()+" [OK]");            
    }
    
    public void completion(Arrival document, List<ArrivalProduct> products) {
        
            try {
                ArrayList<DocumentProduct> pr = new ArrayList<>();
                pr.clear(); 
                pr.addAll(products);
                
                updateSklad(document, pr, true);
                updateStock(document.getStorage(), pr, true);
                updateKassa(document, true);  
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "[DocumentCompletionDAO]\nНе удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        MainFrame.ifManager.infoMessage("Исполнение документа 'Поступление товаров' №"+document.getId()+" [OK]");            
    }
    
    public void completion(Pay pay, Document parentDocument) {
//        PayDAO payDAO = new PayDAO();
        Subordin sub;
        
        new PayDAO().updatePay(pay);
        
        if (parentDocument != null) {
            sub = new Subordin();
            sub.setMain_id(parentDocument.getId());
            sub.setMaindoc_id(parentDocument.getDocuments().getId());
            sub.setSub_id(pay.getId());
            sub.setSubdoc_id(8L);
            try {
                new SubordinDAO().saveSub(sub);        
            } catch (Exception ex) {MainFrame.ifManager.infoMessage("Ошибка! Не удалось сохранить подчиненные документы");}
        }
                
            if (pay.getDebcr() == 0) {
                updateKassa(pay, true);
            } else {
                updateKassa(pay, false);
            }            
        MainFrame.ifManager.infoMessage("Исполнение документа 'Оплата' №"+pay.getId()+" [OK]"); 
    }
    
    // *************************************************************************
    // запись в кассу
    // *************************************************************************
    public void updateKassa(Document doc, boolean isDebt) {
          Kassa kassa = new Kassa();
          kassa.setDocNumber(doc.getId());
          kassa.setDocumenttype(doc.getDocuments());
          kassa.setIndate(doc.getIndate());
          kassa.setOrganization(doc.getOrganization());
          kassa.setContragent(doc.getContragent());
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
    // *************************************************************************
    // запись движения товаров по складу
    // *************************************************************************
    public void updateSklad(Document doc, ArrayList<DocumentProduct> products, boolean income) {
        if (!products.isEmpty()) {
            Sklad sklad;
            SkladDAO sDAO = new SkladDAO();
            for (DocumentProduct product: products) {
                sklad = new Sklad();
                    sklad.setProduct(product.getProduct());
                    sklad.setIndate(doc.getIndate());
                    sklad.setCost(docUtil.getCostWhithDiscount(product));
                    sklad.setDocuments(doc.getDocuments());
                    sklad.setDoc(doc.getId());
                    if (income) { 
                        sklad.setPrihod(product.getCount());
                        sklad.setRashod(0);
                    }
                    else {
                        sklad.setRashod(product.getCount());
                        sklad.setPrihod(0);
                    }
                sDAO.updateSklad(sklad);
            }
        }
    }
    // *************************************************************************
    // добавить-удалить товары со склада
    // *************************************************************************
    public void updateStock(Storage storage, List<DocumentProduct> products, boolean income) {
        Stock stock = null;
        StockDAO sDAO = new StockDAO();
        float count = 0F;
        for (DocumentProduct product: products) {           
            stock = sDAO.findStock(storage, product.getProduct()); 
            if (stock == null) {                    
                stock = new Stock();
                stock.setStorage(storage);
                stock.setProduct(product.getProduct());
                stock.setCount(0);
                }
                if (income)
                    count = stock.getCount() + product.getCount();
                else
                    count = stock.getCount() - product.getCount();
                stock.setCount(count);             
                
                sDAO.updateStock(stock);
        }
        
    }
        
    }
    
