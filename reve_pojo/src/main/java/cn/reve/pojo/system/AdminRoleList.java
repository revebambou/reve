package cn.reve.pojo.system;

import java.io.Serializable;
import java.util.List;

public class AdminRoleList implements Serializable {

    private Admin admin;

    private List<Role> roleList;

    public AdminRoleList() {
    }

    public AdminRoleList(Admin admin, List<Role> roleList) {
        this.admin = admin;
        this.roleList = roleList;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "AdminRoleList{" +
                "admin=" + admin +
                ", roleList=" + roleList +
                '}';
    }
}
