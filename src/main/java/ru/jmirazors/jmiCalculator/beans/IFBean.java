/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import ru.jmirazors.jmiСalculator.jmiframes.MainFrame;

/**
 *
 * @author User
 */
public class IFBean {              
    
    private boolean organization = false;
    private boolean users = false;
    private boolean storage = false;
    private boolean units = false;
    private boolean contragents = false;
    private boolean products = false;
    private boolean priceName = false;
    private boolean loyalty = false;
    
    private boolean invoiceList = false;
    private boolean docInvoice = false;
    
    private boolean arrivalList = false;
    private boolean docArrival = false;
    
    private boolean offerList = false;
    private boolean docOffer = false;
    
    private boolean transferList = false;
    private boolean docTransfer = false;
    
    private boolean setPriceList = false;
    private boolean docSetPrice = false;
    
    private boolean billList = false;
    
    private boolean payList = false;
    private boolean docPay = false;
    
    private boolean deductList = false;
    private boolean docDeduct = false;
    
    private boolean receiptList = false;
    private boolean docReceipt = false;
    
    private boolean actList = false;
    private boolean docAct = false;  
    
    private boolean saleList = false;
    private boolean docSale = false;
    
    private boolean inventoryList = false;
    private boolean docInventory = false;
    
    private boolean parameters = false;
    
    JDesktopPane jDesktopPane;
    
    public IFBean(JDesktopPane jDesktopPane) {
        this.jDesktopPane = jDesktopPane;
    }
                    
    
    
    JTextArea infoPanel = MainFrame.infoPanel;
    
    public void infoMessage(String text) {
        infoPanel.append(text+"\n");
    }
    
