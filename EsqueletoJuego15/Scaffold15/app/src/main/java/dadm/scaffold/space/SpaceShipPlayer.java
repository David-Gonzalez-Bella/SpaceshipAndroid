package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.counter.ResultsFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;


public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final int INITIAL_SUPER_BULLET_POOL_AMOUNT = 8;
    private static final long TIME_BETWEEN_BULLETS = 600;
    private static final long TIME_BETWEEN_SUPER_BULLETS = 800;

    List<Bullet> bullets = new ArrayList<Bullet>();
    List<SuperBullet> superBullets = new ArrayList<SuperBullet>();
    private long timeSinceLastFire;
    private long timeSinceLastFireSuper;

    private int maxX;
    private int maxY;
    private double speedFactor;

    public static int score = 0;
    public static int stars = 0;
    public static int lifes = 3;


    public SpaceShipPlayer(GameEngine gameEngine, int skin) {
        super(gameEngine, skin);
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine, R.drawable.bullet));
        }
        for (int i = 0; i < INITIAL_SUPER_BULLET_POOL_AMOUNT; i++) {
            superBullets.add(new SuperBullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    private SuperBullet getSuperBullet() {
        if (superBullets.isEmpty()) {
            return null;
        }
        return superBullets.remove(0);
    }

    void releaseSuperBullet(SuperBullet superBullet) {
        superBullets.add(superBullet);
    }

    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
        checkFiringSuper(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) { //Shooting normal bullets non stop
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            bullet.init(this, positionX + width / 2, positionY);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        } else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    private void checkFiringSuper(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFireSuper > TIME_BETWEEN_SUPER_BULLETS) { //Check if super bullets can be fired
            SuperBullet bulletLeft = getSuperBullet();
            if (bulletLeft == null) return;

            SuperBullet bulletRight = getSuperBullet();
            if (bulletRight == null) return;

            bulletLeft.init(this, positionX + width / 2, positionY, -1);
            gameEngine.addGameObject(bulletLeft);

            bulletRight.init(this, positionX + width / 2, positionY, 1);
            gameEngine.addGameObject(bulletRight);

            timeSinceLastFireSuper = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        } else {
            timeSinceLastFireSuper += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) { //If we collide with an asteroid
            gameEngine.removeGameObject(otherObject);
            ((Asteroid) otherObject).gameController.returnToPool((Asteroid) otherObject);
            lifes--;
            if (lifes <= 0) {
                gameEngine.removeGameObject(this);
                gameEngine.stopGame();
                gameEngine.onGameEvent(GameEvent.SpaceshipHit);
                ((ScaffoldActivity) gameEngine.mainActivity).navigateToFragment(new ResultsFragment());
            }
        }else if(otherObject instanceof PowerUp){ //If we collide with a power up
            gameEngine.removeGameObject(otherObject);
            ((PowerUp) otherObject).gameController.returnToPool((PowerUp) otherObject);
            ((PowerUp)otherObject).Effect(); //The power up triggers its effect
        }
        else if(otherObject instanceof StarScore){ //If we collide with star
            gameEngine.removeGameObject(otherObject);
            ((StarScore) otherObject).gameController.returnToPool((StarScore) otherObject);
            stars++;
        }
    }
}
