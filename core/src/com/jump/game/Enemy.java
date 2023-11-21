package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Enemy {
    private Texture texture;
    private float x, y;
    private ArrayList<Bullet> bullets;
    private float timeSinceLastShot = 0;
    private Sound shootSound;
    private static final float INITIAL_SHOOT_INTERVAL = 3.0f;
    private static final float INCREASED_SHOOT_INTERVAL = 1.0f;
    private static final float DISTANCE_THRESHOLD = 50.0f;

    public Enemy(Sound shootSound) {
        texture = new Texture("1_police_attack_Attack2_000.png");
        x = 100;
        y = 200;
        bullets = new ArrayList<Bullet>();
        this.shootSound = shootSound;
    }

    public void update(float playerDistance) {
        // Tirer des balles à intervalles réguliers
        float shootInterval = playerDistance >= DISTANCE_THRESHOLD ? INCREASED_SHOOT_INTERVAL : INITIAL_SHOOT_INTERVAL;
        Random random = new Random();

        // Mise à jour du temps depuis le dernier tir
        timeSinceLastShot += Gdx.graphics.getDeltaTime();

        if (timeSinceLastShot >= shootInterval) {
            int numberOfBullets = random.nextInt(3) + 1; // Générer un nombre aléatoire entre 1 et 3
            float timeBetweenBullets = 0.1f; // Temps entre chaque balle en secondes

            for (int i = 0; i < numberOfBullets; i++) {
                // Planifier le tir de chaque balle
                final int bulletIndex = i;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        bullets.add(new Bullet(x + texture.getWidth(), y + 10));
                    }
                }, timeBetweenBullets * bulletIndex);
            }
            timeSinceLastShot = 0;
            shootSound.play(); // Jouer le son du tir
        }

        // Mettre à jour les balles
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update();
            if (bullet.isOffScreen()) {
                iter.remove();
            }
        }
    }


    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    public void reset() {
        x = 100;
        y = 200;
        bullets.clear();
    }
}
