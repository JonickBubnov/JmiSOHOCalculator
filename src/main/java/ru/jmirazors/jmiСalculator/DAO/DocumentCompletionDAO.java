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
import ru.jmirazors.jmiСalculator.entity.SetPrice;
import ru.jmirazors.jmiСalculator.entity.SetPriceProduct;
import ru.jmirazors.jmiСalculator.entity.Sklad;
import ru.jmirazors.jmiСalculator.entity.Stock;
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
    
    public void completion(Invoice document) {
        MainFrame.ifManager.infoMessage("Исполнение документа 'Заказ покупателя' №"+document.getId()+" [OK]");        
    }
    
    public void completion(Transfer transfer, ArrayList<TransferProduct> products) {
        List<DocumentProduct> dp = (List<DocumentProduct>)products.clone();
        System.out.println("SIZE of products = " + products.size());
        docUtil.stockMinus(dp, transfer.getStorage_from());
        docUtil.stockPlus(dp, transfer.getStorage_to());
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
                JOptionPane.showMessageDialog(null, "Не удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}             
        }
        MainFrame.ifManager.infoMessage("Исполнение документа 'Установка цен номенклатуры' №"+document.getId()+" [OK]");
    }
    
    public void completion(Deduct document, List<DeductProduct> products) {
        Stock stock;
        StockDAO stDAO = new StockDAO();
        Sklad sklad;
        SkladDAO skDAO = new SkladDAO();                
        
        for (int i = 0; i < products.size(); i++) {
            sklad = new Sklad();
            sklad.setProduct(products.get(i).getProduct());
            sklad.setIndate(document.getIndate());
            sklad.setRashod(products.get(i).getCount());
            sklad.setCost(products.get(i).getCost());
            sklad.setDoc(document.getId());
            sklad.setDocuments(document.getDocuments());
            
            float count = 0f;
            stock = stDAO.findStock(document.getStorage(), products.get(i).getProduct());
            if (products.get(i).getProduct().getOnstock() == 1) {
                if (stock == null) {                    
                    stock = new Stock();
                    stock.setStorage(document.getStorage());
                    stock.setProduct(products.get(i).getProduct());
                    stock.setCount(0);
                }
                count = stock.getCount() - products.get(i).getCount();
                stock.setCount(count);
            }            
            
            try {
                skDAO.updateSklad(sklad);
                if (stock != null)
                    stDAO.updateStock(stock);
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "Не удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        }        
        MainFrame.ifManager.infoMessage("Исполнение документа 'Списание товаров' №"+document.getId()+" [OK]");
    }
    
    public void completion(Receipt document, List<ReceiptProduct> products) {
        Stock stock;
        StockDAO stDAO = new StockDAO();
        Sklad sklad;
        SkladDAO skDAO = new SkladDAO();
        for (int i = 0; i < products.size(); i++) {
            sklad = new Sklad();
            sklad.setProduct(products.get(i).getProduct());
            sklad.setIndate(document.getIndate());
            sklad.setPrihod(products.get(i).getCount());
            sklad.setCost(products.get(i).getCost());
            sklad.setDoc(document.getId());
            sklad.setDocuments(document.getDocuments());
            
            float count = 0f;
            stock = stDAO.findStock(document.getStorage(), products.get(i).getProduct());
            if (products.get(i).getProduct().getOnstock() == 1) {
                if (stock == null) {                    
                    stock = new Stock();
                    stock.setStorage(document.getStorage());
                    stock.setProduct(products.get(i).getProduct());
                    stock.setCount(0);
                }
                count = stock.getCount() + products.get(i).getCount();
                stock.setCount(count);
            }            
            
            try {
                skDAO.updateSklad(sklad);
                if (stock != null)
                    stDAO.updateStock(stock);
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "Не удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        }        
        MainFrame.ifManager.infoMessage("Исполнение документа 'Оприходование товаров' №"+document.getId()+" [OK]");
    }

    public void completion(Offer document, List<OfferProduct> products) {
        Stock stock;
        StockDAO stDAO = new StockDAO();
        Sklad sklad;
        SkladDAO skDAO = new SkladDAO();

        for (int i = 0; i < products.size(); i++) {
            sklad = new Sklad();
            sklad.setProduct(products.get(i).getProduct());
            sklad.setIndate(document.getIndate());
            sklad.setRashod(products.get(i).getCount());
            sklad.setCost(products.get(i).getCost());
            sklad.setDoc(document.getId());
            sklad.setDocuments(document.getDocuments());
            
            float count = 0f;
            stock = stDAO.findStock(document.getStorage(), products.get(i).getProduct());
            if (products.get(i).getProduct().getOnstock() == 1) {
                if (stock == null) {                    
                    stock = new Stock();
                    stock.setStorage(document.getStorage());
                    stock.setProduct(products.get(i).getProduct());
                    stock.setCount(0);
                }
                count = stock.getCount() - products.get(i).getCount();
                stock.setCount(count);                    
                }
            
            try {
                skDAO.updateSklad(sklad);
                if (stock != null)
                    stDAO.updateStock(stock);
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "Не удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        }
        updateKassa(document, true);
        MainFrame.ifManager.infoMessage("Исполнение документа 'Реализация товаров' №"+document.getId()+" [OK]");            
    }
    
    public void completion(Arrival document, List<ArrivalProduct> products) {
        Stock stock;
        StockDAO stDAO = new StockDAO();
        Sklad sklad;
        SkladDAO skDAO = new SkladDAO();

        for (int i = 0; i < products.size(); i++) {
            sklad = new Sklad();
            sklad.setProduct(products.get(i).getProduct());
            sklad.setIndate(document.getIndate());
            sklad.setPrihod(products.get(i).getCount());
            sklad.setCost(products.get(i).getCost());
            sklad.setDoc(document.getId());
            sklad.setDocuments(document.getDocuments());
            
            float count = 0f;
            stock = stDAO.findStock(document.getStorage(), products.get(i).getProduct());
            if (products.get(i).getProduct().getOnstock() == 1) {
                if (stock == null) {                    
                    stock = new Stock();
                    stock.setStorage(document.getStorage());
                    stock.setProduct(products.get(i).getProduct());
                    stock.setCount(0);
                }
                count = stock.getCount() + products.get(i).getCount();
                stock.setCount(count);                    
                }
            
            try {
                skDAO.updateSklad(sklad);
                if (stock != null)
                    stDAO.updateStock(stock);
            } catch (Exception ex) {JOptionPane.showMessageDialog(null, "Не удалось выполнить документ \n" + ex, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);}
        }
        updateKassa(document, false);
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
    
    // **************************************************************
    // запись в кассу
    public void updateKassa(Document doc, boolean isDebt) {
          Kassa kassa = new Kassa();
          kassa.setDoc_id(doc.getId());
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
}
