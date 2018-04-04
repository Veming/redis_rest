package com.company.dao;

import com.company.enity.UserVO;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl_2 implements UserDao {
    Jedis jedis;
    int userAmount;

    public UserDaoImpl_2(){
        jedis = new Jedis("localhost");
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
                user.setUsername(jedis.get("user:"+i+":username"));
                user.setPassword(jedis.get("user:"+i+":password"));
                user.setBirthday(jedis.get("user:"+i+":birthday"));
                user.setUserico(jedis.get("user:"+i+":userico"));


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
        if (!"".equals(user.getUsername()) || null != user.getUsername()){
            jedis.set(username,user.getUsername());
            addInvertedIndex(user.getUsername(),"username",userId);

        }
        if (!"".equals(user.getPassword()) || null != user.getPassword()){
            jedis.set(password,user.getPassword());
        }
        if (!"".equals(user.getBirthday()) || null != user.getBirthday()){
            jedis.set(birthday,user.getBirthday());
            addInvertedIndex(user.getBirthday(),"birthday",userId);
        }
        if (!"".equals(user.getUserico()) && null != user.getUserico()){
            jedis.set(userico,user.getUserico());
            addInvertedIndex(user.getUserico(),"userico",userId);
        }

        jedis.set(available,"true");

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
        System.out.println("index:"+index);
        System.out.println("type:"+type);
        System.out.println(jedis.hexists(index,type));
        if (jedis.hexists(index,type)){
            String str = jedis.hget(index,type);
            str += "::"+addr;
            jedis.hset(index,type,str);
        }else {
            jedis.hset(index,type,addr);
        }
    }

    private void deleteInvertedIndex(String index, String type, String addr){
        if (jedis.hexists(index,type)){
            String str = jedis.hget(index,type);
            String []result = str.split("::");

            if (result.length == 1) {
                jedis.hdel(index,type);
            }
            else if (result.length == 2){
                if (result[0].equals(addr)){
                    str = result[1];
                }else {
                    str = result[0];
                }
            }
            else{
                int i=1;
                if (result[0].equals(addr)){
                    i++;
                    str = ""+result[1];
                }
                else {
                    str = ""+result[0];
                }

                for (; i<result.length; i++){
                    if (!result[i].equals(addr))str += "::" + result[i];
                }
            }

            jedis.hset(index,type,str);

        }
    }
}
