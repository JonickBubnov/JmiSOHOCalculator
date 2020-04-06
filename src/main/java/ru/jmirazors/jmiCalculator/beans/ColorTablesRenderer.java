/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author User
 */
public class ColorTablesRenderer implements TableCellRenderer{
    
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    java.awt.Color oddRow = new java.awt.Color(245, 245, 245);
    java.awt.Color evenRow = new java.awt.Color(255, 255, 255);
    java.awt.Color selected = new java.awt.Color(169, 169, 169);    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);
            if (row % 2 == 0 && !isSelected)
                c.setBackground(oddRow);
            else if (row % 2 == 1 && !isSelected)
                c.setBackground(evenRow);
            else
                c.setBackground(selected);
            return c;
    }
    
}
