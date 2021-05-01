package com.uqac.wesplit.helpers;

import java.util.Random;

/**
 Classe permettant de générer des tokens aléatoires
 */

public class TokenGenerator {

    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    /**
     Génére un token aléatoire
     @param length longueur demandée
     @return le token aléatoire
     */

    public static String getRandomToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }
}
