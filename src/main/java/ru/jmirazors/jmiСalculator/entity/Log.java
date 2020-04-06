/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import ru.jmirazors.jmiСalculator.DAO.LogDAO;

/**
 *
 * @author jonick
 */
@Entity
@Table(name="Log")
public class Log implements Serializable{
@Id
@GeneratedValue(
    strategy= GenerationType.AUTO, 
    generator="native")
@GenericGenerator(
    name = "native", 
    strategy = "native"
)
private long id;
@Basic
@Temporal(TemporalType.TIMESTAMP)
private java.util.Date indate;
@OneToOne
private User user;
private String job;
        
        

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
public Log(){};
    
public Log(Date date, User user, String job){
    setIndate(date);
    setUser(user);
    setJob(job);
}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void write() {
        new LogDAO().writeLog(this);
    }
}
