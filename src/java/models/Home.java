/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity; 
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
    @OneToMany
    private ArrayList<Room> rooms = new ArrayList<>();
    @OneToMany
    private ArrayList<IUser> users = new ArrayList<>();
    private String url;

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
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

    public ArrayList<IUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<IUser> users) {
        this.users = users;
    }
    
    
}
