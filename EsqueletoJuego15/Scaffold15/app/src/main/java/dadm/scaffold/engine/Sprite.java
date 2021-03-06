package dadm.scaffold.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class Sprite extends ScreenGameObject {

    protected double rotation;

    protected double pixelFactor;

    protected Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    protected Drawable spriteDrawable;
    protected Resources r;

    protected Sprite(GameEngine gameEngine, int drawableRes) {
        r = gameEngine.getContext().getResources();
        spriteDrawable = r.getDrawable(drawableRes);

        this.pixelFactor = gameEngine.pixelFactor;

        this.width = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        this.height = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        radius = Math.max(height, width) / 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (positionX > canvas.getWidth()
                || positionY > canvas.getHeight()
                || positionX < -width
                || positionY < -height) {
            return;
        }
//Extra: Paint Sphere hitbox
//        Paint mPaint = new Paint();
//        mPaint.setColor(Color.CYAN);
//        canvas.drawCircle((float)this.positionX, (float)this.positionY, (float)this.radius, mPaint);

        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate((float) positionX, (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width / 2), (float) (positionY + height / 2));
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
