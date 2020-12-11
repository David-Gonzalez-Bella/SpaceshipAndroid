package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;

public class Asteroid extends FallingObject {

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.planet, 200d);
    }

    public void init(GameEngine gameEngine) {
        super.init(gameEngine);
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        positionX = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4;
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);
        rotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(360);
    }

    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        super.onUpdate(elapsedMillis, gameEngine);
        rotation += rotationSpeed * elapsedMillis; //Asteroids rotate
        if (rotation > 360) {
            rotation = 0;
        }
        else if (rotation < 0) {
            rotation = 360;
        }
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
