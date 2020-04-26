/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiCalculator.beans;

import ru.jmirazors.jmiСalculator.entity.Organization;
import ru.jmirazors.jmiСalculator.entity.Parameters;
import ru.jmirazors.jmiСalculator.entity.User;

/**
 *
 * @author User
 */
public class SessionParams {
    
    private User user;
    private Organization organization;
    private Parameters param;
    private String dburl;
    private String dbname;
    private String dbusername;
    private String dbpass;
    private String dbdriver;
    private String dbdialect;
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Parameters getParam() {
        return param;
    }

    public void setParam(Parameters param) {
        this.param = param;
    }        

    public String getDburl() {
        return dburl;
    }

    public void setDburl(String dburl) {
        this.dburl = dburl;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbusername() {
        return dbusername;
    }

    public void setDbusername(String dbusername) {
        this.dbusername = dbusername;
    }

    public String getDbpass() {
        return dbpass;
    }

    public void setDbpass(String dbpass) {
        this.dbpass = dbpass;
    }

    public String getDbdriver() {
        return dbdriver;
    }

    public void setDbdriver(String dbdriver) {
        this.dbdriver = dbdriver;
    }

    public String getDbdialect() {
        return dbdialect;
    }

    public void setDbdialect(String dbdialect) {
        this.dbdialect = dbdialect;
    }
    
}
