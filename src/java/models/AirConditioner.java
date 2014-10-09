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
import javax.persistence.OneToOne;

/**
 *
 * @author Daniel
 */
@Entity
public class AirConditioner implements Serializable  {
    
    @Id
    @GeneratedValue
    private int id;
    @Column()
    private int temperatura;
    @Column
    private boolean ligado;
    @OneToOne
    private Device device;

    public AirConditioner() {
        temperatura = 17;
        ligado = false;
    }
    
    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatuda(int temperatura) {
        this.temperatura = temperatura;
    }

    public boolean getLigado() {
        return ligado;
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
}
