package cn.reve.controller.system;

import cn.reve.utils.BCrypt;
import com.alibaba.dubbo.config.annotation.Reference;
import cn.reve.entity.PageResult;
import cn.reve.entity.Result;
import cn.reve.pojo.system.Admin;
import cn.reve.service.system.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;

    @PostMapping("/addAdminRole")
    public Result addAdminRole(@RequestBody Map<String, Object> admin){
        String password = (String) admin.get("password");
        String gensalt = BCrypt.gensalt();
        password = BCrypt.hashpw(password, gensalt);
        System.out.println("del after ok"+password);
        admin.put("password", password);
        return new Result();
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