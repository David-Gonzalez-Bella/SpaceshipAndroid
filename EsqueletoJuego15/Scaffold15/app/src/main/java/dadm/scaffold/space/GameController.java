package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 500;
    private static final int TIME_BETWEEN_POWER_UPS = 10000;
    private static final int TIME_BETWEEN_STARS = 20000;
    private long currentMillis;
    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private List<PowerUp> powerUpPool = new ArrayList<PowerUp>();
    private List<StarScore> starsPool = new ArrayList<StarScore>();
    private int enemiesSpawned;
    private int powerUpsSpawned;
    private int starsSpawned;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now (enemies and power ups)
        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
        for (int i = 0; i < 2; i++) {
            powerUpPool.add(new LifePowerUp(this, gameEngine));
            starsPool.add(new StarScore(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        powerUpsSpawned = 0;
        starsSpawned = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestamp = enemiesSpawned*TIME_BETWEEN_ENEMIES;
        long waveTimestampPWU = powerUpsSpawned*TIME_BETWEEN_POWER_UPS;
        long waveTimestampStar = starsSpawned*TIME_BETWEEN_STARS;

        if (currentMillis > waveTimestamp) {
            // Spawn a new enemy
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemiesSpawned++;
            return;
        }

        if (currentMillis > waveTimestampPWU) {
            // Spawn a new power up
            PowerUp a = powerUpPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            powerUpsSpawned++;
            return;
        }

        if (currentMillis > waveTimestampStar) {
            // Spawn a new power up
            StarScore a = starsPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            starsSpawned++;
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }

    public void returnToPool(PowerUp powerUp) {
        powerUpPool.add(powerUp);
    }

    public void returnToPool(StarScore starScore) {
        starsPool.add(starScore);
    }
}
