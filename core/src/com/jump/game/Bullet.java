package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    private Texture texture;
    private float x, y;
    private float speed = 400; // Vitesse de la balle

    public Bullet(float x, float y) {
        texture = new Texture("bullet.png"); // Assurez-vous d'avoir une texture pour la balle
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public boolean isOffScreen() {
        return x > Gdx.graphics.getWidth();
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean collidesWithPlayer(Player player) {
        Rectangle bulletRectangle = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        Rectangle playerRectangle = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return bulletRectangle.overlaps(playerRectangle);
    }
}
