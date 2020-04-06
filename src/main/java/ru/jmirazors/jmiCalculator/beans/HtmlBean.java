/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import java.util.List;

/**
 *
 * @author User
 */
public class HtmlBean {
    
    public String htmlOpen(){
        return "<html><body style='font-size: 8px'>";
    }
    public String htmlClose() {
        return "</body></html>";
    }
    
    public String colorRed(String par) {
        return "<span style='color:#FF0000;'>"+par+"</span>";
    }
    public String colorGreen(String par) {
        return "<span style='color:#32CD32;'>"+par+"</span>";
    }
    public String title(String tit) {
        return "<center><h2>"+tit+"</h2></center>";
    }
    
    public String tableOpen(String width) {
        return "<table style='width:"+width+"%; padding:10%;'>";
    }
    
    public String tableClose() {
        return "</table>";
    }
    
    public String TRGrayOpen(){
        return "<tr style='background:#cccccc;border-bottom:1px solid;'>";
    }
    
    public String TROpen() {
        return "<tr style='border-bottom:1px solid;'>";
    }
    
    public String TRClose() {
        return "</tr>";
    }
    
    public String TH(String par) {
        return "<th>"+par+"</th>";
    }
    
    public String TD(String par, String align) {
        return "<td style='text-align:"+align+";border-right:1px solid;'>"+par+"</td>";
    }
    
    public String TD(long par, String align) {
        return "<td style='text-align:"+align+";border-right:1px solid;'>"+par+"</td>";
    }
    
    public String TD(float par, String align) {
        return "<td style='text-align:"+align+";border-right:1px solid;'>"+par+"</td>";
    }
    public String TD(double par, String align) {
        return "<td style='text-align:"+align+";border-right:1px solid;'>"+par+"</td>";
    }
    
    public String TD() {
        return "<td></td>";
    }
    public String TDSpan(String par, int tds) {
        return "<td colspan='"+tds+"'>"+par+"</td>";
    }
    public String Strong(String par) {
        return "<strong>"+par+"</strong>";
    }
    public String table(List<String[]> data, int cols, String width) {
        String str = tableOpen(width);
            for (int i = 0; i < data.size(); i++) {
                str += TROpen();
                for (int j = 0; j < cols; j++) {
                    str += TD(data.get(i)[j], "center");
                }
                str += TRClose();                
            }
            str += tableClose();
        return str;
    }
}
