package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.Sprite;

public class LifePowerUp extends PowerUp {

    public LifePowerUp(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.life_power_up);
    }

    @Override
    public void Effect() {
        if(SpaceShipPlayer.health < 500)
            SpaceShipPlayer.health += 125;
    }
}
