package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

public class RingTextView extends View {
    private Path bassARC, richARC, vocalARC, detailedARC, spaciousARC;
    private Paint bassText, richText, vocalText, detailedText, spaciousText;
    public RingTextView(Context context) {
        super(context);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        RectF oval = new RectF(width*0.045f, width*0.045f, width*0.837f, width*0.843f);
        bassARC = new Path();
        bassARC.addArc(oval, -162, 72);
        richARC = new Path();
        richARC.addArc(oval, -90, 72);
        vocalARC = new Path();
        vocalARC.addArc(oval, -18, 72);
        detailedARC = new Path();
        detailedARC.addArc(oval, 126, -72);
        spaciousARC = new Path();
        spaciousARC.addArc(oval, 126, 72);

        bassText = new Paint(Paint.ANTI_ALIAS_FLAG);
        bassText.setStyle(Paint.Style.FILL_AND_STROKE);
        bassText.setTextSize(50f);
        bassText.setTextAlign(Paint.Align.CENTER);

        richText = new Paint(Paint.ANTI_ALIAS_FLAG);
        richText.setStyle(Paint.Style.FILL_AND_STROKE);
        richText.setTextSize(50f);
        richText.setTextAlign(Paint.Align.CENTER);

        vocalText = new Paint(Paint.ANTI_ALIAS_FLAG);
        vocalText.setStyle(Paint.Style.FILL_AND_STROKE);
        vocalText.setTextSize(50f);
        vocalText.setTextAlign(Paint.Align.CENTER);

        detailedText = new Paint(Paint.ANTI_ALIAS_FLAG);
        detailedText.setStyle(Paint.Style.FILL_AND_STROKE);
        detailedText.setTextSize(50f);
        detailedText.setTextAlign(Paint.Align.CENTER);

        spaciousText = new Paint(Paint.ANTI_ALIAS_FLAG);
        spaciousText.setStyle(Paint.Style.FILL_AND_STROKE);
        spaciousText.setTextSize(50f);
        spaciousText.setTextAlign(Paint.Align.CENTER);

        rotate(30);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        canvas.drawTextOnPath( "Bass", bassARC, 0, 15, bassText);
        canvas.drawTextOnPath( "Rich", richARC, 0, 15, richText);
        canvas.drawTextOnPath( "Vocal", vocalARC, 0, 15, vocalText);
        canvas.drawTextOnPath( "Detailed", detailedARC, 0, 15, detailedText);
        canvas.drawTextOnPath( "Spacious", spaciousARC, 0, 15, spaciousText);
        invalidate();
    }
    public void rotate(float angle){

        bassText.setColor(angle < 208 || angle > 260 ? 0xff7d7d7d : 0xff000000);
        richText.setColor(angle < 278 || angle > 330 ? 0xff7d7d7d : 0xff000000);
        vocalText.setColor(angle >= 345 || angle <= 39 ? 0xff000000 : 0xff7d7d7d);
        detailedText.setColor(angle < 63 || angle > 111 ? 0xff7d7d7d : 0xff000000);
        spaciousText.setColor(angle < 132 || angle > 181 ? 0xff7d7d7d : 0xff000000);
    }
}
