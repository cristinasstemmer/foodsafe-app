package com.foodsafe.foodsafeapp.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

    // Gera o hash de uma senha
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Verifica se a senha corresponde ao hash
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
