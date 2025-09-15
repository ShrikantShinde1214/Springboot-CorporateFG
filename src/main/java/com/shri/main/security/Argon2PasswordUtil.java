package com.shri.main.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2PasswordUtil {

    public static String hashPassword(String plainPassword) {
        Argon2 argon2 = Argon2Factory.create();
        return argon2.hash(2, 65536, 1, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        Argon2 argon2 = Argon2Factory.create();
        return argon2.verify(hashedPassword, plainPassword.toCharArray());
    }
}
