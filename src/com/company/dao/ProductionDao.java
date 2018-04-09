package com.company.dao;

import com.company.enity.Production;
import com.company.enity.UserVO;

import java.util.List;

public interface ProductionDao {
    public boolean decrease(Production production , UserVO user);

    public void increase(Production production, UserVO user);

    public List<Production> queryByUid(String userid);

    public Production queryByPid(String userid);
}
