/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.jmirazors.jmiСalculator.jmiframes.MainFrame;

/**
 *
 * @author User
 */
public class ListUtil {
    
    
    public void writeProperties(String listName, Map<String, String> properties) {
        
        Properties prop = new Properties();
            for (String key :properties.keySet())
                prop.put(key, properties.get(key));

        String propFileName = "config//"+listName+".properties";
        try {         
            File propFile =  new File(propFileName);
            FileOutputStream outputStream = new FileOutputStream(propFile);
            prop.store(outputStream, "ListProperties");
            outputStream.close();
            MainFrame.ifManager.infoMessage("Настройки для списка сохранены.- ОК.");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, 
                "Не удалось записать файл. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, 
                "Не удалось записать файл. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }    
    }
    
    public Map readProperties(String listName) {
        Map<String, String> properties = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config//"+listName+".properties";
            InputStream inputStream = new FileInputStream(propFileName);            
            if (inputStream != null)
                prop.load(inputStream);
            properties = new HashMap<>();
            for (String key :prop.stringPropertyNames())
                properties.put(key, prop.getProperty(key));
            properties.put("organization", MainFrame.sessionParams.getOrganization().getName());
            
            inputStream.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, 
                "Не удалось прочитать файл. \n" + ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }  
        return properties;
    }
    
    public void saveXLS(JTable table, File fileToSave) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("LIST");        
        
        int colCount = table.getColumnCount();
        int rowCount = table.getRowCount();
        Row row = sheet.createRow(0);
        Cell cell;
        for (int i = 1; i < colCount; i++) {
            cell = row.createCell(i);
            cell.setCellValue(table.getColumnName(i));
            sheet.autoSizeColumn(i);
        }       
        
        for (int j = 1; j <= rowCount; j++) {
            row = sheet.createRow(j);
        for (int i = 1; i < colCount; i++) {
            cell = row.createCell(i);
            cell.setCellValue(table.getValueAt(j-1, i).toString());
        }
        }
        
//        Row row = sheet.createRow(0);
//        Cell name = row.createCell(0);
//        name.setCellValue("John");
//        sheet.autoSizeColumn(1);
        try {
            workbook.write(new FileOutputStream(fileToSave));
            workbook.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ListUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
