package dadm.scaffold.engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;
import dadm.scaffold.sound.SoundManager;
import dadm.scaffold.space.SpaceShipPlayer;

public class GameEngine {

    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> objectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> objectsToRemove = new ArrayList<GameObject>();
    //private List<Collision> detectedCollisions = new ArrayList<Collision>();
    //private QuadTree quadTree = new QuadTree();

    private UpdateThread theUpdateThread;
    private DrawThread theDrawThread;
    public InputController theInputController;
    private final GameView theGameView;

    public Random random = new Random();

    private SoundManager soundManager;

    public int width;
    public int height;
    public double pixelFactor;

    public Activity mainActivity;

    public GameEngine(Activity activity, GameView gameView) {
        mainActivity = activity;

        theGameView = gameView;
        theGameView.setGameObjects(this.gameObjects);

        this.width = theGameView.getWidth()
                - theGameView.getPaddingRight() - theGameView.getPaddingLeft();
        this.height = theGameView.getHeight()
                - theGameView.getPaddingTop() - theGameView.getPaddingTop();

        this.pixelFactor = this.height / 400d;
    }

    public void setTheInputController(InputController inputController) {
        theInputController = inputController;
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int nugameObjects = gameObjects.size();
        for (int i = 0; i < nugameObjects; i++) {
            gameObjects.get(i).startGame();
        }

        // Start the update thread
        theUpdateThread = new UpdateThread(this);
        theUpdateThread.start();

        // Start the drawing thread
        theDrawThread = new DrawThread(this);
        theDrawThread.start();
    }

    public void stopGame() {
        if (theUpdateThread != null) {
            theUpdateThread.stopGame();
        }
        if (theDrawThread != null) {
            theDrawThread.stopGame();
        }
    }

    public void pauseGame() {
        if (theUpdateThread != null) {
            theUpdateThread.pauseGame();
        }
        if (theDrawThread != null) {
            theDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (theUpdateThread != null) {
            theUpdateThread.resumeGame();
        }
        if (theDrawThread != null) {
            theDrawThread.resumeGame();
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (isRunning()) {
            objectsToAdd.add(gameObject);
        } else {
            addGameObjectNow(gameObject);
        }
        mainActivity.runOnUiThread(gameObject.onAddedRunnable);
    }

    public void removeGameObject(GameObject gameObject) {
        objectsToRemove.add(gameObject);
        mainActivity.runOnUiThread(gameObject.onRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = gameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            GameObject go = gameObjects.get(i);
            go.onUpdate(elapsedMillis, this);
            if (go instanceof ScreenGameObject) {
                ((ScreenGameObject) go).onPostUpdate(this);
            }
        }
        checkCollisions();
        synchronized (gameObjects) {
            while (!objectsToRemove.isEmpty()) {
                gameObjects.remove(objectsToRemove.remove(0));
            }
            while (!objectsToAdd.isEmpty()) {
                gameObjects.add(objectsToAdd.remove(0));
            }
        }
    }

//        synchronized (gameObjects) {  //Bad colissions: HERE]
//            while (!objectsToRemove.isEmpty()) {
//                GameObject objectToRemove = objectsToRemove.remove(0);
//                gameObjects.remove(objectToRemove);
//                if (objectToRemove instanceof  ScreenGameObject) {
//                    //quadTree.removeGameObject((ScreenGameObject) objectToRemove);
//                }
//            }
//            while (!objectsToAdd.isEmpty()) {
//                GameObject gameObject = objectsToAdd.remove(0);
//                addGameObjectNow(gameObject);
//            }
//        }
//}

    public void onDraw() {
        theGameView.draw();
    }

    public boolean isRunning() {
        return theUpdateThread != null && theUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return theUpdateThread != null && theUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return theGameView.getContext();
    }

    private void checkCollisions() {
        int numObjects = gameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            if (gameObjects.get(i) instanceof ScreenGameObject) {
                ScreenGameObject objectA = (ScreenGameObject) gameObjects.get(i);
                for (int j = i + 1; j < numObjects; j++) {
                    if (gameObjects.get(j) instanceof ScreenGameObject) {
                        ScreenGameObject objectB = (ScreenGameObject) gameObjects.get(j);
                        if (objectA.checkCollision(objectB)) {
                            objectA.onCollision(this, objectB);
                            objectB.onCollision(this, objectA);
                        }
                    }
                }
            }
        }
    }

//    private void checkCollisions() {  //Bad colissions: HERE]
//        // Release the collisions from the previous step
//        while (!detectedCollisions.isEmpty()) {
//            Collision.release(detectedCollisions.remove(0));
//        }
//        quadTree.checkCollisions(this, detectedCollisions);
//    }

    private void addGameObjectNow(GameObject object) {
        gameObjects.add(object);
        if (object instanceof ScreenGameObject) {
            ScreenGameObject sgo = (ScreenGameObject) object;
        }
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public void onGameEvent(GameEvent gameEvent) {
        // We notify all the GameObjects
        // Also the sound manager
        soundManager.playSoundForGameEvent(gameEvent);
    }
}
