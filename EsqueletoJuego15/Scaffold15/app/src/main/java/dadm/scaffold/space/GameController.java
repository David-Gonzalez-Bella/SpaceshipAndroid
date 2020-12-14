package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ASTEROIDS = 800;
    private static final int TIME_BETWEEN_ENEMY_SHIPS = 7000;
    private static final int TIME_BETWEEN_POWER_UPS = 15000;
    private static final int TIME_BETWEEN_STARS = 5000;
    private static final long TIME_END_LEVEL_PORTAL = 10000;
    private long currentMillis;

    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private List<SpaceShipEnemy> enemyShipsPool = new ArrayList<SpaceShipEnemy>();
    private List<PowerUp> powerUpPool = new ArrayList<PowerUp>();
    private List<StarScore> starsPool = new ArrayList<StarScore>();
    private EndLevelObject endLevel;

    private int asteroidsSpawned;
    private int enemyShipsSpawned;
    private int powerUpsSpawned;
    private int starsSpawned;
    private boolean endLevelCreated = false;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now (enemies and power ups)
        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
            enemyShipsPool.add(new SpaceShipEnemy(this, gameEngine));
        }
        for (int i = 0; i < 2; i++) {
            powerUpPool.add(new LifePowerUp(this, gameEngine));
            powerUpPool.add(new ShieldPowerUp(this, gameEngine));
            starsPool.add(new StarScore(this, gameEngine));
        }
        endLevel = new EndLevelObject(this, gameEngine);
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        asteroidsSpawned = 0;
        enemyShipsSpawned = 0;
        powerUpsSpawned = 0;
        starsSpawned = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestampAsteroids = asteroidsSpawned * TIME_BETWEEN_ASTEROIDS;
        long waveTimestampEnemyShips = enemyShipsSpawned*TIME_BETWEEN_ENEMY_SHIPS;
        long waveTimestampPWU = powerUpsSpawned*TIME_BETWEEN_POWER_UPS;
        long waveTimestampStar = starsSpawned*TIME_BETWEEN_STARS;

        if (currentMillis > waveTimestampAsteroids) {
            // Spawn a new enemy
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            asteroidsSpawned++;
            return;
        }

        if (currentMillis > waveTimestampEnemyShips) {
            // Spawn a new enemy
            SpaceShipEnemy a = enemyShipsPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemyShipsSpawned++;
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

        if(currentMillis > TIME_END_LEVEL_PORTAL && !endLevelCreated){
            endLevel.init(gameEngine);
            gameEngine.addGameObject(endLevel);
            endLevelCreated = true;
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

    public void returnToPool(SpaceShipEnemy enemyShip) {
        enemyShipsPool.add(enemyShip);
    }

    public void returnToPool(PowerUp powerUp) {
        powerUpPool.add(powerUp);
    }

    public void returnToPool(StarScore starScore) {
        starsPool.add(starScore);
    }
}
