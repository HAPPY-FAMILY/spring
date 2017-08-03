package com.transam.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.transam.system.entity.User;


public class PasswordHelper {
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;

	public void encryptPassword(User user) {
		String salt=randomNumberGenerator.nextBytes().toHex();
		user.setCredentialsSalt(salt);
		String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getAccountName() + salt), hashIterations).toHex();
		user.setPassword(newPassword);
	}
	
	public String encryptPassword(String accountName,String password,String salt){
		String newPassword = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(accountName + salt), hashIterations).toHex();
		return newPassword;
	}
//	public static void main(String[] args) {
//		PasswordHelper passwordHelper = new PasswordHelper();
//		User user = new User();
//		user.setPassword("123456");
//		user.setAccountName("admin");
//		passwordHelper.encryptPassword(user);
//		System.out.println(ByteSource.Util.bytes("admin4157c3feef4a6ed91b2c28cf4392f2d1"));
//	}
}
