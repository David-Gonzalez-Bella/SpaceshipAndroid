package dadm.scaffold.space;

import dadm.scaffold.engine.GameEngine;

public interface ShootingObject {

    Bullet getBullet();

    void releaseBullet(Bullet bullet);
}
