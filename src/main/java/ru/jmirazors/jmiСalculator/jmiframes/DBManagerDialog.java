/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author JOnick
 */
public class DBManagerDialog extends javax.swing.JDialog {

    /**
     * Creates new form DBManagerDialog
     */
    DefaultListModel<String> model = new DefaultListModel<>();
    HashMap<String, String> props = new HashMap();
    String prefix = "";
    
    public DBManagerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        readProperties();
    }
    
    void readProperties() {
        model.removeAllElements();
        props.clear();      
        Properties prop = new Properties();
        String propFileName = "config//db.properties";
        try {
            InputStream inputStream = new FileInputStream(propFileName);            
            if (inputStream != null)
                prop.load(inputStream);
            for (String key :prop.stringPropertyNames()) {
                props.put(key, prop.getProperty(key));                 
            }
            for (String val: props.keySet()) {
                if (val.endsWith("hdbname"))
                   model.addElement(props.get(val));
            }
            inputStream.close();
        } catch (IOException e) {}
    }
    
    String getDBName(String val) {
        for (String v : props.keySet()) {
            if (props.get(v).equals(val))
                prefix = v.substring(0, v.indexOf("*"));
        }
        return prefix;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Менеджер баз данных");
        setResizable(false);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 45));

        jButton6.setText("Закрыть");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setText("____");

        jLabel2.setText("DB\\\\");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                    .addComponent(jButton6)
                    .addContainerGap())
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(7, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addContainerGap())
            );

            getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

            jList1.setModel(model);
            jList1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jList1MouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(jList1);

            getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

            jToolBar1.setFloatable(false);
            jToolBar1.setRollover(true);

            jButton1.setText("доб");
            jButton1.setToolTipText("Добавить новую базу");
            jButton1.setFocusable(false);
            jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
            jToolBar1.add(jButton1);

            jButton7.setText("созд");
            jButton7.setToolTipText("Создать пустую базу данных");
            jButton7.setFocusable(false);
            jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jToolBar1.add(jButton7);

            jButton2.setText("удал");
            jButton2.setToolTipText("Удалить базу");
            jButton2.setFocusable(false);
            jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
            jToolBar1.add(jButton2);

            jButton3.setText("арх");
            jButton3.setToolTipText("Архивировать базу данных");
            jButton3.setFocusable(false);
            jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jToolBar1.add(jButton3);

            jButton4.setText("очист");
            jButton4.setToolTipText("Очистить базу данных");
            jButton4.setFocusable(false);
            jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jToolBar1.add(jButton4);

            jButton5.setText("тест");
            jButton5.setToolTipText("Тест базы данных");
            jButton5.setFocusable(false);
            jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jToolBar1.add(jButton5);

            getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Создать новую базу
        NewDatabaseDialog ndbd = new NewDatabaseDialog(null, true);
        ndbd.setVisible(true);
        readProperties();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        jLabel1.setText(getDBName(jList1.getSelectedValue()));
        if (evt.getClickCount() == 2) {
            NewDatabaseDialog ndbd = new NewDatabaseDialog(null, true, prefix, props);
            ndbd.setLocationRelativeTo(this);
            ndbd.setVisible(true);
            readProperties();
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Set s = props.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
 
                String el = (String) it.next();
                System.out.println(el);
                if (el.startsWith(prefix)) { 
                    System.out.println("[delete] " + el);
                    props.remove(el);
                    }                
            }
        System.out.println("----------------------------------");
        for (String st: props.keySet()) {
            System.out.println(st);
        }
                
        String propFileName = "config//db.properties";        
        File propFile =  new File(propFileName);        
        try {        
            FileOutputStream outputStream = new FileOutputStream(propFile);
            Properties p = new Properties();
            p.putAll(props);
            p.store(outputStream, "Databases Configuration");
            outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        readProperties();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
