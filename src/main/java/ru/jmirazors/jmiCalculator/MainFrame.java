/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator;

import java.awt.Color;
import java.awt.Graphics2D;
import ru.jmirazors.jmiСalculator.jmiframes.reportsif.ReportPriceListInternalFrame;
import java.awt.Image;
import java.awt.SplashScreen;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ru.jmirazors.jmiCalculator.beans.Calc;
import ru.jmirazors.jmiCalculator.beans.IFBean;
import ru.jmirazors.jmiCalculator.beans.SessionParams;
import ru.jmirazors.jmiСalculator.DAO.OrganizationDAO;
import ru.jmirazors.jmiСalculator.entity.Organization;
import ru.jmirazors.jmiСalculator.jmiframes.ContragentsIf;
import ru.jmirazors.jmiСalculator.jmiframes.DBManagerDialog;
import ru.jmirazors.jmiСalculator.jmiframes.DepartmentIf;
import ru.jmirazors.jmiСalculator.jmiframes.Documents.DocAct;
import ru.jmirazors.jmiСalculator.jmiframes.JournalDialog;
import ru.jmirazors.jmiСalculator.jmiframes.ListActInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListArrivalInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListBillInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListDeductInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListInventoryInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListInvoiceInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListLoyaltyInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListOfferInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListPayInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListReceiptInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListSaleInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListSetPriceInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.ListTransferInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.LoyaltyIf;
import ru.jmirazors.jmiСalculator.jmiframes.OrganizationDialog;
import ru.jmirazors.jmiСalculator.jmiframes.ParametersInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.PriceNameIf;
import ru.jmirazors.jmiСalculator.jmiframes.ProductsIf;
import ru.jmirazors.jmiСalculator.jmiframes.StoragesIf;
import ru.jmirazors.jmiСalculator.jmiframes.UnitsIf;
import ru.jmirazors.jmiСalculator.jmiframes.UsersIf;
import ru.jmirazors.jmiСalculator.jmiframes.reportsif.ReportProductsOnStockInternalFrame;
import ru.jmirazors.jmiСalculator.jmiframes.reportsif.ReportSaleDocInternalFrame;



/**
 *
 * @author Jonick
 */
public class MainFrame extends javax.swing.JFrame {        
   
    public static IFBean ifManager;
    
    public static SessionParams sessionParams = new SessionParams();
//    private User user;
//    private Organization organization;
    //компания
    private final String TITLE = "JMI Калькулятор v1.4.0";
    
