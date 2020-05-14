/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import com.github.lgooddatepicker.components.CalendarPanel;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 *
 * @author User
 */
public class CalendarPopup {
    
    CalendarPanel calendarPanel = new CalendarPanel();
    Popup popup;
    PopupFactory pf = PopupFactory.getSharedInstance();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    ZoneId defaultZoneId = ZoneId.systemDefault();
    
    public CalendarPopup() {
        calendarPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        
        calendarPanel.addFocusListener(new FocusAdapter(){
            public void windowLostFocus( WindowEvent e ) {
                System.out.println("LOST FOCUS");
                popup.hide();
        }            
        });
        calendarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                System.out.println(evt.getLocationOnScreen());
                System.out.println("CLICK");
            }          
        });
            
    }
    
    public Popup getPopup(JComponent comp) {
        Point l = comp.getLocationOnScreen();
        popup = pf.getPopup(comp, calendarPanel, l.x, l.y+comp.getHeight());        
        return popup;
    }
    
    public Popup getPopup() {
        return popup;
    }
    
//    public setDate() {
//        calendarPanel.set
//    }
    
    public CalendarPanel getCPanel() {
        return calendarPanel;
    }
    public String getStringDate() {
        if (calendarPanel.getSelectedDate() != null)
            return format.format(getDate());
        
        return "";        
    }
    public Date getDate() {
        Date date;
        if (calendarPanel.getSelectedDate() != null) {
            LocalDateTime time = LocalDateTime.of(calendarPanel.getSelectedDate(), LocalTime.now());
            date = Date.from(time.atZone(defaultZoneId).toInstant());
        } else
            date = new Date();
        return date; 
    }
}
