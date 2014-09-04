/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
public class HistAction implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTime;
    @OneToOne(mappedBy = "histAction", fetch = FetchType.LAZY, optional = false)
    private IAction action;
    @ManyToOne
    @JoinColumn(name = "home_id", nullable=false)
    private Home home;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }


    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
    
    
    
    
}
