/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 *
 * @author Daniel
 */
@Entity
public class IAction implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String name;
    @OneToOne @MapsId
    private Device device;
    @OneToOne
    private HistAction histAction;

    public IAction(String name, Device device, HistAction histAction) {
        this.name = name;
        this.device = device;
        this.histAction = histAction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public HistAction getHistAction() {
        return histAction;
    }

    public void setHistAction(HistAction histAction) {
        this.histAction = histAction;
    }
    
    
    
}