    // -------------- дисконтные карты -------------------
    public boolean isLoyaltyOpen() {
        return loyalty;
    }
    public void setLoyaltyOpen(boolean val) {
        loyalty = val;
    }
    // --------------  фрейм настройки -------------------
    public boolean isParametersOpen() {
        return parameters;
    }    
    public void setParametersFrameOpen(boolean val) {
        parameters = val;
    }
    // --------------  фрейм организации -------------------
    public boolean isOrganizationOpen() {
        return organization;
    }    
    public void setOrganizationFrameOpen(boolean val) {
        organization = val;
    }
    // --------------- фрейм пользователи --------------------
    public boolean isUsersOpen() {
        return users;
    }    
    public void setUsersFrameOpen(boolean val) {
        users = val;
    }
    // ------------- фрейм склады ---------------------------
    public boolean isStoragesOpen() {
        return storage;
    }    
    public void setStoragesFrameOpen(boolean val) {
        storage = val;
    }
    // ----------------  фрейм еденицы измерения ---------------------------
    public boolean isUnitsOpen() {
        return units;
    }    
    public void setUnitsFrameOpen(boolean val) {
        units = val;
    } 
    // ------------  фрейм контрагенты --------------------
    public boolean isContragentsOpen() {
        return contragents;
    }    
    public void setContragentsFrameOpen(boolean val) {
        contragents = val;
    } 
    // -------------- фрейм номенклатура --------------------
    public boolean isProductsOpen() {
        return products;
    }    
    public void setProductsFrameOpen(boolean val) {
        products = val;
    }
    // --------------  фрейм типы цен ------------------------
    public boolean isPriceNameOpen() {
        return priceName;
    }    
    public void setPriceNameFrameOpen(boolean val) {
        priceName = val;
    }
    // --------------  список заказы покупателей ---------------
    public boolean isInvoiceListOpen() {
        return invoiceList;
    }    
    public void setInvoiceListFrameOpen(boolean val) {
        invoiceList = val;
    }
    // ------------  список поступление товаров ----------------
    public boolean isArrivalListOpen() {
        return arrivalList;
    }    
    public void setArrivalListFrameOpen(boolean val) {
        arrivalList = val;
    }
    // ------------  список реализация товаров --------------------
    public boolean isOfferListOpen() {
        return offerList;
    }   
    public void setOfferListFrameOpen(boolean val) {
        offerList = val;
    }
    // ------------- список перемещение товаров -----------------
    public boolean isTransferListOpen() {
        return transferList;
    }    
    public void setTransferListFrameOpen(boolean val) {
        transferList = val;
    }
    // -------------- список установка цен номенклатуры ---------------
    public boolean isSetPriceListOpen() {
        return setPriceList;
    }    
    public void setSetPriceListFrameOpen(boolean val) {
        setPriceList = val;
    }   
    // -------------  список счета на оплату ----------------------
    public boolean isBillListOpen() {
        return billList;
    }    
    public void setBillListFrameOpen(boolean val) {
        billList = val;
    }
    // ----------------- список оплаты ----------------------
    public boolean isPayListOpen() {
        return payList;
    }    
    public void setPayListFrameOpen(boolean val) {
        payList = val;
    }   
    // ------------ список списание товаров ------------------
    public boolean isDeductListOpen() {
        return deductList;
    }    
    public void setDeductListFrameOpen(boolean val) {
        deductList = val;
    }  
    // ------------ список акты ------------------
    public boolean isActListOpen() {
        return actList;
    }    
    public void setActListFrameOpen(boolean val) {
        actList = val;
    }     
    // ------------- список оприходование товаров ---------------
    public boolean isReceiptListOpen() {
        return receiptList;
    }        
    public void setReceiptListFrameOpen(boolean val) {
        receiptList = val;
    }
    // ------------- список розничные продажи ---------------
    public boolean isSaleListOpen() {
        return saleList;
    }        
    public void setSaleListFrameOpen(boolean val) {
        saleList = val;
    }    
    // ------------- список инвентаризация товаров ---------------
    public boolean isInventoryListOpen() {
        return inventoryList;
    }        
    public void setInventoryListFrameOpen(boolean val) {
        inventoryList = val;
    }       
    // ---------- документ заказ покупателя -------------------
    public void setDocInvoiceFrameOpen(boolean val) {
        docInvoice = val;
    }
    public boolean isDocInvoiceFrameOpen() {
        return docInvoice;
    }
    // -------------- документ установка цен номенклатуры --------
    public void setDocSetPriceFrameOpen(boolean val) {
        docSetPrice = val;
    }
    public boolean isDocSetPriceFrameOpen() {
        return docSetPrice;
    }
    // --------------- документ списание товаров ---------------
    public void setDocDeductFrameOpen(boolean val) {
        docDeduct = val;
    }
    public boolean isDocDeductFrameOpen() {
        return docDeduct;
    }    
    // --------------- документ оприходование товаров ---------------
    public void setDocReceiptFrameOpen(boolean val) {
        docReceipt = val;
    }
    public boolean isDocReceiptFrameOpen() {
        return docReceipt;
    }  
    // --------------- документ реализация товаров ---------------
    public void setDocOfferFrameOpen(boolean val) {
        docOffer = val;
    }
    public boolean isDocOfferFrameOpen() {
        return docOffer;
    }   
    // --------------- документ инвентаризация товаров ---------------
    public void setDocInventoryFrameOpen(boolean val) {
        docInventory = val;
    }
    public boolean isDocInventoryFrameOpen() {
        return docInventory;
    }      
    // --------------- документ поступление товаров ---------------
    public void setDocArrivalFrameOpen(boolean val) {
        docArrival = val;
    }
    public boolean isDocArrivalFrameOpen() {
        return docArrival;
    }  
    // --------------- документ перемещение товаров ---------------
    public void setDocTransferFrameOpen(boolean val) {
        docTransfer = val;
    }
    public boolean isDocTransferFrameOpen() {
        return docTransfer;
    }    
    // --------------- документ продажа ---------------
    public void setDocSaleFrameOpen(boolean val) {
        docSale = val;
    }
    public boolean isDocSaleFrameOpen() {
        return docSale;
    }     
    // --------------- документ оплата ---------------
    public void setDocPayFrameOpen(boolean val) {
        docPay = val;
    }
    public boolean isDocPayFrameOpen() {
        return docPay;
    } 
    
    public void showFrame(JInternalFrame frame, boolean maximize) {
            jDesktopPane.add(frame);
            if (maximize)
                jDesktopPane.getDesktopManager().maximizeFrame(frame);
            frame.setVisible(true);         
    }
    
}
