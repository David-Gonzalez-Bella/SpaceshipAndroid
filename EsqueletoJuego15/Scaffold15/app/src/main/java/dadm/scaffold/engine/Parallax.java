package dadm.scaffold.engine;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Parallax extends ScreenGameObject {

    private final Bitmap mBitmap;
    protected double mPixelFactor;
    protected double mSpeedY;
    protected double mImageHeight;
    protected double mImageWidth;
    protected double mScreenHeight;
    protected double mScreenWidth;
    protected double mTargetWidth;
    protected double mPositionY;
    private final Matrix mMatrix = new Matrix();
    private final Rect mSrcRect = new Rect();
    private final Rect mDstRect = new Rect();

    public Parallax(GameEngine gameEngine, int speed, int drawableResId) {
        Drawable spriteDrawable = gameEngine.getContext().getResources().getDrawable(drawableResId);
        mBitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
        mPixelFactor = gameEngine.pixelFactor;
        mSpeedY = speed * mPixelFactor / 1000d;
        mImageHeight = spriteDrawable.getIntrinsicHeight() * mPixelFactor;
        mImageWidth = spriteDrawable.getIntrinsicWidth() * mPixelFactor;

        mScreenHeight = gameEngine.height;
        mScreenWidth = gameEngine.width;
        mTargetWidth = Math.min(mImageWidth, mScreenWidth);
    }

    @Override
    public void startGame() { }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionY += mSpeedY * elapsedMillis; //Basic vertical movement downwards
    }

    @Override
    public void onDraw(Canvas canvas) {
        efficientDraw(canvas); //The efficient draw metod will draw our background time after time using Rects (tileable)
    }

    private void efficientDraw(Canvas canvas) {

        if (mPositionY < 0) {
            mSrcRect.set(0,
                    (int) (-mPositionY / mPixelFactor),
                    (int) (mTargetWidth / mPixelFactor),
                    (int) ((mScreenHeight - mPositionY) / mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
        } else {
            mSrcRect.set(0,
                    0,
                    (int) (mTargetWidth / mPixelFactor),
                    (int) ((mScreenHeight - mPositionY) / mPixelFactor));
            mDstRect.set(0,
                    (int) mPositionY,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
            // We need to draw the previous block
            mSrcRect.set(0,
                    (int) ((mImageHeight - mPositionY) / mPixelFactor),
                    (int) (mTargetWidth / mPixelFactor),
                    (int) (mImageHeight / mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mPositionY);
        }
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);

        if (mPositionY > mScreenHeight) {
            mPositionY -= mImageHeight;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) { }
}

