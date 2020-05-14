/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator;

import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author User
 */
public class SelectDatabaseDialog extends javax.swing.JDialog {

    /**
     * Creates new form SelectDatabaseDialog
     */
    HashMap<String, String> props = new HashMap();
    HashMap<String, String> params = new HashMap();
    String prefix = "";
    
    public SelectDatabaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
        readProperties();
    }
    
    HashMap<String, String> getSelectedParams(String pr) {
        for (String val: props.keySet()) {
            if (val.startsWith(pr))
                params.put(val, props.get(val));
        }
        return params;
    }
    
    String getDBName(String val) {
        for (String v : props.keySet()) {
            if (props.get(v).equals(val))
                prefix = v.substring(0, v.indexOf("*"));
        }
        return prefix;
    }    

    void readProperties() {
                
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
                    jComboBox1.addItem(props.get(val));                    
            }
            inputStream.close();
        } catch (IOException e) {}
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("База данных");
        setResizable(false);

        jLabel1.setText("Выберите базу данных");

        jComboBox1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jComboBox1.setFocusable(false);
        jComboBox1.setPreferredSize(new java.awt.Dimension(33, 24));

        jButton1.setText("Отмена");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 88, 102)));
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new java.awt.Dimension(80, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ок");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 88, 102)));
        jButton2.setFocusPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(80, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 120, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
        System.exit(0);        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        prefix = getDBName(jComboBox1.getSelectedItem().toString());
        MainFrame.sessionParams.setDbdialect(props.get(prefix+"*hibernate.dialect"));
        MainFrame.sessionParams.setDbdriver(props.get(prefix+"*hibernate.connection.driver_class"));
        MainFrame.sessionParams.setDbname(props.get(prefix+"*hdbname"));
        MainFrame.sessionParams.setDburl(props.get(prefix+"*hibernate.connection.url"));
        MainFrame.sessionParams.setDbusername(props.get(prefix+"*hibernate.connection.username"));
        MainFrame.sessionParams.setDbpass(props.get(prefix+"*hibernate.connection.password"));
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
