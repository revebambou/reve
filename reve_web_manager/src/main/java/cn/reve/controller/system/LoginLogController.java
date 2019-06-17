package cn.reve.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import cn.reve.entity.PageResult;
import cn.reve.entity.Result;
import cn.reve.pojo.system.LoginLog;
import cn.reve.service.system.LoginLogService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Reference
    private LoginLogService loginLogService;

    @GetMapping("/findAll")
    public List<LoginLog> findAll(){
        return loginLogService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<LoginLog> findPage(int page, int size){
        return loginLogService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<LoginLog> findList(@RequestBody Map<String,Object> searchMap){
        return loginLogService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<LoginLog> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  loginLogService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public LoginLog findById(Integer id){
        return loginLogService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody LoginLog loginLog){
        loginLogService.add(loginLog);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody LoginLog loginLog){
        loginLogService.update(loginLog);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        loginLogService.delete(id);
        return new Result();
    }

}
