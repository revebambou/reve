package cn.reve.service.system;
import cn.reve.entity.PageResult;
import cn.reve.pojo.system.Admin;

import java.util.*;

/**
 * admin业务逻辑层
 */
public interface AdminService {


    public List<Admin> findAll();


    public PageResult<Admin> findPage(int page, int size);


    public List<Admin> findList(Map<String,Object> searchMap);


    public PageResult<Admin> findPage(Map<String,Object> searchMap,int page, int size);


    public Admin findById(Integer id);

    public void add(Admin admin);


    public void update(Admin admin);


    public void delete(Integer id);

    void saveAdminRoleByMap(Map<String, Object> admin);
}
