package com.kh.ajax.model.vo;

// 2022.2.23(수) 15h40
public class Member {
	private String userId;
	private String userPwd;
	private String name;
	private int age;
	private String phone;
	
	public Member() {
		super();
	}

	public Member(String userId, String userPwd, String name, int age, String phone) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.name = name;
		this.age = age;
		this.phone = phone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Member [userId=" + userId + ", userPwd=" + userPwd + ", name=" + name + ", age=" + age + ", phone="
				+ phone + "]";
	}

}
