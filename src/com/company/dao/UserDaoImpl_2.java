package com.company.dao;

import com.company.enity.UserVO;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl_2 implements UserDao {
    Jedis jedis;
    private static int userAmount;
    //userid 最好还是根据时间来设置 通过时间和自增长 生成伪随机数

    public UserDaoImpl_2(){
        jedis = new Jedis("localhost");
//        jedis.auth("0234kz9*l");
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
            UserVO user = queryById(""+i);
            if (null != user){
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public List<UserVO> queryByName(String name) {
        String str = jedis.hget(name,"username");
        String []results = str.split("::");
        List<UserVO> users = new ArrayList<>();

        for(String result:results){
            UserVO user = queryById(""+result);
            if (null != user){
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public UserVO queryById(String userId) {
        //judge whether the userid exists and availability
        if (jedis.exists("user:"+userId+":available") &&
                "true".equals(jedis.get("user:"+userId+":available"))){
            UserVO user = new UserVO();
            user.setUserId(String.valueOf(userId));
            user.setUsername(jedis.get("user:"+userId+":username"));
            user.setPassword(jedis.get("user:"+userId+":password"));
            user.setBirthday(jedis.get("user:"+userId+":birthday"));
            user.setUserico(jedis.get("user:"+userId+":userico"));
            return user;
        }
        else {
            return null;
        }
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

        UserVO oldUser = queryById(user.getUserId());
        if (null == oldUser || "".equals(user.getUserId()))return;

        updateParam(oldUser.getUsername(),user.getUsername(),"username",user.getUserId());

        updateParam(oldUser.getBirthday(),user.getBirthday(),"birthday",user.getUserId());

        updateParam(oldUser.getPassword(),user.getPassword(),"password",user.getUserId());

        updateParam(oldUser.getUserico(),user.getUserico(),"userico",user.getUserId());
    }

    @Override
    public void delete(UserVO user) {
        if (jedis.exists("user:"+user.getUserId()+":available") &&
                "true".equals(jedis.get("user:"+user.getUserId()+":available"))){
            user = queryById(user.getUserId());

            jedis.set("user:"+user.getUserId()+":available","false");

            deleteInvertedIndex(user.getUsername(),"username",user.getUserId());
            deleteInvertedIndex(user.getBirthday(),"birthday",user.getUserId());
            deleteInvertedIndex(user.getUserico(),"userico",user.getUserId());
        }
    }

    private void addInvertedIndex(String index, String type, String addr){
        //save Inverted Index
        //hash_table
        //index:value type:attr addr:id
        //eg: man username user.getUserId();
        System.out.println("index:"+index);
        System.out.println("type:"+type);
//        System.out.println(jedis.hexists(index,type));
        if (jedis.hexists(index,type)){
            String str = jedis.hget(index,type);
            str += "::"+addr;
            jedis.hset(index,type,str);
        }else {
            jedis.hset(index,type,addr);
        }
    }

    private void deleteInvertedIndex(String index, String type, String addr){
        if (index != null &&
                !"".equals(index) &&
                jedis.hexists(index,type)){
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

    private void updateParam(String oldParam, String param,String type, String addr){
        if (null == oldParam && null == param){
            return;
        }
        else if (null == oldParam && null != param){
            jedis.set("user:"+addr+":"+type,param);
            addInvertedIndex(param, type, addr);
        }
        else if (null != oldParam && null == param){
            jedis.del("user:"+addr+":"+type);
            deleteInvertedIndex(oldParam, type, addr);
        }
        else if (null != oldParam && null != param){
            if (oldParam.equals(param)){
                jedis.set("user:"+addr+":"+type,param);

                if ("password".equals(type))return;

                deleteInvertedIndex(oldParam, type, addr);
                addInvertedIndex(param, type, addr);
            }
        }
    }
}
