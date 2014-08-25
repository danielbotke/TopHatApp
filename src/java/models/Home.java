/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

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
    private int id;
    
    @OneToMany(mappedBy = "home", targetEntity = Room.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
    @OneToMany(mappedBy = "home", targetEntity = IUser.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IUser> users = new ArrayList<>();
    @OneToMany(mappedBy = "home", targetEntity = HistAction.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistAction> histAction = new ArrayList<>();
    @Column
    private String url;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<IUser> getUsers() {
        return users;
    }

    public void setUsers(List<IUser> users) {
        this.users = users;
    }
    
    
}
