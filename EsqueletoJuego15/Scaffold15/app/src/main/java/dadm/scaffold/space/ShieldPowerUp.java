package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.sound.GameEvent;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.shield_power_up);
    }

    @Override
    public void Effect(GameEngine gameEngine) {
        SpaceShipPlayer.shielded = true;
        SpaceShipPlayer.skinChanged = false;
        gameEngine.onGameEvent(GameEvent.ShieldPicked);
    }
}
