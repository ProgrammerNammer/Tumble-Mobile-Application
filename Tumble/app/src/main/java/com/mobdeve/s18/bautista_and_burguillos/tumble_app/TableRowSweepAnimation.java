package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class TableRowSweepAnimation extends View {
    int framesPerSecond = 60;
    long animationDuration = 10000; // 10 seconds

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

        matrix.postTranslate(25 * elapsedTime / 1000, 0); // move 100 pixels to the right

        canvas.concat(matrix);        // call this before drawing on the canvas!!
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        
        final float WIDTH = Math.round(getWidth() / 2.0);
        final float HEIGHT = getHeight();
        canvas.translate(-1 * WIDTH, 0 );
        canvas.drawRect(0, 0, WIDTH, getHeight(), myPaint); // draw on canvas

        if(elapsedTime < animationDuration) {
            this.postInvalidateDelayed( 1000 / framesPerSecond);
        }
    }
}
