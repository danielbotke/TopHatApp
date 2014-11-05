/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import dao.RoomDao;
import dao.ToDoActionDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Daniel
 */
@Entity
public class Home implements Serializable {
    @Id
    private String id;
    
    @OneToMany(mappedBy = "home", targetEntity = Room.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
    @OneToMany(mappedBy = "home", targetEntity = IUser.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IUser> users = new ArrayList<>();
    @OneToMany(mappedBy = "home", targetEntity = HistAction.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistAction> histAction = new ArrayList<>();
    @OneToMany(mappedBy = "home", targetEntity = ToDoAction.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ToDoAction> toDoAction = new ArrayList<>();
    @Column
    private String ip;

    public List<Room> getRooms() {
        rooms = (new RoomDao()).list(this.id);
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<IUser> getUsers() {
        return users;
    }

    public void setUsers(List<IUser> users) {
        this.users = users;
    }

    public List<HistAction> getHistAction() {
        return histAction;
    }

    public void setHistAction(List<HistAction> histAction) {
        this.histAction = histAction;
    }

    public List<ToDoAction> getToDoAction() {
        return toDoAction;
    }

    public void setToDoAction(List<ToDoAction> toDoAction) {
        this.toDoAction = toDoAction;
    }
    
    
    
}
