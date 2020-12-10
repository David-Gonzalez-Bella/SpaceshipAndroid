package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class SuperBullet extends Bullet {

    private int xDirection;

    public SuperBullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.bullet_02);
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += (speedFactor / 2 * xDirection) * elapsedMillis;
        positionY += speedFactor * elapsedMillis;
        if (positionY < -height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            ((SpaceShipPlayer)parent).releaseSuperBullet(this);
        }
    }

    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY, int direction) {
        super.init(parentPlayer, initPositionX, initPositionY);
        xDirection = direction;
    }

    @Override
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        ((SpaceShipPlayer)parent).releaseSuperBullet(this);
    }
}
