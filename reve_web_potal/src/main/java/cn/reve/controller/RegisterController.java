package cn.reve.controller;

import cn.reve.entity.Result;
import cn.reve.pojo.user.User;
import cn.reve.service.user.UserService;
import cn.reve.utils.BCrypt;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Reference
    private UserService userService;

    @GetMapping("/spawnCode")
    public Result spawnCode(String phoneNum){
        userService.spawnCode(phoneNum);
        return new Result();
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user, String code){
        String password = user.getPassword();
        if(password!=null && !"".equals(password)){
            String gensalt = BCrypt.gensalt();
            password = BCrypt.hashpw(password, gensalt);
        }
        user.setPassword(password);
        userService.addUser(user, code);
        return new Result();
    }
}
