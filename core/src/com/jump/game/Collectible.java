package com.jump.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Collectible {
    private Texture texture;
    private float x, y;
    private float speedX;

    public Collectible(Texture texture, float x, float y, float speedX) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float deltaTime) {
        x -= speedX * deltaTime; // Déplace le collectible vers la gauche
    }
}
