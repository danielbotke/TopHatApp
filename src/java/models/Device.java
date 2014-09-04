/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Daniel
 */
@Entity
public class Device implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable=false)
    private String name;
    @Column
    private int actionPort;
    @Column
    private int statusDevice;
    @Column(nullable=false)
    private char type;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable=false)
    private Room room;
    

    public Device() {
    }

    public Device(String name, int actionPort, int statusDevice, char type) {
        this.name = name;
        this.actionPort = actionPort;
        this.statusDevice = statusDevice;
        this.type = type;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActionPort() {
        return actionPort;
    }

    public void setActionPort(int actionPort) {
        this.actionPort = actionPort;
    }

    public int getStatus() {
        return statusDevice;
    }

    public void setStatusPort(int statusPort) {
        this.statusDevice = statusPort;
    }

    public int getStatusDevice() {
        return statusDevice;
    }

    public void setStatusDevice(int statusDevice) {
        this.statusDevice = statusDevice;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
}
