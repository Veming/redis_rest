package com.company.dao;

import com.company.enity.UserVO;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl_2 implements UserDao {
    Jedis jedis = new Jedis("localhost");
    int userAmount;

    UserDaoImpl_2(){
        if (jedis.exists("userAmount")){
            userAmount = Integer.parseInt(jedis.get("userAmount"));
        }
        else {
            jedis.set("userAmount", "0");
        }
    }

    @Override
    public List<UserVO> queryAll() {
        List<UserVO> users = new ArrayList<>();
        for (int i=1;i<=userAmount;i++){
            //judge whether the userid exists and availability
            if (jedis.exists("user:"+i+":available") &&
                    "true".equals(jedis.get("user:"+i+":available"))){
                UserVO user = new UserVO();
                user.setUserId(String.valueOf(i));
                user.setUsername(jedis.get("user"+i+"username"));
                user.setPassword(jedis.get("user"+i+"password"));
                user.setBirthday(jedis.get("user"+i+"birthday"));
                user.setUserico(jedis.get("user"+i+"userico"));

                users.add(user);
            }
        }
        return users;
    }

    @Override
    public List<UserVO> queryByName(String name) {
        return null;
    }

    @Override
    public UserVO queryById(String userId) {
        return null;
    }

    @Override
    public void insert(UserVO user) {

        String userId = String.valueOf(++userAmount);
        jedis.set("userAmount", userId);

        //make user
        user.setUserId(userId);
        String username = "user:"+user.getUserId()+":username";
        String password = "user:"+user.getUserId()+":password";
        String birthday = "user:"+user.getUserId()+":birthday";
        String userico = "user:"+user.getUserId()+":userico";
        String available = "user:"+user.getUserId()+":available";

        //save com.company.enity.UserVO.UserVO
        if (!user.getUsername().isEmpty())jedis.set(username,user.getUsername());
        if (!user.getPassword().isEmpty())jedis.set(password,user.getPassword());
        if (!user.getBirthday().isEmpty())jedis.set(birthday,user.getBirthday());
        if (!user.getUserico().isEmpty())jedis.set(userico,user.getUserico());
        jedis.set(available,"true");

        addInvertedIndex(username,"username",userId);
        addInvertedIndex(password,"password",userId);
        addInvertedIndex(birthday,"birthday",userId);
        addInvertedIndex(userico,"userico",userId);

    }

    @Override
    public void update(UserVO user) {

    }

    @Override
    public void delete(UserVO user) {

    }

    private void addInvertedIndex(String index, String type, String addr){
        //save Inverted Index
        //hash_table
        //index:value type:attr addr:id
        //eg: man username user.getUserId();
        if (jedis.hexists(index,type)){
            jedis.hset(index,type,addr);
        }else {
            String str = jedis.hget(index,type);
            str += "::"+addr;
            jedis.hset(index,type,addr);
        }
    }

    private void deleteInvertedIndex(String index, String type, String addr){
        if (jedis.hexists(index,type)){
            String str = jedis.hget(index,type);
            String []result = str.split("::");

            if (result.length == 1) jedis.hdel(index,type);

            for (String r:result){
                if (addr != r){
                    str += r+"::";
                }

            }
        }
    }
}
