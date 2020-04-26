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
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class NewDatabaseDialog extends javax.swing.JDialog {

    /**
     * Creates new form NewDatabaseDialog
     */
    String curl = "";
    
    public NewDatabaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        renewDialect();
    }
    
    public NewDatabaseDialog(java.awt.Frame parent, boolean modal, String dbname, Map<String, String> prop) {
        super(parent, modal);
        initComponents();
        
        renewDialect();
                          
        jComboBox2.setSelectedItem(prop.get(dbname+"*hibernate.connection.driver_class"));
        jTextField1.setText(prop.get(dbname+"*hdbname"));
        jTextField3.setText(dbname);
        jTextField2.setText(parseServer(prop.get(dbname+"*hibernate.connection.url")));
        jFormattedTextField1.setText(parsePort(prop.get(dbname+"*hibernate.connection.url")));
        jComboBox1.setSelectedItem(prop.get(dbname+"*hibernate.dialect"));
        jTextField4.setText(prop.get(dbname+"*hibernate.connection.username"));
        jPasswordField1.setText(prop.get(dbname+"*hibernate.connection.password"));
    }

    String parseServer(String val){
        int start = val.indexOf(":", 6)+3;
        int end = val.indexOf(":", start);
        return val.substring(start, end);
    }
    String parsePort(String val) {
        int start1 = val.indexOf(":", 6)+3;
        int start = val.indexOf(":", start1)+1;
        return val.substring(start, start+4);
    }
    
    String getConnectionURL(){
        StringBuffer url = new StringBuffer(curl);
        url.append(jTextField2.getText()).append(":").append(jFormattedTextField1.getText()); 
        if (jComboBox2.getSelectedIndex()==1)
            url.append(":").append(jTextField3.getText());
        else
            if (jComboBox2.getSelectedIndex() == 3)
                url.append(";instance=SQLEXPRESS;databaseName=").append(jTextField3.getText());
        else
                url.append("/").append(jTextField3.getText());
        return url.toString();
    }
    private void renewDialect() {
        jComboBox1.removeAllItems();
        String str = jComboBox2.getSelectedItem().toString();
        
        switch (str) {
            case "com.mysql.jdbc.Driver":
                jComboBox1.addItem("org.hibernate.dialect.MySQLDialect");
                jComboBox1.addItem("org.hibernate.dialect.MySQL8Dialect");                
                jFormattedTextField1.setText("3306");
                curl = "jdbc:mysql://";
                break;
            case "oracle.jdbc.OracleDriver":
                jComboBox1.addItem("org.hibernate.dialect.OraclecDialect");
                jComboBox1.addItem("org.hibernate.dialect.Oracle9cDialect");
                jComboBox1.addItem("org.hibernate.dialect.Oracle10cDialect");
                jComboBox1.addItem("org.hibernate.dialect.Oracle12cDialect");
                jFormattedTextField1.setText("1521");
                curl = "jdbc:oracle:thin:@";
                break;
            case "org.postgresql.Driver":                
                jComboBox1.addItem("org.hibernate.dialect.PostgreSQLDialect");
                jComboBox1.addItem("org.hibernate.dialect.PostgreSQL95Dialect");
                jFormattedTextField1.setText("5432");
                curl = "jdbc:postgresql://";
                break;
            case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                jComboBox1.addItem("org.hibernate.dialect.SQLServerDialect");
                jComboBox1.addItem("org.hibernate.dialect.SQLServer2012Dialect");
                jFormattedTextField1.setText("1433");
                curl = "jdbc:sqlserver://";
                break;
            case "org.mariadb.jdbc.Driver":
                jComboBox1.addItem("org.hibernate.dialect.MariaDB53Dialect");
                jFormattedTextField1.setText("3306");
                curl = "jdbc:mariadb://";
                break;
            }        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Настройки подключения");
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(424, 45));

        jButton1.setText("Отмена");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сохранить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setText("Драйвер");

        jLabel2.setText("Сервер");

        jTextField2.setText("localhost");

        jLabel3.setText("База данных");

        jLabel4.setText("Порт");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel5.setText("Логин");

        jTextField4.setText("admin");

        jLabel6.setText("Пароль");

        jLabel7.setText("Диалект");

        jComboBox1.setFocusable(false);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "com.mysql.jdbc.Driver", "oracle.jdbc.OracleDriver", "org.postgresql.Driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "org.mariadb.jdbc.Driver" }));
        jComboBox2.setFocusable(false);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel8.setText("Наименование");

        jTextField1.setText("Новая база");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4)
                    .addComponent(jPasswordField1)
                    .addComponent(jComboBox2, 0, 308, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String propFileName = "config//db.properties";
        Properties properties = new Properties();
        File propFile =  new File(propFileName);
        try {
            FileInputStream in = new FileInputStream(propFile);       
            properties.load(in);
            in.close();
        } catch (IOException e) {}
                                        
        try {
            FileOutputStream outputStream = new FileOutputStream(propFile);            
            
            properties.setProperty(jTextField3.getText()+"*hibernate.connection.driver_class", jComboBox2.getSelectedItem().toString());
            properties.setProperty(jTextField3.getText()+"*hibernate.dialect", jComboBox1.getSelectedItem().toString());
            properties.setProperty(jTextField3.getText()+"*hibernate.connection.username", jTextField4.getText());
            properties.setProperty(jTextField3.getText()+"*hibernate.connection.password", String.valueOf(jPasswordField1.getPassword()));
            properties.setProperty(jTextField3.getText()+"*hibernate.connection.url", getConnectionURL());
            properties.setProperty(jTextField3.getText()+"*hdbname", jTextField1.getText());
            
            properties.store(outputStream, "Databases Configuration");
            outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewDatabaseDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewDatabaseDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        
        if (evt.getStateChange() == 2) {
            renewDialect();
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
