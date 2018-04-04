package com.company.enity;

public class UserVO {
//	user:id:  attribute
	
	private String userId;
	private String username;
	private String password;
	private String birthday;
	private String userico;
	private String available;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUserico() {
		return userico;
	}

	public void setUserico(String userico) {
		this.userico = userico;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}
}
