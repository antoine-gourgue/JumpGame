package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.jump.game.Bullet;


import java.util.ArrayList;

public class Player {
    private Texture texture1;
    private Texture texture2;
    private Texture texture3;
    private Texture texture4;
    private Texture texture5;
    private Texture texture6;
    private float x, y;
    private float velocityY = 0;
    private boolean isJumping = false;
    private final float GRAVITY = -20;
    private final float JUMP_VELOCITY = 450;
    private boolean isHit = false;
    private float animationTime = 0;
    private float animationInterval = 0.1f;
    private float width;
    private float height;
    private float distance = 0;
    private float speed = 100;
    private int money;
    private String pseudo;


    public Player() {
        texture1 = new Texture("1_terrorist_1_Run_000.png");
        texture2 = new Texture("1_terrorist_1_Run_001.png");
        texture3 = new Texture("1_terrorist_1_Run_002.png");
        texture4 = new Texture("1_terrorist_1_Run_003.png");
        texture5 = new Texture("1_terrorist_1_Run_004.png");
        texture6 = new Texture("1_terrorist_1_Run_005.png");
        x = 800;
        y = 200;
        width = 110;
        height = 160;
        money = 0;
    }


    public void update() {
        // Mise à jour de la position horizontale

        // Gestion des entrées pour le saut
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            velocityY = JUMP_VELOCITY;
        }

        // Appliquer la gravité
        if (isJumping) {
            velocityY += GRAVITY;
            y += velocityY * Gdx.graphics.getDeltaTime();

            // Arrêter le saut lorsque le joueur touche le sol
            if (y <= 200) {
                y = 200;
                isJumping = false;
                velocityY = 0;
            }
        }
        if (!isJumping) {
            animationTime += Gdx.graphics.getDeltaTime();
        }
        distance += (speed * Gdx.graphics.getDeltaTime()) / 100.0f;
    }

    public void draw(SpriteBatch batch) {
        Texture currentTexture;
        if (!isJumping) {
            // Sélectionner la texture en fonction du temps d'animation
            int textureIndex = (int)(animationTime / animationInterval) % 6;
            switch (textureIndex) {
                case 0:
                    currentTexture = texture1;
                    break;
                case 1:
                    currentTexture = texture2;
                    break;
                case 2:
                    currentTexture = texture3;
                    break;
                case 3:
                    currentTexture = texture4;
                    break;
                case 4:
                    currentTexture = texture5;
                    break;
                default:
                    currentTexture = texture6;
                    break;
            }
        } else {
            // Texture à utiliser lors du saut
            currentTexture = texture1; // ou une autre texture spécifique pour le saut
        }

        batch.draw(currentTexture, x, y, width, currentTexture.getHeight());
    }


    public Texture getTexture() {
        return texture1;
    }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return this.width;
    }
    public float getY() {
        return y;
    }

    public float getHeight(){
        return texture5.getHeight();
    }

    public boolean isHitByBullet(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (bullet.collidesWithPlayer(this)) {
                return true; // Le joueur est touché par une balle
            }
        }
        return false; // Le joueur n'est pas touché par une balle
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public void reset() {
        x = 800;
        y = 200;

        isHit = false;
        distance = 0;
        money = 0;

    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public float getDistance() {
        return distance;
    }
    public void addMoney(int amount) {
        money += amount;
    }

    public int getMoney() {
        return money;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // Assurez-vous que width et height sont définis
    }
}