    /**
     * Creates new form MainFrame
     * @throws java.lang.Exception
     */
    public MainFrame() throws Exception, Exception {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        new SelectDatabaseDialog(this, true).setVisible(true);             
        
        sessionParams.setOrganization(new OrganizationDAO().getOrganization(1L));
        
        sessionParams.setParam(new OrganizationDAO().getParam(sessionParams.getOrganization()));                
        
        initComponents();
        
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image iconLogo = imageIcon.getImage();
        this.setIconImage(iconLogo);
                
        
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                        int dialogResult = JOptionPane.showOptionDialog(null, "Выйти из приложения?", "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да"); 
                        if (dialogResult == 0) {                            
                            System.exit(0);
                        }
                    }
                });
        
        
        ifManager = new IFBean(jDesktopPane1);
        ifManager.infoMessage("JMI Калькулятор запущен.");
        ifManager.infoMessage("База данных: " + sessionParams.getDbname()); 
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        java.awt.EventQueue.invokeLater(() -> {
            this.setVisible(true);
        });        
        
        new LoginDialog(this, true).setVisible(true); 
        
        this.setTitle(TITLE+" ["+sessionParams.getUser().getName()+"] /"+sessionParams.getOrganization().getName()+"/");        
        
        ifManager.infoMessage("Вход выпонен для пользователя: " + sessionParams.getUser().getName());
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton4 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoPanel = new javax.swing.JTextArea();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(TITLE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/shopping-bag.png"))); // NOI18N
        jButton4.setToolTipText("Список заказы");
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cash-register.png"))); // NOI18N
        jButton18.setToolTipText("Розничные продажи");
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton18);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/box_arrival.png"))); // NOI18N
        jButton5.setToolTipText("Список поступления");
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fast-delivery.png"))); // NOI18N
        jButton8.setToolTipText("Список отгрузки");
        jButton8.setFocusPainted(false);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/transfer.png"))); // NOI18N
        jButton6.setToolTipText("Список перемещения");
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
        jButton7.setToolTipText("Список установка цен");
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/doc-plus.png"))); // NOI18N
        jButton13.setToolTipText("Оприходование товаров");
        jButton13.setFocusPainted(false);
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton13);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/doc-minus.png"))); // NOI18N
        jButton12.setToolTipText("Списание товаров");
        jButton12.setFocusPainted(false);
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton12);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bill.png"))); // NOI18N
        jButton14.setToolTipText("Счета на оплату");
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton14);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coin-stack.png"))); // NOI18N
        jButton15.setToolTipText("Оплаты");
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton15);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/complete.png"))); // NOI18N
        jButton16.setToolTipText("Акт выполненных работ");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/inventory.png"))); // NOI18N
        jButton17.setToolTipText("Инвентаризация товаров");
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton17);
        jToolBar1.add(jSeparator1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/boxes.png"))); // NOI18N
        jButton1.setToolTipText("Справочник товары");
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/group-profile-users.png"))); // NOI18N
        jButton2.setToolTipText("Справочник контрагенты");
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/factory-stock-house.png"))); // NOI18N
        jButton3.setToolTipText("Справочник склады");
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coins.png"))); // NOI18N
        jButton9.setToolTipText("Справочник Типы цен");
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/meter.png"))); // NOI18N
        jButton10.setToolTipText("Единицы измерения");
        jButton10.setFocusPainted(false);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        jButton11.setText("jButton11");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton11);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jDesktopPane1.setBackground(new java.awt.Color(153, 153, 153));
        jDesktopPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), new java.awt.Color(102, 102, 102)));
        jPanel1.add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        infoPanel.setEditable(false);
        infoPanel.setColumns(20);
        infoPanel.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        infoPanel.setRows(5);
        jScrollPane1.setViewportView(infoPanel);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(18, 25));
        jToolBar2.setMinimumSize(new java.awt.Dimension(18, 25));

        jLabel1.setText("\\");
            jLabel1.setPreferredSize(new java.awt.Dimension(7, 25));
            jToolBar2.add(jLabel1);

            jPanel2.add(jToolBar2, java.awt.BorderLayout.PAGE_START);

            jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

            getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

            jMenu1.setText("Файл");

            jMenuItem19.setText("Сменить пользователя");
            jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem19ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem19);

            jMenuItem1.setText("Выход");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem1);

            jMenuBar1.add(jMenu1);

            jMenu2.setText("Документы");

            jMenuItem3.setText("Заказы покупателей");
            jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem3ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem3);

            jMenuItem24.setText("Счета на оплату");
            jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem24ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem24);

            jMenuItem4.setText("Поступления");
            jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem4ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem4);

            jMenuItem15.setText("Отгрузки");
            jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem15ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem15);

            jMenuItem12.setText("Перемещения");
            jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem12ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem12);

            jMenuItem16.setText("Списание товаров");
            jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem16ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem16);

            jMenuItem17.setText("Оприходование товаров");
            jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem17ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem17);

            jMenuItem18.setText("Оплаты");
            jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem18ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem18);

            jMenuItem23.setText("Инвентаризация товаров");
            jMenu2.add(jMenuItem23);

            jMenuBar1.add(jMenu2);

            jMenu3.setText("Справочники");
            jMenu3.setToolTipText("");

            jMenuItem5.setText("Склады");
            jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem5ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem5);

            jMenuItem6.setText("Контрагенты");
            jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem6ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem6);

            jMenuItem7.setText("Еденицы измерения");
            jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem7ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem7);

            jMenuItem13.setText("Типы цен");
            jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem13ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem13);

            jMenuItem11.setText("Номенклатура");
            jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem11ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem11);

            jMenuItem29.setText("Дисконтные карты");
            jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem29ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem29);

            jMenuBar1.add(jMenu3);

            jMenu4.setText("Отчеты");

            jMenu5.setText("Продажи");

            jMenuItem32.setText("Продажи по документам");
            jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem32ActionPerformed(evt);
                }
            });
            jMenu5.add(jMenuItem32);

            jMenu4.add(jMenu5);

            jMenu8.setText("Товары");

            jMenuItem31.setText("Товары на складах");
            jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem31ActionPerformed(evt);
                }
            });
            jMenu8.add(jMenuItem31);

            jMenuItem30.setText("Прайс-Лист");
            jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem30ActionPerformed(evt);
                }
            });
            jMenu8.add(jMenuItem30);

            jMenu4.add(jMenu8);
            jMenu4.add(jSeparator2);

            jMenuItem26.setText("Журнал событий");
            jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem26ActionPerformed(evt);
                }
            });
            jMenu4.add(jMenuItem26);

            jMenuBar1.add(jMenu4);

            jMenu9.setText("Дополнительно");

            jMenuItem22.setText("Дисконтные карты");
            jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem22ActionPerformed(evt);
                }
            });
            jMenu9.add(jMenuItem22);

            jMenuItem21.setText("Калькулятор");
            jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem21ActionPerformed(evt);
                }
            });
            jMenu9.add(jMenuItem21);

            jMenuBar1.add(jMenu9);

            jMenu6.setText("Настройки");

            jMenuItem8.setText("Пользователи");
            jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem8ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem8);

            jMenuItem20.setText("Организация");
            jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem20ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem20);

            jMenuItem28.setText("Настройки организации");
            jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem28ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem28);

            jMenuItem14.setText("Подразделения");
            jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem14ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem14);

            jMenuItem9.setText("Интерфейс");
            jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem9ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem9);

            jMenuItem27.setText("Базы данных");
            jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem27ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem27);

            jMenuBar1.add(jMenu6);

            jMenu7.setText("Справка");

            jMenuItem10.setText("О программе");
            jMenu7.add(jMenuItem10);

            jMenuBar1.add(jMenu7);

            setJMenuBar(jMenuBar1);

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // НОМЕНКЛАТУРА        
        if (!ifManager.isProductsOpen()) {
            ifManager.showFrame(new ProductsIf(jToolBar2), false);
            ifManager.setProductsFrameOpen(true);
        }                                       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // КОНТРАГЕНТЫ
        if (!ifManager.isContragentsOpen()) {
            ifManager.showFrame(new ContragentsIf(jToolBar2), false);
            ifManager.setContragentsFrameOpen(true);
        }                 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // СКЛАДЫ
        if (!ifManager.isStoragesOpen()) {
            ifManager.showFrame(new StoragesIf(jToolBar2), false);
            ifManager.setStoragesFrameOpen(true);
        }                  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // СПРАВОЧНИК ЗАКАЗЫ
        if (!ifManager.isInvoiceListOpen()) {
            ifManager.showFrame(new ListInvoiceInternalFrame(jToolBar2), true);
            ifManager.setInvoiceListFrameOpen(true);
        }                  
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // СПРАВОЧНИК ПОСТУПЛЕНИЯ
        if (!ifManager.isArrivalListOpen()) {
            ifManager.showFrame(new ListArrivalInternalFrame(jToolBar2), true);
            ifManager.setArrivalListFrameOpen(true);
        }                        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // СПРАВОЧНИК РЕАЛИЗАЦИИ
         if (!ifManager.isOfferListOpen()) {
            ifManager.showFrame(new ListOfferInternalFrame(jToolBar2), true);
            ifManager.setOfferListFrameOpen(true);
        }           
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // СПРАВОЧНИК ПЕРЕМЕЩЕНИЯ
         if (!ifManager.isTransferListOpen()) {
            ListTransferInternalFrame transferList = new ListTransferInternalFrame(jToolBar2);
            jDesktopPane1.add(transferList);
            jDesktopPane1.getDesktopManager().maximizeFrame(transferList);
            transferList.setVisible(true);
            ifManager.setTransferListFrameOpen(true);
        }         
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // ТИПЫ ЦЕН
        if (!ifManager.isPriceNameOpen()) {
            PriceNameIf priceName = new PriceNameIf(jToolBar2);
            jDesktopPane1.add(priceName);
            priceName.setVisible(true);
            ifManager.setPriceNameFrameOpen(true);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // ЕДИНИЦЫ ИЗМЕРЕНИЯ
        if (!ifManager.isUnitsOpen()) {
            UnitsIf units = new UnitsIf(jToolBar2);
            jDesktopPane1.add(units);
            units.setVisible(true);
            ifManager.setUsersFrameOpen(true);
        }          
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // СПРАВОЧНИК УСТАНОВКА ЦЕН               
         if (!ifManager.isSetPriceListOpen()) { 
            ListSetPriceInternalFrame setPriceList = new ListSetPriceInternalFrame(jToolBar2);
            jDesktopPane1.add(setPriceList);
            jDesktopPane1.getDesktopManager().maximizeFrame(setPriceList);
            setPriceList.setVisible(true);
            ifManager.setSetPriceListFrameOpen(true);
        }            
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // ПОЛЬЗОВАТЕЛИ
        if (!ifManager.isUsersOpen()) {
            UsersIf users = new UsersIf(jToolBar2);
            jDesktopPane1.add(users);
            users.setVisible(true);
            ifManager.setUsersFrameOpen(true);
        }        
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // ОРГАНИЗАЦЯ      
        Organization org;
        try {
            org = new OrganizationDAO().getOrganization(1L);
            OrganizationDialog organizationDialog = new OrganizationDialog(null, true, org);
            organizationDialog.setLocationRelativeTo(this);
            organizationDialog.setVisible(true);        
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // СПРАВОЧНИК ОПЛАТЫ
         if (!ifManager.isPayListOpen()) {  
            ListPayInternalFrame payList = new ListPayInternalFrame(jToolBar2);
            jDesktopPane1.add(payList);
            jDesktopPane1.getDesktopManager().maximizeFrame(payList);
            payList.setVisible(true);
            ifManager.setPayListFrameOpen(true);
        }         
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // СПРАВОЧНИК СПИСАНИЕ
         if (!ifManager.isDeductListOpen()) {  
            ListDeductInternalFrame deductList = new ListDeductInternalFrame(jToolBar2);
            jDesktopPane1.add(deductList);
            jDesktopPane1.getDesktopManager().maximizeFrame(deductList);
            deductList.setVisible(true);
            ifManager.setDeductListFrameOpen(true);
        }                   
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // СПРАВОЧНИК ОПРИХОДОВАНИЕ
         if (!ifManager.isReceiptListOpen()) {  
            ListReceiptInternalFrame receiptList = new ListReceiptInternalFrame(jToolBar2);
            jDesktopPane1.add(receiptList);
            jDesktopPane1.getDesktopManager().maximizeFrame(receiptList);
            receiptList.setVisible(true);
            ifManager.setReceiptListFrameOpen(true);
        }       
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // СПРАВОЧНИК СЧЕТА НА ОПЛАТУ
         if (!ifManager.isBillListOpen()) { 
            ListBillInternalFrame billList = new ListBillInternalFrame(jToolBar2);
            jDesktopPane1.add(billList);
            jDesktopPane1.getDesktopManager().maximizeFrame(billList);
            billList.setVisible(true);
            ifManager.setBillListFrameOpen(true);
        }         
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // СКЛАДЫ
        if (!ifManager.isStoragesOpen()) {
            StoragesIf storages = new StoragesIf(jToolBar2);
            jDesktopPane1.add(storages);
            storages.setVisible(true);
            ifManager.setStoragesFrameOpen(true);
        } 
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // КОНТРАГЕНТЫ
        if (!ifManager.isContragentsOpen()) {
            ContragentsIf contragents = new ContragentsIf(jToolBar2);
            jDesktopPane1.add(contragents);
            contragents.setVisible(true);
            ifManager.setContragentsFrameOpen(true);
        }   
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // ЕДИНИЦЫ ИЗМЕРЕНИЯ
        if (!ifManager.isUnitsOpen()) {
            UnitsIf units = new UnitsIf(jToolBar2);
            jDesktopPane1.add(units);
            units.setVisible(true);
            ifManager.setUsersFrameOpen(true);
        } 
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // ТИПЫ ЦЕН
        if (!ifManager.isPriceNameOpen()) {
            PriceNameIf priceName = new PriceNameIf(jToolBar2);
            jDesktopPane1.add(priceName);
            priceName.setVisible(true);
            ifManager.setPriceNameFrameOpen(true);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // НОМЕНКЛАТУРА        
        if (!ifManager.isProductsOpen()) {
            ProductsIf products = new ProductsIf(jToolBar2);
            jDesktopPane1.add(products);
            products.setVisible(true);
            ifManager.setProductsFrameOpen(true);
        }  
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // СПРАВОЧНИК ЗАКАЗЫ        
        if (!ifManager.isInvoiceListOpen()) {
            ifManager.showFrame(new ListInvoiceInternalFrame(jToolBar2), true);
            ifManager.setInvoiceListFrameOpen(true);
            }          
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // СПРАВОЧНИК ПОСТУПЛЕНИЯ
        if (!ifManager.isArrivalListOpen()) {
            ListArrivalInternalFrame arrivalList = new ListArrivalInternalFrame(jToolBar2);
            jDesktopPane1.add(arrivalList);
            jDesktopPane1.getDesktopManager().maximizeFrame(arrivalList);
            arrivalList.setVisible(true);
            ifManager.setArrivalListFrameOpen(true);
        }   
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // СПРАВОЧНИК РЕАЛИЗАЦИИ
         if (!ifManager.isOfferListOpen()) {
            ListOfferInternalFrame offerList = new ListOfferInternalFrame(jToolBar2);
            jDesktopPane1.add(offerList);
            jDesktopPane1.getDesktopManager().maximizeFrame(offerList);
            offerList.setVisible(true);
            ifManager.setOfferListFrameOpen(true);
        }  
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // СПРАВОЧНИК ПЕРЕМЕЩЕНИЯ
         if (!ifManager.isTransferListOpen()) {
            ListTransferInternalFrame transferList = new ListTransferInternalFrame(jToolBar2);
            jDesktopPane1.add(transferList);
            jDesktopPane1.getDesktopManager().maximizeFrame(transferList);
            transferList.setVisible(true);
            ifManager.setTransferListFrameOpen(true);
        } 
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // СПРАВОЧНИК СПИСАНИЕ
         if (!ifManager.isDeductListOpen()) {  
            ListDeductInternalFrame deductList = new ListDeductInternalFrame(jToolBar2);
            jDesktopPane1.add(deductList);
            jDesktopPane1.getDesktopManager().maximizeFrame(deductList);
            deductList.setVisible(true);
            ifManager.setDeductListFrameOpen(true);
        }  
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // СПРАВОЧНИК ОПРИХОДОВАНИЕ
         if (!ifManager.isReceiptListOpen()) {  
            ListReceiptInternalFrame receiptList = new ListReceiptInternalFrame(jToolBar2);
            jDesktopPane1.add(receiptList);
            jDesktopPane1.getDesktopManager().maximizeFrame(receiptList);
            receiptList.setVisible(true);
            ifManager.setReceiptListFrameOpen(true);
        } 
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        // Журнал событий
        JournalDialog journald = new JournalDialog(this, true);
        journald.setLocationRelativeTo(this);
        journald.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // СПРАВОЧНИК ОПЛАТЫ
         if (!ifManager.isPayListOpen()) {  
            ListPayInternalFrame payList = new ListPayInternalFrame(jToolBar2);
            jDesktopPane1.add(payList);
            jDesktopPane1.getDesktopManager().maximizeFrame(payList);
            payList.setVisible(true);
            ifManager.setPayListFrameOpen(true);
        }  
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
         if (!ifManager.isBillListOpen()) { 
            ListBillInternalFrame billList = new ListBillInternalFrame(jToolBar2);
            jDesktopPane1.add(billList);
            jDesktopPane1.getDesktopManager().maximizeFrame(billList);
            billList.setVisible(true);
            ifManager.setBillListFrameOpen(true);
        }  
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // Менеджер баз данных
        DBManagerDialog dbm = new DBManagerDialog(this, true);
        dbm.setLocationRelativeTo(this);
        dbm.setVisible(true);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

   
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // АКТЫ ВЫПОЛНЕННЫХ РАБОТ
         if (!ifManager.isActListOpen()) {  
            ListActInternalFrame actList = new ListActInternalFrame(jToolBar2);
            jDesktopPane1.add(actList);
            jDesktopPane1.getDesktopManager().maximizeFrame(actList);
            actList.setVisible(true);
            ifManager.setActListFrameOpen(true);
        }          
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // РОЗНИЧНЫЕ ПРОДАЖИ
        if (!ifManager.isSaleListOpen()) {
            ifManager.showFrame(new ListSaleInternalFrame(jToolBar2), true);
            ifManager.setSaleListFrameOpen(true);
            }                  
    }//GEN-LAST:event_jButton18ActionPerformed
    
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // ИНВЕНТАРИЗАЦИЯ ТОВАРОВ
        if (!ifManager.isInventoryListOpen()) {
            ifManager.showFrame(new ListInventoryInternalFrame(jToolBar2), true);
            ifManager.setInventoryListFrameOpen(true);
            }          
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
       // НАСТРОЙКИ ОРГАНИЗАЦИИ
        if (!ifManager.isParametersOpen()) {
            ParametersInternalFrame params = new ParametersInternalFrame();
            jDesktopPane1.add(params);
            params.setVisible(true);
            ifManager.setParametersFrameOpen(true);
        }  
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // ДИСКОНТНЫЕ КАРТЫ
        if (!ifManager.isLoyaltyOpen()) {
            ifManager.showFrame(new ListLoyaltyInternalFrame(jToolBar2), false);
            ifManager.setLoyaltyOpen(true);
            }          
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        // Отчет прайслист
        if (!ifManager.isReportPriceListOpen()) {
            ifManager.showFrame(new ReportPriceListInternalFrame(jToolBar2), true);
            ifManager.setReportPriceListOpen(true);
            }         
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        // отчет товары на складах
        if (!ifManager.isReportStockProductOpen()) {
            ifManager.showFrame(new ReportProductsOnStockInternalFrame(jToolBar2), true);
            ifManager.setStockProductOpen(true);
            }         
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        // отчет продажи по документам
        if (!ifManager.isReportSaleDocOpen()) {
            ifManager.showFrame(new ReportSaleDocInternalFrame(jToolBar2), true);
            ifManager.setReportSaleDocOpen(true);
            }                 
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // Подразделения
        if (!ifManager.isDepartmentOpen()) {
            ifManager.showFrame(new DepartmentIf(jToolBar2), false);
//            DepartmentIf organizations = new DepartmentIf(jToolBar2);
//            jDesktopPane1.add(organizations);
//            organizations.show();
            ifManager.setDepartmentFrameOpen(true);
            }        
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // Сменить пользователя
        try {    
            new LoginDialog(this, true).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // Калькулятор        
        ifManager.showFrame(new Calc(), false);


    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // Дисконтные карты        
        ifManager.showFrame(new LoyaltyIf(jToolBar2), false);
        
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        ifManager.showFrame(new DocAct(), false);
    }//GEN-LAST:event_jButton11ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextArea infoPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    protected static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    public static javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
}
