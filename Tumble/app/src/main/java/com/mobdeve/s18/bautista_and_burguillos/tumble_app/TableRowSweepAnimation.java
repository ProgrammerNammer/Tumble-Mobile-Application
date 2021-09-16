package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class TableRowSweepAnimation extends View {
    int framesPerSecond = 60;
    long animationDuration = 3000; // 10 seconds

    Matrix matrix = new Matrix(); // transformation matrix

    long startTime;

    public TableRowSweepAnimation(Context context) {
        super(context);

        // start the animation:
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long elapsedTime = System.currentTimeMillis() - startTime;

        matrix.postTranslate(150 * elapsedTime / 1000, 0); // move 100 pixels to the right

        canvas.concat(matrix);        // call this before drawing on the canvas!!
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(166, 235, 138));

        final float WIDTH = Math.round(getWidth() / 2.0);
        final float HEIGHT = getHeight();
        canvas.translate(-3 * WIDTH, -1 * HEIGHT );
        canvas.rotate(45);
        canvas.drawRect(0, 0, WIDTH * 2, HEIGHT * 5, myPaint); // draw on canvas

        if(elapsedTime < animationDuration) {
            this.postInvalidateDelayed( 1000 / framesPerSecond);
        }
    }
}
