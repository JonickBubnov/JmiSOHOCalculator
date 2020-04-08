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
    
}
