package com.company.dao;

import com.company.enity.UserVO;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    Jedis jedis = new Jedis("localhost");
    @Override
    public List<UserVO> queryAll() {
        List<String> strUsers =  jedis.lrange("user",0,jedis.llen("user"));
        ArrayList<UserVO> users = new ArrayList<>();

        for (String strUser:strUsers){
            String[] str = strUser.split("##");
            UserVO user = new UserVO();
            user.setUserId(str[0]);
            user.setUsername(str[1]);
            user.setPassword(str[2]);
            user.setBirthday(str[3]);
            users.add(user);
        }
        return users;

    }

    @Override
    public List<UserVO> queryByName(String Name) {
        List<UserVO> users = queryAll();
        List<UserVO> theUser = new ArrayList<>();

        for (UserVO user:users){
            if (Name.equals(user.getUsername())){
                theUser.add(user);
            }
        }
        return theUser;
    }

    @Override
    public UserVO queryById(String userId) {
        List<UserVO> users = queryAll();
        int id = Integer.parseInt(userId);
        return users.get(id);
    }

    @Override
    public void insert(UserVO user) {
        Jedis jedis = new Jedis("localhost");
        jedis.rpush("user",jedis.llen("user")+"##"+user.getUsername()+"##"+user.getPassword()+"##"+user.getBirthday());
    }

    @Override
    public void update(UserVO user) {
        jedis.lset("user",Integer.parseInt(user.getUserId()),user.getUserId()+"##"+user.getUsername()+"##"+user.getPassword()+"##"+user.getBirthday());
    }

    @Override
    public void delete(UserVO user) {
        jedis.lset("user",Integer.parseInt(user.getUserId()),"null##null##null##null");
    }
}
