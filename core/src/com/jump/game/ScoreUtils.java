package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScoreUtils {

    public static void savePlayerScore(PlayerScore score) {
        FileHandle file = Gdx.files.local("scores.txt");
        try {
            file.writeString(score.getPseudo() + "," + score.getDistance() + "," + score.getMoney() + "\n", true); // 'true' pour append
        } catch (Exception e) {
            System.err.println("Erreur lors de l'écriture dans scores.txt : " + e.getMessage());
        }
    }


    public static List<PlayerScore> loadPlayerScores() {
        List<PlayerScore> scores = new ArrayList<>();
        FileHandle file = Gdx.files.local("scores.txt");

        if (!file.exists()) {
            System.out.println("Le fichier scores.txt n'existe pas");
            return scores;
        }

        try {
            String[] lines = file.readString().split("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String pseudo = parts[0];
                    if (pseudo == null || pseudo.equals("null") || pseudo.trim().isEmpty()) {
                        System.out.println("Pseudo invalide détecté: " + line);
                        continue;
                    }
                    try {
                        float distance = Float.parseFloat(parts[1].trim().replace(',', '.'));
                        int money = Integer.parseInt(parts[2].trim());
                        scores.add(new PlayerScore(pseudo, distance, money));
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur de format dans la ligne: " + line);
                    }
                } else {
                    System.out.println("Format de ligne incorrect: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

}

