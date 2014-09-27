/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import dao.DeviceDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Daniel
 */
@Entity
public class Room implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable=false)
    private String name;
    @OneToMany(mappedBy = "room", targetEntity = Device.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "home_id", nullable=false)
    private Home home;

    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Device> getDevices() {
        this.setDevices((new DeviceDao()).list(this.id));
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
    
}
