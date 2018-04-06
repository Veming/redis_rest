package com.company;

import com.company.dao.UserDao;
import com.company.dao.UserDaoImpl;
import com.company.dao.UserDaoImpl_2;
import com.company.enity.UserVO;
import redis.clients.jedis.Jedis;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        UserDao userDao = new UserDaoImpl_2();
//
//        UserVO user = new UserVO();
//
//
//        user.setUserId("5");
//        user.setUsername("dome");
//        user.setPassword("123123123");
//        user.setBirthday("1970-1-1");
//        user.setUserico("/url");
//
//
        Jedis jedis = new Jedis("localhost",7003);
        jedis.auth("0234kz9*l");
//        userDao.delete(user);
//        System.out.println(jedis.hexists("angelart","username"));
//
//        userDao.insert(user);
//        userDao.update(user);
//        for (UserVO usr:userDao.queryAll()){
//            System.out.println("UserID:"+usr.getUserId()+"\tUsername:"+usr.getUsername()+
//                    "\tUserPassword:"+usr.getPassword()+"\tBirthday:"+usr.getBirthday()+
//                    "\tuserico:"+usr.getUserico());
//        }

        System.out.println(jedis.ping());


    }
}
