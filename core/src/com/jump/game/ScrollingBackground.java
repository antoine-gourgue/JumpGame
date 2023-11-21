package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScrollingBackground {
    private Texture texture;
    private float x1, x2;
    private float speed = 100; // Vitesse de défilement

    public ScrollingBackground() {
        texture = new Texture("background.png"); // Assurez-vous d'avoir une texture d'arrière-plan
        x1 = 0;
        x2 = texture.getWidth();
    }

    public void update() {
        x1 -= speed * Gdx.graphics.getDeltaTime();
        x2 -= speed * Gdx.graphics.getDeltaTime();

        // Quand une image sort de l'écran, elle est remise à la suite de l'autre
        if (x1 + texture.getWidth() <= 0) x1 = x2 + texture.getWidth();
        if (x2 + texture.getWidth() <= 0) x2 = x1 + texture.getWidth();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x1, 0);
        batch.draw(texture, x2, 0);
    }
}
