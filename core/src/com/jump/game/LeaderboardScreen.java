package com.jump.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardScreen {
    private List<PlayerScore> scores;
    private BitmapFont font;
    private SpriteBatch batch;
    private GlyphLayout layout = new GlyphLayout();
    private Stage stage;
    private Skin skin;
    private JumpGame game;
    private int currentPage = 0;
    private static final int SCORES_PER_PAGE = 30;

    public LeaderboardScreen(JumpGame game) {
        this.game = game;
        this.scores = ScoreUtils.loadPlayerScores(); // Charge les scores depuis le fichier
        Collections.sort(this.scores, scoreComparator); // Tri des scores en utilisant Collections.sort
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Assurez-vous d'avoir un skin
        createBackButton();
        createPaginationButtons();
    }
    private void createPaginationButtons() {
        TextButton nextButton = new TextButton("Suivant", skin);
        nextButton.setSize(200, 50);
        nextButton.setPosition(Gdx.graphics.getWidth() - 800, 50); // Position ajustée
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((currentPage + 1) * SCORES_PER_PAGE < scores.size()) {
                    currentPage++;
                    updateDisplayedScores();
                }
            }
        });

        TextButton prevButton = new TextButton("Precedent", skin);
        prevButton.setSize(200, 50);
        prevButton.setPosition(600, 50); // Position ajustée
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentPage > 0) {
                    currentPage--;
                    updateDisplayedScores();
                }
            }
        });

        stage.addActor(nextButton);
        stage.addActor(prevButton);
    }

    private void updateDisplayedScores() {
        // Effacez les scores actuellement affichés
        // Affichez les scores pour la currentPage
        int start = currentPage * SCORES_PER_PAGE;
        int end = Math.min((currentPage + 1) * SCORES_PER_PAGE, scores.size());
        for (int i = start; i < end; i++) {
            PlayerScore score = scores.get(i);
            // Affichez le score (par exemple, sous forme de texte)
        }
    }
    private void createBackButton() {
        TextButton backButton = new TextButton("Retour", skin);
        backButton.setSize(200, 50);
        backButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 100, 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeState(JumpGame.GameState.MENU);
            }
        });

        stage.addActor(backButton);
    }

    public void render(float deltaTime) {
        loadAndSortScores();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.getCurrentState() == JumpGame.GameState.LEADERBOARD) {
            Gdx.input.setInputProcessor(stage);
        }

        batch.begin();

        // Calcul de l'index de départ et de fin pour l'affichage des scores
        int start = currentPage * SCORES_PER_PAGE;
        int end = Math.min((currentPage + 1) * SCORES_PER_PAGE, scores.size());

        // Log pour déboguer la plage des scores affichés
        Gdx.app.log("LeaderboardScreen", "Affichage des scores, page: " + currentPage + ", plage: " + start + " à " + end);

        // Position de départ pour l'affichage des scores
        int y = Gdx.graphics.getHeight() - 50; // Ajustez cette valeur selon votre interface

        for (int i = start; i < end; i++) {
            PlayerScore score = scores.get(i);
            int rank = i + 1; // Ajoute 1 car les indices de liste commencent à 0
            String text = rank + ". Pseudo: " + score.getPseudo() + ", Distance: " + String.format("%.2f", score.getDistance()) + " m, Argent: " + score.getMoney() + " pièces";
            layout.setText(font, text);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2; // Calcule la position x pour centrer le texte
            font.draw(batch, text, x, y);
            y -= 30; // Décale chaque ligne vers le bas pour le prochain score

            // Log pour chaque score affiché
            Gdx.app.log("LeaderboardScreen", "Score " + rank + ": " + text);
        }

        batch.end();

        stage.act(deltaTime);
        stage.draw();
    }



    private Comparator<PlayerScore> scoreComparator = new Comparator<PlayerScore>() {
        @Override
        public int compare(PlayerScore score1, PlayerScore score2) {
            // Comparaison primaire par distance
            int distanceCompare = Float.compare(score2.getDistance(), score1.getDistance());
            if (distanceCompare != 0) {
                return distanceCompare;
            }

            // Comparaison secondaire par argent
            return Integer.compare(score2.getMoney(), score1.getMoney());
        }
    };
    private void loadAndSortScores() {
        this.scores = ScoreUtils.loadPlayerScores(); // Recharge les scores
        if (scores == null || scores.isEmpty()) {
            Gdx.app.log("LeaderboardScreen", "Aucun score chargé ou liste des scores vide");
        } else {
            Gdx.app.log("LeaderboardScreen", "Nombre total de scores chargés: " + scores.size());
            Collections.sort(this.scores, scoreComparator); // Tri des scores
        }
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}

