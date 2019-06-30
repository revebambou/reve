package cn.reve.service.impl;
import cn.reve.dao.AdminRoleMapper;
import cn.reve.dao.RoleMapper;
import cn.reve.pojo.system.AdminRole;
import cn.reve.pojo.system.AdminRoleList;
import cn.reve.pojo.system.Role;
import cn.reve.utils.MapperUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.reve.dao.AdminMapper;
import cn.reve.entity.PageResult;
import cn.reve.pojo.system.Admin;
import cn.reve.service.system.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Admin> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectAll();
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Admin> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Admin> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param admin
     */
    public void add(Admin admin) {
        adminMapper.insert(admin);
    }

    /**
     * 修改
     * @param admin
     */
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void saveAdminRoleList(Admin admin, List<String> roles, String memo) {
        saveAdminRole(admin, roles, false);
    }

    @Override
    public AdminRoleList findAdminRoleByAdminId(Integer adminId) {
        AdminRoleList adminRoleList = new AdminRoleList();
        Admin admin = adminMapper.selectByPrimaryKey(adminId);
        Example example = MapperUtils.andEqualToWithSingleValue(AdminRole.class, "adminId", adminId);
        List<AdminRole> adminRoles = adminRoleMapper.selectByExample(example);
        List<Role> roleList = new ArrayList<>();
        for (AdminRole role : adminRoles) {
            Integer roleId = role.getRoleId();
            Role roleQuery = roleMapper.selectByPrimaryKey(roleId);
            roleList.add(roleQuery);
        }
        adminRoleList.setAdmin(admin);
        adminRoleList.setRoleList(roleList);
        return adminRoleList;
    }

    @Override
    @Transactional
    public void updateAdminRole(Admin admin, List<String> roleList, String memo) {
        saveAdminRole(admin, roleList, true);
    }


    private void saveAdminRole(Admin admin, List<String> roles, boolean update){
        int adminId = admin.getId();
        if(!update){
            adminMapper.insertSelective(admin);
            //need to assign the id if this is insert
            adminId = admin.getId();
        }
        //if this is updated, remove all the previous tb_admin_role
        else{
            adminMapper.updateByPrimaryKeySelective(admin);
            Example example = MapperUtils.andEqualToWithSingleValue(AdminRole.class, "adminId", adminId);
            List<AdminRole> adminRoles = adminRoleMapper.selectByExample(example);
            for (AdminRole role : adminRoles) {
                AdminRole adminRole = new AdminRole();
                adminRole.setRoleId(role.getRoleId());
                adminRole.setAdminId(adminId);
                adminRoleMapper.deleteByPrimaryKey(adminRole);
            }
        }
        //save tb_admin_role
        AdminRole adminRole = new AdminRole();
        for (String role : roles) {
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(Integer.parseInt(role));
            adminRoleMapper.insertSelective(adminRole);
        }
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("loginName")!=null && !"".equals(searchMap.get("loginName"))){
                criteria.andLike("loginName","%"+searchMap.get("loginName")+"%");
            }
            // 密码
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andLike("password","%"+searchMap.get("password")+"%");
            }
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }

}
