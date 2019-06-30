package cn.reve.service.impl;
import cn.reve.utils.MapperUtils;
import cn.reve.utils.MethodUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.reve.dao.UserMapper;
import cn.reve.entity.PageResult;
import cn.reve.pojo.user.User;
import cn.reve.service.user.UserService;
import org.omg.SendingContext.RunTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private String sufPrompt = "please check again and then try again later.";

    private String verifyCode = "verifyCode";

    private String count = "count";

    private String avoid = "avoid";

    private String phoneNumIsNull = "The phone number shouldn't empty, ";

    private String phoneNumUsed = "The phone number had been used, ";

    private String codeIsNull = "The verify code shouldn't be empty, ";

    /**
     * 返回全部记录
     * @return
     */
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<User> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<User> users = (Page<User>) userMapper.selectAll();
        return new PageResult<User>(users.getTotal(),users.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<User> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return userMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<User> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<User> users = (Page<User>) userMapper.selectByExample(example);
        return new PageResult<User>(users.getTotal(),users.getResult());
    }

    /**
     * 根据Id查询
     * @param username
     * @return
     */
    public User findById(String username) {
        return userMapper.selectByPrimaryKey(username);
    }

    /**
     * 新增
     * @param user
     */
    public void add(User user) {
        userMapper.insert(user);
    }

    /**
     * 修改
     * @param user
     */
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     *  删除
     * @param username
     */
    public void delete(String username) {
        userMapper.deleteByPrimaryKey(username);
    }

    @Override
    public void spawnCode(String phoneNum) {
        //the method of getting the verify code and when check the phone num
        //check if this phone has been used
        phoneIsUsed(phoneNum, phoneNumUsed);
        Object tempCount = redisTemplate.boundValueOps(count + verifyCode + phoneNum).get();
        Integer countTime = 0;
            if(tempCount!=null){
                if(tempCount instanceof Integer) {
                    countTime = (Integer) tempCount;
                    countTime++;
                    //make sure user cannot continue to send code,
                        Object countAvoidTemp = redisTemplate.boundValueOps(count + avoid + verifyCode + phoneNum).get();
                        if (countAvoidTemp == null) {
                            redisTemplate.boundValueOps(count + avoid + verifyCode + phoneNum).set(0);
                            redisTemplate.boundValueOps(count + avoid + verifyCode + phoneNum).expire(24, TimeUnit.HOURS);
                        } else if (countAvoidTemp instanceof Integer) {
                            Integer countAvoidTime = (Integer)countAvoidTemp;
                            //send 5 times in 24 hours, then cannot send code again in 24 hours
                            if(countAvoidTime>=5) {
                                throw new RuntimeException("Operation is too frequently with phone number " + phoneNum + " today, please try again after 24 hours");
                            }else{
                                countAvoidTime++;
                                redisTemplate.boundValueOps(count+avoid+verifyCode+phoneNum).set(countAvoidTime);
                                redisTemplate.boundValueOps(count+avoid+verifyCode+phoneNum).expire(24,TimeUnit.HOURS);
                            }
                        } else{
                            //if the type is not required, then remove it. Most of time, this will not be run
                            redisTemplate.delete(count+avoid+verifyCode+phoneNum);
                            throw new RuntimeException("Invalid value of countAvoidTime is existed in redis, and it has been removed automatically, please try again later");
                        }
                        //
                    if (countTime >= 3) {
                        throw new RuntimeException("Operation is too frequently, please try again after 5 minutes.");
                    }
                    redisTemplate.boundValueOps(count + verifyCode + phoneNum).set(countTime);
                    redisTemplate.boundValueOps(count + verifyCode + phoneNum).expire(5, TimeUnit.MINUTES);
                }else{
                    //if the class type is not required, then remove it. Most of time, this won't be run
                    redisTemplate.delete(count+verifyCode+phoneNum);
                    throw new RuntimeException("Invalid value of countTime is existed in redis, and it has been removed automatically, please try again later");
                }
            }

        String code = MethodUtils.generalRandomNum(6, 10);
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", phoneNum);
        map.put("code", code);
        String jsonString = JSON.toJSONString(map);
        //save to RabbitMQ
        rabbitTemplate.convertAndSend("", "yoka", jsonString);
        //save to redis
        //the key must be different when saving to the redis while the user is different
        //The phone number can make sure the key is unique
        redisTemplate.boundValueOps(verifyCode+phoneNum).set(code);
        redisTemplate.boundValueOps(verifyCode+phoneNum).expire(5, TimeUnit.MINUTES);
        if(countTime==0) {
            redisTemplate.boundValueOps(count + verifyCode + phoneNum).set(0);
            redisTemplate.boundValueOps(count + verifyCode + phoneNum).expire(5, TimeUnit.MINUTES);
        }
    }

    @Override
    public void addUser(User user, String code) {
        isNull(code, codeIsNull);
        String phoneNum = user.getPhone();
        isNull(phoneNum, phoneNumIsNull);
        phoneIsUsed(phoneNum, phoneNumUsed);

        String verifyCode = (String)redisTemplate.boundValueOps(this.verifyCode+phoneNum).get();
        isNull(verifyCode, "The verify code is expired or it hasn't been sent with the phone number "+phoneNum+" ,");
        isNotEqual(verifyCode, code, "The verify code is invalid, ");
            //init part of values
            Date date = new Date();
            user.setCreated(date);
            user.setUpdated(date);
            String username = user.getUsername();
            if(username==null || "".equals(username)){
                user.setUsername(user.getPhone());
            }
            user.setUserLevel(0);
            user.setStatus("1");
            userMapper.insertSelective(user);
    }

    @Override
    public void checkCode(String code, String phoneNum) {
        isNull(code, codeIsNull);
        isNull(phoneNum, phoneNumIsNull);
        phoneIsUsed(phoneNum, phoneNumUsed);

        String verifyCode = (String)redisTemplate.boundValueOps(this.verifyCode + phoneNum).get();
        isNull(verifyCode, "The verify code is not existed or had been expired, ");
        isNotEqual(verifyCode, code, "The verify code is invalid, ");
    }

    @Override
    public void checkPhone(String phoneNum) {
        isNull(phoneNum, phoneNumIsNull);
        phoneIsUsed(phoneNum, phoneNumUsed);
    }

    private void isNull(String value, String prePrompt){
        if(null==value || "".equals(value)){
            throw new RuntimeException(prePrompt+sufPrompt);
        }
    }

    private void phoneIsUsed(String phoneNum, String prePrompt){
        Example example = MapperUtils.andEqualToWithSingleValue(User.class, "phone", phoneNum);
        if(userMapper.selectCountByExample(example)>0){
            throw new RuntimeException(prePrompt+sufPrompt);
        }
    }

    private void isNotEqual(String offSet, String desc, String prePrompt){
        if(!offSet.equals(desc)){
            throw new RuntimeException(prePrompt+sufPrompt);
        }
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
            }
            // 密码，加密存储
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andLike("password","%"+searchMap.get("password")+"%");
            }
            // 注册手机号
            if(searchMap.get("phone")!=null && !"".equals(searchMap.get("phone"))){
                criteria.andLike("phone","%"+searchMap.get("phone")+"%");
            }
            // 注册邮箱
            if(searchMap.get("email")!=null && !"".equals(searchMap.get("email"))){
                criteria.andLike("email","%"+searchMap.get("email")+"%");
            }
            // 会员来源：1:PC，2：H5，3：Android，4：IOS
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                criteria.andLike("sourceType","%"+searchMap.get("sourceType")+"%");
            }
            // 昵称
            if(searchMap.get("nickName")!=null && !"".equals(searchMap.get("nickName"))){
                criteria.andLike("nickName","%"+searchMap.get("nickName")+"%");
            }
            // 真实姓名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 使用状态（1正常 0非正常）
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }
            // 头像地址
            if(searchMap.get("headPic")!=null && !"".equals(searchMap.get("headPic"))){
                criteria.andLike("headPic","%"+searchMap.get("headPic")+"%");
            }
            // QQ号码
            if(searchMap.get("qq")!=null && !"".equals(searchMap.get("qq"))){
                criteria.andLike("qq","%"+searchMap.get("qq")+"%");
            }
            // 手机是否验证 （0否  1是）
            if(searchMap.get("isMobileCheck")!=null && !"".equals(searchMap.get("isMobileCheck"))){
                criteria.andLike("isMobileCheck","%"+searchMap.get("isMobileCheck")+"%");
            }
            // 邮箱是否检测（0否  1是）
            if(searchMap.get("isEmailCheck")!=null && !"".equals(searchMap.get("isEmailCheck"))){
                criteria.andLike("isEmailCheck","%"+searchMap.get("isEmailCheck")+"%");
            }
            // 性别，1男，0女
            if(searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))){
                criteria.andLike("sex","%"+searchMap.get("sex")+"%");
            }

            // 会员等级
            if(searchMap.get("userLevel")!=null ){
                criteria.andEqualTo("userLevel",searchMap.get("userLevel"));
            }
            // 积分
            if(searchMap.get("points")!=null ){
                criteria.andEqualTo("points",searchMap.get("points"));
            }
            // 经验值
            if(searchMap.get("experienceValue")!=null ){
                criteria.andEqualTo("experienceValue",searchMap.get("experienceValue"));
            }

        }
        return example;
    }

}
