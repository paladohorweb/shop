package jgm.tiendaVirtual.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        // Generar una clave secreta aleatoria
        String secretKey = Base64.getEncoder().encodeToString(
                Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );

        System.out.println("Clave secreta en Base64:");
        System.out.println(secretKey);
    }
}

