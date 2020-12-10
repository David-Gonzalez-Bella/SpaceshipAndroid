package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;

public class EndLevelObject extends FallingObject{

    public EndLevelObject(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.robot, 100d);
    }

    public void init(GameEngine gameEngine) {
        super.init(gameEngine);
        positionX = gameEngine.width / 2;
        speedX = 0.0d;
        speedY = speed;
    }

    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        super.onUpdate(elapsedMillis, gameEngine);
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height / 10) {
            speedY = 0.0d;
        }
    }
}
