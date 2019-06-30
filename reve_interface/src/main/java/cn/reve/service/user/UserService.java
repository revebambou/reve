package cn.reve.service.user;
import cn.reve.entity.PageResult;
import cn.reve.pojo.user.User;

import java.util.*;

/**
 * user业务逻辑层
 */
public interface UserService {


    public List<User> findAll();


    public PageResult<User> findPage(int page, int size);


    public List<User> findList(Map<String,Object> searchMap);


    public PageResult<User> findPage(Map<String,Object> searchMap,int page, int size);


    public User findById(String username);

    public void add(User user);


    public void update(User user);


    public void delete(String username);

    void spawnCode(String phoneNum);

    void addUser(User user, String phoneNum);

    void checkCode(String code, String phoneNum);

    void checkPhone(String phoneNum);
}
