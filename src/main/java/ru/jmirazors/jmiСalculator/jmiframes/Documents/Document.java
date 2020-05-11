/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.jmirazors.jmiCalculator.beans.DocumentUtil;
import ru.jmirazors.jmiCalculator.beans.SessionParams;
import ru.jmirazors.jmiСalculator.DAO.SubordinDAO;
import ru.jmirazors.jmiCalculator.MainFrame;
import ru.jmirazors.jmiСalculator.DAO.DocumentDAO;
import ru.jmirazors.jmiСalculator.entity.Contragent;
import ru.jmirazors.jmiСalculator.entity.Department;
import ru.jmirazors.jmiСalculator.entity.DocStatus;
import ru.jmirazors.jmiСalculator.entity.DocumentProduct;
import ru.jmirazors.jmiСalculator.entity.DocumentType;
import ru.jmirazors.jmiСalculator.entity.Organization;
import ru.jmirazors.jmiСalculator.entity.PriceName;
import ru.jmirazors.jmiСalculator.entity.Storage;
import ru.jmirazors.jmiСalculator.entity.Subordin;
import ru.jmirazors.jmiСalculator.entity.User;
import ru.jmirazors.jmiСalculator.jmiframes.SubordinDocDialog;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.ContragentSelectDialog;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.DepartmentSelectDialog;
import ru.jmirazors.jmiСalculator.jmiframes.selectDialogs.UserSelectNameDialog;

/**
 *
 * @author Jonick
 */
public class Document {
    private long id;
    private Date indate;
    private DocStatus status;
    private Contragent contragent;
    private float total;
    private String descr;
    private User usr;
    private Storage storage;
    private PriceName priceName;
    private List<DocumentProduct> documentProducts;
    private DocumentType documents;
    private Organization organization;
    private final SessionParams parameters = MainFrame.sessionParams;
    private Department department;
    private float weight;
    private int discount;
    private String title;
    
    DocumentDAO documentDAO = new DocumentDAO();

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public DocStatus getStatus() {
        return status;
    }

    public void setStatus(DocStatus status) {
        this.status = status;
    }

    public Contragent getContragent() {
        return contragent;
    }

    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }
    
    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public User getUsr() {
        return usr;
    }

    public void setUsr(User user) {
        this.usr = user;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    } 

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle(Document doc) {        
        return title+ " ["+doc.getStatus().getName()+"]";
    }
    
    
    public List<DocumentProduct> getDocumentProducts() {
        return documentProducts;
    }

    public void setDocumentProducts(List<DocumentProduct> documentProducts) {
        if (this.documentProducts != null) {
            this.documentProducts.clear();
            this.documentProducts.addAll(documentProducts);
        } else
            this.documentProducts = documentProducts;
    }  

    public DocumentType getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentType documents) {
        this.documents = documents;
    }

    public PriceName getPriceName() {
        return priceName;
    }

    public void setPriceName(PriceName priceName) {
        this.priceName = priceName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    public String getDocumentName() {
        return getDocuments().getName();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
    /**
     *
     * @return
     */
    public Class getDocumentClass() {
        return new DocumentUtil().getDocumentClass(getDocuments().getId());
    }
    
    public boolean isSaved() {
        return getId() != 0;
    }
    
    public User getSessionUser() {
        return parameters.getUser();
    }
    public Organization getSessionOrganization() {
        return parameters.getOrganization();
    }
    
    // показать подчиненные документы
    public void showSubordins() {
        try {           
            List<Subordin> subdocs_l1 = new SubordinDAO().list(getId(), getDocuments().getId());
            if (subdocs_l1 == null || subdocs_l1.isEmpty())
                MainFrame.ifManager.showMessageDialog(null, "Сообщение", "Нет подчиненных документов");
            else
                new SubordinDocDialog(null, true, subdocs_l1).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // инициализировать документ
    public void init(Long docType) {
        setDocuments(documentDAO.getDocumentType(docType));
        setOrganization(getSessionOrganization());
        setDepartment(getSessionUser().getDepartment());
        setUsr(getSessionUser());
        setWeight(0);
        setDiscount(0);
        setIndate(new Date());
        setTotal(0);
        setStatus(documentDAO.getStatus(1L));
        setDescr("");
        setTitle(documentDAO.getDocumentType(docType).getName());
    }
    // посчитать сумму документа
    public float getSum(List prods, int discount) {
        ArrayList<DocumentProduct> products = (ArrayList<DocumentProduct>) prods;
        float sum = 0;
        for (DocumentProduct product : products) {
            sum += (product.getCost()-product.getCost()*product.getDiscount()/100)*product.getCount();
        }
        return sum;
    }
    
    // диалог подразделения
    public void showDepartmentDialog(Document doc) {
        DepartmentSelectDialog dsd = new DepartmentSelectDialog(null, true, doc);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - dsd.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - dsd.getHeight()) / 2);
        dsd.setLocation(x, y);
        dsd.setVisible(true);        
    }
    // Диалог контрагенты
    public void showContragentDialog(Document doc) {
        ContragentSelectDialog csd = new ContragentSelectDialog(null, true, doc);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - csd.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - csd.getHeight()) / 2);
        csd.setLocation(x, y);
        csd.setVisible(true);        
    }
    //диалог пользователи
    public void showUserDialog(Document doc) {
        UserSelectNameDialog usnd = new UserSelectNameDialog(null, true, doc);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - usnd.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - usnd.getHeight()) / 2);
        usnd.setLocation(x, y);
        usnd.setVisible(true);         
    }
    // форматированный номер документа
    public String getFormattedID(long id) {
        StringBuilder sbuf = new StringBuilder();
        Formatter fmt = new Formatter(sbuf);
        fmt.format("%06d", id);
        return sbuf.toString();
    }

}
