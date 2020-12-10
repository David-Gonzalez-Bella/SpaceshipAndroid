package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.shield_power_up);
    }

    @Override
    public void Effect() {
        SpaceShipPlayer.shielded = true;
        SpaceShipPlayer.skinChanged = false;
    }
}
