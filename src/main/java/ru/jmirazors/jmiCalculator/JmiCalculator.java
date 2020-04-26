/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator;

import javax.swing.JFrame;
import ru.jmirazors.jmiCalculator.beans.LookAndFeelUtils;

/**
 *
 * @author User
 */
public class JmiCalculator {
    public static void main(String args []) throws Exception {
        LookAndFeelUtils.setWindowsLookAndFeel();
        JFrame mainFrame = new MainFrame();        
    }
}

