package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import dadm.scaffold.space.SpaceShipPlayer;

public class FramesPerSecondCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private Paint paint;
    private long totalMillis;
    private int draws;
    private float framesPerSecond;

    private String framesPerSecondText = "";

    public FramesPerSecondCounter(GameEngine gameEngine) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        textHeight = (float) (25 * gameEngine.pixelFactor);
        textWidth = (float) (50 * gameEngine.pixelFactor);
        paint.setTextSize(textHeight / 2);
    }

    @Override
    public void startGame() {
        totalMillis = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        totalMillis += elapsedMillis;
        if (totalMillis > 1000) {
            framesPerSecond = draws * 1000 / totalMillis;
            framesPerSecondText = framesPerSecond + " fps";
            totalMillis = 0;
            draws = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) { //This method will draw all UI texts (FPS, lifes, score, ...)
        //FPS black box
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, (int) (canvas.getHeight() - textHeight), textWidth, canvas.getHeight(), paint);

        //FPS
        paint.setColor(Color.WHITE);
        canvas.drawText(framesPerSecondText, textWidth / 2, (int) (canvas.getHeight() - textHeight / 2), paint);

        //Score
        paint.setColor(Color.WHITE);
        canvas.drawText("Score:   " + SpaceShipPlayer.score, textWidth, textHeight, paint);

        //Lifes
        paint.setColor(Color.WHITE);
        canvas.drawText("Stars:   " + SpaceShipPlayer.stars, textWidth * 3, textHeight, paint);

        //Stars
        paint.setColor(Color.WHITE);
        canvas.drawText("Lifes:   " + SpaceShipPlayer.lifes, textWidth * 5, textHeight, paint);
        draws++;
    }
}
