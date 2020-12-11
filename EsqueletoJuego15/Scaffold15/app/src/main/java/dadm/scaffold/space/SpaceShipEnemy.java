package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipEnemy extends FallingObject implements ShootingObject{
    private static final int BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 600;
    private long timeSinceLastFire;

    List<Bullet> bullets = new ArrayList<Bullet>();

    public SpaceShipEnemy(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.enemy_ship, 80d);
        initBulletPool(gameEngine);
    }

    public void init(GameEngine gameEngine) {
        super.init(gameEngine);
        positionX = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4;
        speedX = 0.0d;
        speedY = speed;
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine, R.drawable.bullet));
        }
    }

    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        super.onUpdate(elapsedMillis, gameEngine);
        checkFiring(elapsedMillis, gameEngine);
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool of asteroids
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) { //Shooting normal bullets non stop
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            bullet.init(this, positionX + width / 2, positionY + height / 2);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
        } else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    public Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    public void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void removeObject(GameEngine gameEngine) {
        super.removeObject(gameEngine);
        gameController.returnToPool(this);
    }
}
