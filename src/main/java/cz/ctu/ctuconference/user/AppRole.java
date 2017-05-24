/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.user;

import cz.ctu.ctuconference.utils.dao.AbstractEntity;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nick nemame
 */
@Entity
@Table(name = "app_role")
public class AppRole extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "authority")
    private String authority;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_for_user",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")})
    private List<AppUser> userList;

    public AppRole() {
    }

    public AppRole(Long id) {
        this.id = id;
    }

    public AppRole(Long id, String name, String title, String authority) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AppRole[ id=" + id + " ]";
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<AppUser> getUserList() {
        return userList;
    }

    public void setUserList(List<AppUser> userList) {
        this.userList = userList;
    }

}
