/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Daniel
 */
public class Device {
    private String name;
    private int actionPort;
    private int statusDevice;
    private char type;

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
    
    
}
