package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.MotionEvent;
import android.view.View;

import com.zonar.zonarapp.utils.ArrayUtils;
import com.zonar.zonarapp.utils.ZonarUtils;

import java.util.ArrayList;

public class CircleWaveView extends View {
    private static final String TAG = CircleWaveView.class.getSimpleName();

    private Matrix matrix = new Matrix();

    private Path touchPath = new Path();
    private PathMeasure pathMeasure = new PathMeasure();
    private float[] pos = new float[2];
    private float[] tan = new float[2];

    private Path path1 = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    private Paint paint = new Paint();
    private Paint shadow = new Paint();

    private ArrayList<Float> drawValues = ArrayUtils.newArrayList(10);
    private ArrayList<PointF> peeks1 = new ArrayList<>();
    private ArrayList<PointF> peeks2 = new ArrayList<>();
    private ArrayList<PointF> peeks3 = new ArrayList<>();
    private PointF touchPoint = new PointF();

    private float currentNumero = 1f;
    private int currentNumeroInt = 1;

    private boolean isTouchDown = false;

    private OnAngleChangedListener mOnAngleChangedListener;

    public static interface OnAngleChangedListener {
        public void onAngleChanged(float angle);
    }

    public CircleWaveView(Context context, double[] values) {
        super(context);

        //double[] values = ZonarUtils.getEQData(true, currentNumeroInt, 1);
        //if(values==null)
            //values = ZonarUtils.getEQData(true, currentNumeroInt, 1);
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
            this.drawValues.set(i, (float) values[i]);
        }

        setListener();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth() / 2f;


