package jgm.tiendaVirtual.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;










public class PasswordGenerator {
    
    
    
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("123456Jorge?");
        System.out.println("Contraseña encriptada: " + encodedPassword);
    }
}
