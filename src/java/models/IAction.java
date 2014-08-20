/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private String toDoAction;
    @OneToOne
    private Device device;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToDoAction() {
        return toDoAction;
    }

    public void setToDoAction(String toDoAction) {
        this.toDoAction = toDoAction;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
}