        drawByTouchPoint(canvas, touchPoint.x, touchPoint.y);

//        if (!new RectF(touchPoint.x - radius / 10f, touchPoint.y - radius / 10f, touchPoint.x + radius / 10f, touchPoint.y + radius / 10f).contains(pos[0], pos[1])) {
//
//            float distanceTouch = calculateDistance(touchPoint.x, touchPoint.y);
//            float distancePoint = calculateDistance(pos[0], pos[1]);
//            float ratio = distanceTouch / distancePoint;
//            float touchX = (pos[0] - centerX) * ratio * 1.3f + centerX;
//            float touchY = (pos[1] - centerY) * ratio * 1.3f + centerY;
//
//            canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
//            drawByTouchPoint(canvas, touchX, touchY);
//        }
    }

    private void drawByTouchPoint(Canvas canvas, float touchX, float touchY) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float distance = calculateDistance(touchX, touchY);
        float radius = getWidth() / 2f;

        peeks1.clear();
        peeks2.clear();
        peeks3.clear();
        for (int i = 0; i < drawValues.size(); i++) {
            float dist = (distance * 3 / 4f * drawValues.get(i) / 10f) + (distance / 4f);
            double degree = Math.PI * 2 / 10 * i;

            int ni = (i + 1) % drawValues.size();
            float n_dist = (distance * 3 / 4f * drawValues.get(ni) / 10f) + (distance / 4f);
            double n_degree = Math.PI * 2 / 10 * ni;

            //
            float x1 = (float) (Math.cos(degree) * dist) + centerX;
            float y1 = (float) (Math.sin(degree) * dist) + centerY;
            float n_x1 = (float) (Math.cos(n_degree) * n_dist) * 10 / 10+ centerX;
            float n_y1 = (float) (Math.sin(n_degree) * n_dist) * 10 / 10+ centerY;
            float i_x1 = ((x1 + n_x1) / 2 - centerX) * 95 / 100 + centerX;
            float i_y1 = ((y1 + n_y1) / 2 - centerY) * 95 / 100 + centerY;
            peeks1.add(new PointF(x1, y1));
            peeks1.add(new PointF(i_x1, i_y1));

            //
            float x2 = (float) (Math.cos(degree) * dist) * 0.85f + centerX;
            float y2 = (float) (Math.sin(degree) * dist) * 0.85f + centerY;
            float n_x2 = (float) (Math.cos(n_degree) * n_dist) * 0.85f + centerX;
            float n_y2 = (float) (Math.sin(n_degree) * n_dist) * 0.85f + centerY;
            float i_x2 = ((x2 + n_x2) / 2 - centerX) * 95 / 100 + centerX;
            float i_y2 = ((y2 + n_y2) / 2 - centerY) * 95 / 100 + centerY;
            peeks2.add(new PointF(x2, y2));
            peeks2.add(new PointF(i_x2, i_y2));

            //
            float x3 = (float) (Math.cos(degree) * dist) * 0.7f + centerX;
            float y3 = (float) (Math.sin(degree) * dist) * 0.7f + centerY;
            float n_x3 = (float) (Math.cos(n_degree) * n_dist) * 0.7f + centerX;
            float n_y3 = (float) (Math.sin(n_degree) * n_dist) * 0.7f + centerY;
            float i_x3 = ((x3 + n_x3) / 2 - centerX) * 95 / 100 + centerX;
            float i_y3 = ((y3 + n_y3) / 2 - centerY) * 95 / 100 + centerY;
            /*float i_x3_1 = ((x3 + n_x3) * 1 / 4 - centerX) * 75 / 100 + centerX;
            float i_y3_1 = ((y3 + n_y3) * 1 / 4 - centerY) * 75 / 100 + centerY;
            float i_x3_2 = ((x3 + n_x3) * 2 / 4 - centerX) * 55 / 100 + centerX;
            float i_y3_2 = ((y3 + n_y3) * 2 / 3 - centerY) * 55 / 100 + centerY;
            float i_x3_3 = ((x3 + n_x3) * 3 / 4 - centerX) * 75 / 100 + centerX;
            float i_y3_3 = ((y3 + n_y3) * 3 / 4 - centerY) * 75 / 100 + centerY;*/
            peeks3.add(new PointF(x3, y3));
            peeks3.add(new PointF(i_x3, i_y3));
            //peeks3.add(new PointF(i_x3_1, i_y3_1));
            //peeks3.add(new PointF(i_x3_2, i_y3_2));
            //peeks3.add(new PointF(i_x3_3, i_y3_3));

        }

        // 畫三個等高線圖
        addPathByPeek(path1, peeks1);
        addPathByPeek(path2, peeks2);
        addPathByPeek(path3, peeks3);

        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        float angle = (float)(Math.atan2(touchY - centerY, touchX - centerX) * 180 / Math.PI);

        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(angle, centerX, centerY);

        //canvas.rotate(angle + 90, centerX, centerY);
        int[] mColors = new int[]{
                0xff946b4a,
                0x8f946b4a,
                0x4f946b4a,
                0x0f946b4a
        };
        Shader SweepShader1 = new SweepGradient(centerX, centerY, mColors, null);
        SweepShader1.setLocalMatrix(gradientMatrix);
        paint.reset();
        //paint.setColor(0xff946b4a);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(SweepShader1);
        canvas.drawPath(path1, paint);

        mColors = new int[]{
                0xffb8845c,
                0x8fb8845c,
                0x4fb8845c,
                0x0fb8845c
        };
        Shader SweepShader2 = new SweepGradient(centerX, centerY, mColors, null);
        SweepShader2.setLocalMatrix(gradientMatrix);
        paint.reset();
        //paint.setColor(0xffb8845c);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(SweepShader2);
        canvas.drawPath(path2, paint);

        mColors = new int[]{
                0xffc28b61,
                0x8fc28b61,
                0x4fc28b61,
                0x0fc28b61
        };
        Shader SweepShader3 = new SweepGradient(centerX, centerY, mColors, null);
        SweepShader3.setLocalMatrix(gradientMatrix);
        paint.reset();
        //paint.setColor(0xffc28b61);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(SweepShader3);
        canvas.drawPath(path3, paint);

        // 背景漸層圖
        //Rect rect = new Rect(0, 0, getWidth(), getHeight());
        //double angle = Math.atan2(touchY - centerY, touchX - centerX) * 180 / Math.PI;
        //matrix.reset();
        //matrix.postScale(rect.width() / (float) gradientBitmap.getWidth(), rect.height() / (float) gradientBitmap.getHeight());
        //matrix.postRotate((float) angle + 90, centerX, centerY);
        //paint.reset();
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        //canvas.drawBitmap(gradientBitmap, matrix, paint);

        // 圈圈位置
        if (angle < 0) {
            angle += 360;
        }
        touchPath.reset();
        touchPath.addCircle(0, 0, radius / 13f, Path.Direction.CW);
        pathMeasure.setPath(path1, false);
        float length = pathMeasure.getLength();
        double progress = -1;
        float slope1 = 1;
        float slope2 = 1;
        try {
            for (int i = 0; i < 360; i++) {
                pathMeasure.getPosTan(length * i / 360f, pos, tan);
                slope1 = (pos[1] - centerY) / (pos[0] - centerX);
                slope2 = (touchY - centerY) / (touchX - centerX);
                if (slope1 / slope2 >= 0.87 &&
                        slope1 / slope2 <= 1.13 &&
                        ((pos[1] - centerY) / (touchY - centerY)) > 0 &&
                        ((pos[0] - centerX) / (touchX - centerX)) > 0) {
                    progress = i;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (progress == -1) {
            progress = angle;
            pathMeasure.getPosTan(length * ((float) progress + 10) / 360f, pos, tan);
        } else {
            pathMeasure.getPosTan(length * ((float) progress + 5) / 360f, pos, tan);
        }

        // 手指頭位置
        paint.reset();
        paint.setColor(0xffc48c5c);
        //paint.setColor(0xffa47b5a);
        paint.setStyle(Paint.Style.FILL);
        shadow.reset();
        shadow.setStyle(Paint.Style.FILL);

        if (isTouchDown) {
            Shader dotShade = new RadialGradient(touchX, touchY, radius/ 7f, 0xffc48c5c, 0x00d8ac80, Shader.TileMode.CLAMP);
            shadow.setShader(dotShade);
            canvas.drawCircle(touchX, touchY, radius / 20f, paint);
            canvas.drawCircle(touchX, touchY, radius / 14f, shadow);
        } else {
            Shader dotShade = new RadialGradient(pos[0], pos[1], radius / 7f, 0xffc48c5c, 0x00d8ac80, Shader.TileMode.CLAMP);
            shadow.setShader(dotShade);
            canvas.drawCircle(pos[0], pos[1], radius / 20f, paint);
            canvas.drawCircle(pos[0], pos[1], radius / 14f, shadow);
        }
    }

    private void addPathByPeek(Path path, ArrayList<PointF> peeks) {
        path.reset();
        float firstMiddleX = (peeks.get(peeks.size() - 1).x + peeks.get(0).x) / 2;
        float firstMiddleY = (peeks.get(peeks.size() - 1).y + peeks.get(0).y) / 2;
        path.moveTo(firstMiddleX, firstMiddleY);
        for (int i = 0; i < peeks.size(); i++) {
            int ni = (i + 1) % peeks.size();

            float middleX = (peeks.get(i).x + peeks.get(ni).x) / 2;
            float middleY = (peeks.get(i).y + peeks.get(ni).y) / 2;

            path.quadTo(peeks.get(i).x, peeks.get(i).y, middleX, middleY);
            //path.quadTo(middleX, middleY, peeks.get(i).x, peeks.get(i).y);
        }
        path.close();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        touchPoint.x = w * 5 / 5f;
        touchPoint.y = h / 2f;

        postInvalidate();
    }

    private void setListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isTouchDown = true;
                    modifyValues(x, y);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    isTouchDown = true;
                    modifyValues(x, y);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    isTouchDown = false;
                    modifyValues(x, y);
                }

                return true;
            }
        });
    }

    private void modifyValues(float x, float y) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth() / 2f;

        touchPoint.x = x;
        touchPoint.y = y;

        float min_distance = radius / 3f;
        float max_distance = radius * 0.9f;
        float distance = calculateDistance(touchPoint.x, touchPoint.y);
        if (distance < min_distance) {
            float ratio = min_distance / distance;
            touchPoint.x = (touchPoint.x - centerX) * ratio + centerX;
            touchPoint.y = (touchPoint.y - centerY) * ratio + centerY;
        }
        if (distance > max_distance) {
            float ratio = max_distance / distance;
            touchPoint.x = (touchPoint.x - centerX) * ratio + centerX;
            touchPoint.y = (touchPoint.y - centerY) * ratio + centerY;
        }
        float final_distance = calculateDistance(touchPoint.x, touchPoint.y);

        postInvalidate();

        float angle = (float) (Math.atan2(y - centerY, x - centerX) * 180 / Math.PI);
        if (angle <= 0) {
            angle += 360;
        }

        if (mOnAngleChangedListener != null) {
            mOnAngleChangedListener.onAngleChanged(angle);
        }

        float numero = angle / 18f;

        if (currentNumero == numero) {
            return;
        }
        currentNumero = numero;

        int numeroInt = (int) Math.ceil(numero);
        int n_numeroInt = numeroInt == 20 ? 1 : numeroInt + 1;

        if (currentNumeroInt != numeroInt) {
            currentNumeroInt = numeroInt;
        }

//        int mode = Math.min(2, (int) (calculateDistance(x, y) / (getWidth() / 3)));
        int mode = 0;
        if (final_distance >= radius / 3f + (radius * 2f / 3f) * 2f / 3f) {
            mode = 2;
        } else if (final_distance >= radius / 3f + (radius * 2f / 3f) * 1f / 3f) {
            mode = 1;
        }

        double[] values = ZonarUtils.getEQData(true, numeroInt, mode);
        double[] n_values = ZonarUtils.getEQData(false, n_numeroInt, mode);
        for (int i = 0; i < values.length; i++) {

            double value = (n_values[i] - values[i]) * (numero - (int) numero) + values[i];

            this.drawValues.set(i, (float) value);
        }

        postInvalidate();
    }

    private float calculateDistance(float x, float y) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        return (float) Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
    }

    public void setOnAngleChangedListener(OnAngleChangedListener onAngleChangedListener) {
        mOnAngleChangedListener = onAngleChangedListener;
    }

}
