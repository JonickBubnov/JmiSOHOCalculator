/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

/**
 *
 * @author User
 */
public class MonthToTextBean {
    
    public String dateToText(String date) {
        String sd = "";
        String dd = date.substring(0, date.indexOf('.'));
        String mm = date.substring(date.indexOf('.')+1, date.lastIndexOf('.'));
        String yy = date.substring(date.lastIndexOf(".")+1);
        
        sd += dd;
        switch (mm) {
            case "01":
                sd += " января ";
                break;
            case "02":
                sd += " февраля ";
                break;
            case "03":
                sd += " марта ";
                break;
            case "04":
                sd += " апреля ";
                break;
            case "05":
                sd += " мая ";
                break;       
            case "06":
                sd += " июня ";
                break;      
            case "07":
                sd += " июля ";
                break;      
            case "08":
                sd += " августа ";
                break;         
            case "09":
                sd += " сентября ";
                break;          
            case "10":
                sd += " октября ";
                break;         
            case "11":
                sd += " ноября ";
                break;        
            case "12":
                sd += " декабря ";
                break;                
        }
        sd += yy;
        return sd;
    }
    
}
