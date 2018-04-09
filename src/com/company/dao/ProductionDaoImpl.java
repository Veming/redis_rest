package com.company.dao;

import com.company.enity.Production;
import com.company.enity.UserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Array;
import java.util.*;

public class ProductionDaoImpl implements ProductionDao {

    Jedis jedis;
    private static int PNbr;
    //id 最好还是根据时间来设置 通过时间和自增长 生成伪随机数

    public ProductionDaoImpl(){
        jedis = new Jedis("localhost");
        if (jedis.exists("PNbr")){
            PNbr = Integer.parseInt(jedis.get("PNbr"));
        }
        else {
            jedis.set("PNbr", "0");
        }
    }

    @Override
    public boolean increase(Production production, UserVO user) {

        String TPNbr = jedis.get("production:"+production.getPid()+":amount");
        String name = "production:"+production.getPid()+":name";
        String price = "production:"+production.getPid()+":price";
        String amount = "production:"+production.getPid()+":amount";
        String brand = "production:"+production.getPid()+":brand";
        if ( TPNbr == null ){


            jedis.set(name,production.getName());
            jedis.set(price,production.getPrice());
            jedis.set(amount,production.getAmount());
            jedis.set(brand,production.getBrand());

        }
        else {
            int n = Integer.parseInt(production.getAmount());
            int m = Integer.parseInt(TPNbr);

            jedis.set(name,production.getName());
            jedis.set(price,production.getPrice());
            jedis.set(amount,String.valueOf(n+m));
            jedis.set(brand,production.getBrand());


        }
        String record = jedis.get("user:"+user.getUserId()+":record");

        if (record == null){
            ArrayList<String> PNbr = new ArrayList<>();
            PNbr.add(production.getAmount());
            Map<String,ArrayList<String>> TP = new HashMap<>();
            TP.put(production.getPid(),PNbr);
            Gson gson = new Gson();
            jedis.set("user:"+user.getUserId()+":record",gson.toJson(TP));
        }
        else {
            Map<String,ArrayList<String>> recordMap = new Gson().fromJson(record,new TypeToken<Map<String ,ArrayList<String>>>(){}.getType());
            ArrayList PNbr = recordMap.get(production.getPid());
            PNbr.add(production.getAmount());
            jedis.set("user:"+user.getUserId()+":record",new Gson().toJson(recordMap));
        }
        return true;


    }

    @Override
    public boolean decrease(Production production, UserVO user) {
        Production good = new Production();
        String amount = jedis.get("production:"+production.getPid()+":amount");
        if (amount == null)return false;
        else {
            if (Integer.parseInt(amount)<Integer.parseInt(production.getAmount()))return false;
            else {
                int nbr = Integer.parseInt(amount) - Integer.parseInt(production.getAmount());
                jedis.set("production:"+production.getPid()+":amount",String.valueOf(nbr));
                String record = jedis.get("user:"+user.getUserId()+":record");

                if (record == null){
                    ArrayList<String> PNbr = new ArrayList<>();
                    PNbr.add(production.getAmount());
                    Map<String,ArrayList<String>> TP = new HashMap<>();
                    TP.put(production.getPid(),PNbr);
                    Gson gson = new Gson();
                    jedis.set("user:"+user.getUserId()+":record",gson.toJson(TP));
                }
                else {
                    Map<String,ArrayList<String>> recordMap = new Gson().fromJson(record,new TypeToken<Map<String ,ArrayList<String>>>(){}.getType());
                    ArrayList PNbr = recordMap.get(production.getPid());
                    PNbr.add(production.getAmount());
                    jedis.set("user:"+user.getUserId()+":record",new Gson().toJson(recordMap));
                }
                return true;
            }
        }




    }


    @Override
    public List<Production> queryByUid(String userid) {

        String record = jedis.get("user:"+userid+":record");
        if (record == null){return null; }
        else {
            Map<String,ArrayList<String>> recordMap = new Gson().fromJson(record,new TypeToken<Map<String ,ArrayList<String>>>(){}.getType());
            Set<String> keySet = recordMap.keySet();
            ArrayList<Production> productions = new ArrayList<>();
            for (String key:keySet){
                productions.add(queryByPid(userid));
            }
            return productions;
        }
    }

    @Override
    public Production queryByPid(String Pid) {
        Production production = new Production();

        production.setPid(Pid);
        production.setName(jedis.get("production:"+Pid+":name"));
        production.setPrice(jedis.get("production:"+Pid+":price"));
        production.setAmount(jedis.get("production:"+Pid+":amount"));
        production.setBrand(jedis.get("production:"+Pid+":brand"));

        return production;

    }
}
