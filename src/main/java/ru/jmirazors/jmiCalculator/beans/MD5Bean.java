/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class MD5Bean {
    
    public String getEncryptedPassword(String pass) {
        MessageDigest md;
        StringBuffer sb = null;        
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] b = pass.getBytes();
            md.reset();
            byte[] b1 = md.digest(b);
            sb = new StringBuffer();
            for (int i = 0; i < b1.length; i++)
                sb.append(Integer.toHexString(0xff & b1[i]));            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Bean.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return sb.toString();
    }
    
    public boolean isPassCoins(String pass1, String pass2) {
        return getEncryptedPassword(pass1).equals(pass2);
    }
}
