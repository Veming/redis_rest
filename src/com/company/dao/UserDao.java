package com.company.dao;

import java.util.List;

import com.company.enity.UserVO;

public interface UserDao {
	public List<UserVO> queryAll();
	
	public List<UserVO> queryByName(String name);
	
	public UserVO queryById(String userId);

	public void insert(UserVO user);
	
	public void update(UserVO user);
	
	public void delete(UserVO user);
	

}
