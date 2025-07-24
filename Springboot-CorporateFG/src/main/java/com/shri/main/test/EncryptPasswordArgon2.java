package com.shri.main.test;
import com.shri.main.security.Argon2PasswordUtil;

public class EncryptPasswordArgon2 {

	public static void main(String[] args) { 
		String rawPassword = "Shrikant!1006";
		String encryptedPassword = Argon2PasswordUtil.hashPassword(rawPassword);
        System.out.println("Encrypted Password for '" + rawPassword + "':");
		System.out.println(encryptedPassword);
	}
}
