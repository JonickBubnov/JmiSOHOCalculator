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
public class NumberToTextBean {
    
    int mil = 0;
    int th = 0;
    int rub = 0;
    
    public String numberToText(Double a) {
        String str = "";
        int dig = 0;        
        
        dig = (int) Math.floor(a/100000000);
        str += h(dig);
        dig = (int) Math.floor(a/1000000) - (int) Math.floor(a/100000000)*100;
        if (dig >=10 && dig < 20)
            str += e(dig); else {
            dig = (int) Math.floor(dig/10);
            str += d(dig);
            dig = (int) Math.floor(a/1000000) - (int) Math.floor(a/10000000)*10;
            str += o(dig);
        }
        str += mil(mil);
        
        dig = (int) Math.floor(a/1000) - (int) Math.floor(a/1000000)*1000;
        str += h((int)Math.floor(dig/100));
        dig = dig - (int)Math.floor(dig/100)*100;
        if (dig >=10 && dig < 20)
            str += e(dig); else {
            str += d((int) Math.floor(dig/10));
            dig = dig - (int) Math.floor(dig/10)*10;
            str += of(dig);
        } 
        str += th(th);
        
        dig = (int)Math.floor(a) - (int) Math.floor(a/1000)*1000;
        str += h((int)Math.floor(dig/100));
        dig = dig - (int)Math.floor(dig/100)*100;
        if (dig >=10 && dig < 20)
            str += e(dig); else {
            str += d((int) Math.floor(dig/10));
            dig = dig - (int) Math.floor(dig/10)*10;
            str += o(dig);
        }         
        str += rub(rub);
        

        dig = (int) Math.round(a*100) - (int)Math.floor(a)*100;
        str += dig;
        str += " коп.";
              
        return str;
    }
        
    String rub(int c) { 
            String r = "";
            switch (c) {
                case 1:
                    r = "рубль ";
                    break;
                case 2:
                    r = "рубля ";
                    break;
                case 3:
                    r = "рублей ";
                    break;
            }
            return r;
        }
        
    String mil(int c) {
            String m = "";
            switch (c) {
                case 1:
                    m = "миллион ";
                    break;
                case 2:
                    m = "миллиона ";
                    break;
                case 3:
                    m = "миллионов ";
                    break;
            }
            return m;
        }
        
    String th(int c) {
            String t = "";
            switch (c) {
                case 1:
                    t = "тысяча ";
                    break;
                case 2:
                    t = "тысячи ";
                    break;
                case 3:
                    t = "тысяч ";
                    break;
            }
            return t;
        }        
        
        
    String h(int dig) {
            String s = "";
            switch (dig) {
                case 1:
                    s += "сто ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 2:
                    s += "двести ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;      
                case 3:
                    s += "триста ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;      
                case 4:
                    s += "четыреста ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;         
                case 5:
                    s += "пятьсот ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;       
                case 6:
                    s += "шестьсот ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;        
                case 7:
                    s += "семьсот ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 8:
                    s += "восемьсот ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 9:
                    s += "девятьсот ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;                    
            }
            return s;
        }
        
    String d(int dig) {
            String s = "";
            switch (dig) {
                case 2:
                    s += "двадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 3:
                    s += "тридцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;        
                case 4:
                    s += "сорок ";
                    mil = 3;                    
                    th = 3;
                    rub = 3;
                    break;     
                case 5:
                    s += "пятдесят ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;        
                case 6:
                    s += "шестьдесят ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;          
                case 7:
                    s += "семьдесят ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;        
                case 8:
                    s += "восемьдесят ";
                    th = 3;
                    mil = 3;
                    rub = 3;
                    break;           
                case 9:
                    s += "девяносто ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;                    
            }
            return s;
        }
        
    String e(int dig) {
            String s = "";
            switch (dig) {
                case 10:
                    s += "десять ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 11:
                    s += "одинадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 12:
                    s += "двенадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 13:
                    s += "тринадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;            
                case 14:
                    s += "четырнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;              
                case 15:
                    s += "пятнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 16:
                    s += "шестнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;             
                case 17:
                    s += "семнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;            
                case 18:
                    s += "восемнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;              
                case 19:
                    s += "девятнадцать ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;                 
            }
            return s;
        }
        
    String of(int dig) {
            String s = "";
            switch (dig) {
                case 1:
                    s += "одна ";
                    th = 1;
                    rub = 3;
                    break;
                case 2:
                    s += "две ";
                    th = 2;
                    rub = 3;
                    break;
                case 3:
                    s += "три ";
                    mil = 2;
                    th = 2;
                    rub = 3;
                    break;
                case 4:
                    s += "четыре ";
                    mil = 2;
                    th = 2;
                    rub = 3;
                    break;
                case 5:
                    s += "пять ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 6:
                    s += "шесть ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 7:
                    s += "семь ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 8:
                    s += "восемь ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 9:
                    s += "девять ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;                      
            }
            return s;
        }
        
    String o(int dig) {
        String s = "";            
        switch (dig) {
                case 1:
                    s += "один ";
                    mil = 1;
                    rub = 1;
                    break;
                case 2:
                    s += "два ";
                    mil = 2;
                    rub = 2;
                    break;
                case 3:
                    s += "три ";
                    mil = 2;
                    th = 2;
                    rub = 2;
                    break;
                case 4:
                    s += "четыре ";
                    mil = 2;
                    th = 2;
                    rub = 2;
                    break;
                case 5:
                    s += "пять ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 6:
                    s += "шесть ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 7:
                    s += "семь ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 8:
                    s += "восемь ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;
                case 9:
                    s += "девять ";
                    mil = 3;
                    th = 3;
                    rub = 3;
                    break;                
        }            
            return s;
        }    
}
