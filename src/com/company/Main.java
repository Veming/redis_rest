package com.company;

import com.company.dao.UserDao;
import com.company.dao.UserDaoImpl;
import com.company.dao.UserDaoImpl_2;
import com.company.enity.UserVO;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;

import java.util.LinkedHashSet;
import java.util.Set;

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
        Jedis jedis = new Jedis("localhost",7004);
        jedis.auth("0234kz9*l");

        jedis.select(1);
        System.out.println(jedis.get("name"));

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

//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //
//        Set<HostAndPort> nodes = new LinkedHashSet<>();
//        nodes.add(new HostAndPort("127.0.0.1",7003));
//        nodes.add(new HostAndPort("127.0.0.1",7004));
//        nodes.add(new HostAndPort("127.0.0.1",7005));
//        nodes.add(new HostAndPort("127.0.0.1",7006));
//
//        JedisCluster jedisCluster = new JedisCluster(nodes);




//        System.out.println(jedis.ping());


    }
}
