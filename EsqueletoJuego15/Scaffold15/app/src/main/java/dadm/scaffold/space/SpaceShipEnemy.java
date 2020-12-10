package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;

public class SpaceShipEnemy extends FallingObject{

    public SpaceShipEnemy(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.enemy_ship, 250d);
    }

    public void init(GameEngine gameEngine) {
        super.init(gameEngine);
        positionX = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4;
        speedX = 0.0d;
        speedY = speed;
    }

    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        super.onUpdate(elapsedMillis, gameEngine);
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool of asteroids
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }
    }

    public void removeObject(GameEngine gameEngine) {
        super.removeObject(gameEngine);
        gameController.returnToPool(this);
    }
}