package com.jump.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.Iterator;

public class JumpGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	Enemy enemy;
	ScrollingBackground scrollingBackground;
	private BitmapFont font;
	private boolean gameOver = false;
	private Texture gameOverTexture;
	private Texture winTexture;
	Music backgroundMusic;
	private Sound shootSound;
	Music gameOverMusic;
	private ArrayList<Collectible> collectibles;
	private float distancePourNouveauCollectible = 10.0f;
	private float dernierCollectibleGenere = 0.0f;
	private int collectedItems = 0;
	private final int WINNING_COLLECTIBLE_COUNT = 10;
	private boolean playerWon = false;
	private Stage stage;
	private boolean inMenu = true;
	enum GameState {
		MENU,
		ENTER_PSEUDO,
		PLAYING,
		LEADERBOARD
	}

	private GameState currentState = GameState.MENU;
	private TextField pseudoInputField;
	private LeaderboardScreen leaderboardScreen;
	private ArrayList<PlayerScore> playerScores;
	private Stage pseudoStage;
	private String playerPseudo;


	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		createMenu();
		pseudoStage = new Stage();
		createPseudoInputField();
		batch = new SpriteBatch();
		player = new Player();
		shootSound = Gdx.audio.newSound(Gdx.files.internal("shootsound.mp3"));
		enemy = new Enemy(shootSound);
		scrollingBackground = new ScrollingBackground();
		font = new BitmapFont();
		gameOver = false;
		gameOverTexture = new Texture("gameover.png");
		winTexture = new Texture("WinTexture.png");
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		backgroundMusic.setVolume(0.1f);
		backgroundMusic.play();
		backgroundMusic.setLooping(true);
		gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameover_music.mp3"));
		gameOverMusic.setVolume(0.5f);
		collectibles = new ArrayList<Collectible>();
		collectibles.add(new Collectible(new Texture("collectible.png"), 1900, 200,200));
		playerScores = new ArrayList<>();
	}
	public void addPlayerScore(String pseudo, float distance, int money) {
		playerScores.add(new PlayerScore(pseudo, distance, money));
	}
	public void savePlayerScore(PlayerScore score) {
		FileHandle file = Gdx.files.local("scores.txt");
		String scoreData = score.getPseudo() + "," + score.getDistance() + "," + score.getMoney() + "\n";

		// Écriture des données dans le fichier
		file.writeString(scoreData, true); // 'true' pour ajouter à la fin du fichier
	}
	private void createMenu() {
		Texture backgroundTexture = new Texture(Gdx.files.internal("BackgroundImage.png"));
		Image backgroundImage = new Image(backgroundTexture);
		backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundImage.setPosition(0, 0);
		stage.addActor(backgroundImage);
		// Bouton Jouer existant
		TextButton playButton = new TextButton("Jouer", new Skin(Gdx.files.internal("uiskin.json")));
		playButton.setSize(200, 50); // Définissez la taille du bouton
		float xPlay = (Gdx.graphics.getWidth() - playButton.getWidth()) / 2;
		float yPlay = (Gdx.graphics.getHeight() - playButton.getHeight()) / 2 + 100; // légèrement ajusté
		playButton.setPosition(xPlay, yPlay);
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentState = GameState.ENTER_PSEUDO;
			}
		});
		stage.addActor(playButton);


		// Nouveau Bouton Classement
		TextButton leaderboardButton = new TextButton("Classement", new Skin(Gdx.files.internal("uiskin.json")));
		leaderboardButton.setSize(200, 50);
		float xLeaderboard = (Gdx.graphics.getWidth() - leaderboardButton.getWidth()) / 2;
		float yLeaderboard = yPlay - 70; // Positionné en dessous du bouton Jouer
		leaderboardButton.setPosition(xLeaderboard, yLeaderboard);
		leaderboardButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentState = GameState.LEADERBOARD;
			}
		});
		stage.addActor(leaderboardButton);

		// Nouveau bouton Quitter
		TextButton quitButton = new TextButton("Quitter", new Skin(Gdx.files.internal("uiskin.json")));
		quitButton.setSize(200, 50);
		float xQuit = (Gdx.graphics.getWidth() - quitButton.getWidth()) / 2;
		float yQuit = yLeaderboard - 70; // Positionné en dessous du bouton Classement
		quitButton.setPosition(xQuit, yQuit);
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit(); // Quitte l'application
			}
		});
		stage.addActor(quitButton);
	}


	private void createPseudoInputField() {
		Texture backgroundTexture = new Texture(Gdx.files.internal("BackgroundImage.png"));
		Image backgroundImage = new Image(backgroundTexture);
		backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundImage.setPosition(0, 0);
		pseudoStage.addActor(backgroundImage);
		pseudoInputField = new TextField("", new Skin(Gdx.files.internal("uiskin.json")));
		pseudoInputField.setMessageText("Enter Pseudo");
		pseudoInputField.setSize(200, 50);
		float x = (Gdx.graphics.getWidth() - pseudoInputField.getWidth()) / 2;
		float y = (Gdx.graphics.getHeight() - pseudoInputField.getHeight()) / 2;
		pseudoInputField.setPosition(x, y);
		pseudoStage.addActor(pseudoInputField);
	}
	private boolean pseudoEntered() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !pseudoInputField.getText().isEmpty()) {
			playerPseudo = pseudoInputField.getText(); // Stocker le pseudo saisi
			return true;
		}
		return false;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (currentState) {
			case MENU:
				// Gérer l'affichage du menu
				stage.act(Gdx.graphics.getDeltaTime());
				stage.draw();
				break;

			case ENTER_PSEUDO:
				Gdx.input.setInputProcessor(pseudoStage);
				pseudoInputField.setVisible(true);
				pseudoStage.act(Gdx.graphics.getDeltaTime());
				pseudoStage.draw();

				if (pseudoEntered()) {
					currentState = GameState.PLAYING;
					pseudoInputField.setVisible(false);
					Gdx.input.setInputProcessor(stage); // Retour au stage principal pour l'état PLAYING
				}
				break;

			case LEADERBOARD:
				if (leaderboardScreen == null) {
					leaderboardScreen = new LeaderboardScreen(this);
				}
				leaderboardScreen.render(Gdx.graphics.getDeltaTime());
				break;



			case PLAYING:
				// Gérer la logique du jeu
				batch.begin();
				scrollingBackground.draw(batch);
				enemy.draw(batch);

				if (!gameOver) {
					handleGameLogic();
				} else {
					handleEndGameScreen();
				}
				batch.end();
				break;
		}
	}

	private void handleCollectibles() {
		float distanceParcourue = player.getDistance();
		if (distanceParcourue - dernierCollectibleGenere >= distancePourNouveauCollectible) {
			collectibles.add(new Collectible(new Texture("collectible.png"), Gdx.graphics.getWidth(), 200, 100));
			dernierCollectibleGenere = distanceParcourue;
		}

		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible collectible = iter.next();
			collectible.update(Gdx.graphics.getDeltaTime());
			collectible.draw(batch);

			if (player.getBounds().overlaps(collectible.getBounds())) {
				iter.remove();
				player.addMoney(100);
				collectedItems++;

				if (collectedItems >= WINNING_COLLECTIBLE_COUNT) {
					playerWon = true;
					gameOver = true;
					backgroundMusic.stop();
				}
			}
		}
	}

	private void handleGameOver() {
		player.setHit(true);
		gameOver = true;
		playerWon = false;
		backgroundMusic.stop();
		gameOverMusic.play();
		PlayerScore score = new PlayerScore(playerPseudo, player.getDistance(), player.getMoney());
		savePlayerScore(score);
	}

	private void handleEndGameScreen() {
		if (playerWon) {
			batch.draw(winTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		} else {
			batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		font.draw(batch, "Appuyer sur R pour Rejouer", Gdx.graphics.getWidth() / 2.2f, Gdx.graphics.getHeight() / 1.1f);;
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, "Appuyer sur S pour Menu");
		float x = (Gdx.graphics.getWidth() - layout.width) / 2;
		font.draw(batch, "Appuyer sur S pour Menu", x, Gdx.graphics.getHeight() / 1.2f);

		GlyphLayout layout1 = new GlyphLayout();
		layout.setText(font, "Appuyer sur C pour voir le Classement");
		float x1 = (float) ((Gdx.graphics.getWidth() - layout1.width) / 2.3);
		font.draw(batch, "Appuyer sur C pour voir le Classement", x1, Gdx.graphics.getHeight() / 1.3f);


		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			resetGame();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) { // Pressez M pour revenir au menu
			currentState = GameState.MENU;
			resetGame();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) { // Pressez M pour revenir au menu
			currentState = GameState.LEADERBOARD;
		}
	}

	private void handleGameLogic() {
		scrollingBackground.update();
		enemy.update(player.getDistance());
		player.update();
		player.draw(batch);

		String distanceText = String.format("Distance: %.2f m", player.getDistance());
		font.draw(batch, distanceText, Gdx.graphics.getWidth() - 1900, Gdx.graphics.getHeight() - 20);
		font.draw(batch, "Argent: " + player.getMoney() + " /1000", 20, Gdx.graphics.getHeight() - 50);

		handleCollectibles();

		if (player.isHitByBullet(enemy.getBullets())) {
			handleGameOver();
		}

	}


	private void resetGame() {
		player.reset();
		enemy.reset();
		collectibles.clear();
		dernierCollectibleGenere = 0.0f;
		gameOver = false;
		playerWon = false; // Assurez-vous de réinitialiser cette variable également
		collectedItems = 0;
		backgroundMusic.play(); // Redémarrer la musique, si nécessaire
		gameOverMusic.stop(); // Arrêter la musique de game over, si elle joue

	}


	@Override
	public void dispose () {
		batch.dispose();
		enemy.getTexture().dispose();
		player.getTexture().dispose();
		font.dispose();
		backgroundMusic.dispose();
		shootSound.dispose();
	}
	public void changeState(GameState newState) {
		clearStage(); // Nettoyez le stage avant de changer d'état
		this.currentState = newState;

		if (newState == GameState.MENU) {
			Gdx.input.setInputProcessor(stage); // Rétablissez le processeur d'entrée
			createMenu(); // Recréez les boutons du menu
		}
	}
	private void clearStage() {
		stage.clear();
	}
	public GameState getCurrentState() {
		return this.currentState;
	}
}
