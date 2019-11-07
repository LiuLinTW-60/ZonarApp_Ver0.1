package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

public class RingView extends View {
    private Paint mBackground;
    private int[] mColors;
    public RingView(Context context) {
        super(context);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth()* 0.9f / 2f ;

        mColors = new int[]{
                0xffc28b61,
                0x2fc28b61,
                0x2fc28b61,
                0x2fc28b61,
                0x2fc28b61,
                0xffc28b61
        };
        Shader shader = new SweepGradient(centerX, centerY, mColors, null);
        mBackground = new Paint();
        mBackground.setStyle(Paint.Style.STROKE);
        mBackground.setStrokeWidth(60);
        mBackground.setShader(shader);
        canvas.drawCircle(centerX, centerY, radius, mBackground);
    }
}
