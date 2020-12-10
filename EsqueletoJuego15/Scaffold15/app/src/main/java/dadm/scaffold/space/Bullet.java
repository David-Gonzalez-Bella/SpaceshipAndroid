package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    protected double speedFactor;
    protected ShootingObject parent;

    public Bullet(GameEngine gameEngine, int sprite){
        super(gameEngine, sprite);
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (parent instanceof SpaceShipPlayer)
            positionY += speedFactor * elapsedMillis;
        else
            positionY -= speedFactor * elapsedMillis;
        if (positionY < -height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }

    public void init(ShootingObject parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    protected void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid && parent instanceof SpaceShipPlayer) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            SpaceShipPlayer.score += 100; //Add score
        }
        else if(otherObject instanceof SpaceShipEnemy && parent instanceof SpaceShipPlayer){
            removeObject(gameEngine);
            SpaceShipEnemy a = (SpaceShipEnemy) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            SpaceShipPlayer.score += 200; //Add score
        }
        else if(otherObject instanceof Bullet && ((Bullet)otherObject).parent instanceof SpaceShipEnemy){
            removeObject(gameEngine);
            Bullet a = (Bullet) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }
    }
}
