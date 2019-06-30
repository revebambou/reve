package cn.reve.controller.system;

import cn.reve.pojo.system.AdminRoleList;
import cn.reve.pojo.system.Role;
import cn.reve.pojo.user.User;
import cn.reve.utils.BCrypt;
import com.alibaba.dubbo.config.annotation.Reference;
import cn.reve.entity.PageResult;
import cn.reve.entity.Result;
import cn.reve.pojo.system.Admin;
import cn.reve.service.system.AdminService;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;

    @PostMapping("/addAdminRole")
    public Result addAdminRole(@RequestBody Admin admin,  String roleList, String memo){
        List<String> roles = JSON.parseObject(roleList, List.class);
        //bcrypt the password
        admin = preHandlePassword(admin);
        adminService.saveAdminRoleList(admin, roles, memo);
        return new Result();
    }

    @PostMapping("/updateAdminRole")
    public Result updateAdminRole(@RequestBody Admin admin, String roleList, String memo){
        List<String> roles = JSON.parseObject(roleList, List.class);
        admin = preHandlePassword(admin);
        adminService.updateAdminRole(admin, roles, memo);
        return new Result();
    }

    /**
     * if the password is not null or empty, it should be bcript
     * @param admin
     * @return
     */
    private Admin preHandlePassword(Admin admin){
        String password = admin.getPassword();
        if(null!=password && "".equals(password)){
            String gensalt = BCrypt.gensalt();
            String hashpw = BCrypt.hashpw(password, gensalt);
            admin.setPassword(hashpw);
        }
        return admin;
    }

    @GetMapping("/findAdminRoleById")
    public AdminRoleList findAdminRoleByAdminId(Integer adminId){
        return adminService.findAdminRoleByAdminId(adminId);
    }

    @GetMapping("/findAll")
    public List<Admin> findAll(){
        return adminService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Admin> findPage(int page, int size){
        return adminService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Admin> findList(@RequestBody Map<String,Object> searchMap){
        return adminService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Admin> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  adminService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Admin findById(Integer id){
        return adminService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.update(admin);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        adminService.delete(id);
        return new Result();
    }

}
