package cn.com.hadon.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 王黎军 on 2017/5/8.
 */

public class ScanView extends View {
    private Paint blueCirclePaint;//蓝色圈的画笔
    private Paint redCirclePaint;//红色圈的画笔
    private Paint whiteCirclePaint;//白色圈的画笔

    public static final int STATE_READY = 1;
    public static final int STATE_SCANING = 2;
    public static final int STATE_SUCCESS = 3;

    //定义圆弧的宽度
    private static final int BLUE_CIRCLE_BORDER_WIDTH = 8;
    private static final int INSIDER_RED_CIRCLE_BORDER_WIDTH = 20;
    private static final int OUTSIDER_CIRCLE_BORDER_WIDTH = 20;
    private static final int WHITE_CIRCLE_BORDER_WIDTH = 20;

    private int minLength;//中心最大圆的直径
    private int radius;//中心最大圆的半径
    private int centerX;//中心点X坐标
    private int centerY;//中心点Y坐标

    private Bitmap scanerbitmap;//条形扫描图片
    private int curState = STATE_SCANING;//初始状态

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化一些变量
        scanerbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scaner);
        blueCirclePaint = new Paint();
        redCirclePaint = new Paint();
        whiteCirclePaint = new Paint();

        blueCirclePaint.setColor(Color.BLUE);
        blueCirclePaint.setAntiAlias(true);
        blueCirclePaint.setStyle(Paint.Style.STROKE);
        blueCirclePaint.setStrokeWidth(BLUE_CIRCLE_BORDER_WIDTH);

        redCirclePaint.setColor(Color.RED);
        redCirclePaint.setAntiAlias(true);
        redCirclePaint.setStyle(Paint.Style.STROKE);
        redCirclePaint.setStrokeWidth(INSIDER_RED_CIRCLE_BORDER_WIDTH);

        whiteCirclePaint.setColor(Color.WHITE);
        whiteCirclePaint.setAntiAlias(true);
        whiteCirclePaint.setStyle(Paint.Style.STROKE);
        whiteCirclePaint.setStrokeWidth(WHITE_CIRCLE_BORDER_WIDTH);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        //获取直径和半径以及中心带你坐标方便后面的计算
        minLength = Math.min(width, height);
        radius = minLength / 2;

    }


    /**
     * 公开方法设置当前的状态值
     * @param state
     */
    public void setState(int state) {
        this.curState = state;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        switch (curState) {
            case STATE_READY:
                drawWhiteCircle(canvas);
                drawInsiderRedCircle(canvas);
                drawOutsiderRedCircle(canvas);
                break;
            case STATE_SCANING:
                drawWhiteCircle(canvas);
                drawBlueCircle(canvas);
                drawInsiderRedCircle(canvas);
                drawOutsiderRedCircle(canvas);
                drawScaner(canvas);
                break;
            case STATE_SUCCESS:
                drawWhiteCircle(canvas);
                drawInsiderRedCircle(canvas);
                break;
        }
        updateValues();
        invalidate();
    }


    private int blueStartAngle = 0;//蓝色圆圈的开始角度
    private int blueCircleSpace = BLUE_CIRCLE_BORDER_WIDTH;//蓝色弧距离最短边的距离用于计算自身的半径用
    private static final int BLUE_CIRCLE_SWEP_ANGLE = 20;//蓝色弧扫过的角度

    /**
     * 绘制蓝色弧
     * @param canvas
     */
    private void drawBlueCircle(Canvas canvas) {
        canvas.drawArc(centerX - radius + blueCircleSpace, blueCircleSpace, centerX + radius - blueCircleSpace, minLength - blueCircleSpace, blueStartAngle, BLUE_CIRCLE_SWEP_ANGLE, false, blueCirclePaint);
        canvas.drawArc(centerX - radius + blueCircleSpace, blueCircleSpace, centerX + radius - blueCircleSpace, minLength - blueCircleSpace, blueStartAngle + 180, BLUE_CIRCLE_SWEP_ANGLE, false, blueCirclePaint);
    }

    /**
     * 根据当前的状态来更改变量达到动画的效果
     */
    private void updateValues() {
        switch (curState) {
            case STATE_READY:
                if (insideRedCircleSpace >= BLUE_CIRCLE_BORDER_WIDTH + INSIDER_RED_CIRCLE_BORDER_WIDTH+ OUTSIDER_CIRCLE_BORDER_WIDTH) {
                    insideRedCircleSpace -= 2;
                }
                whiteStartAngle = 5 / 2;
                outsiderRedCircleStartAndle = -OUTSIDER_RED_CIRCLE_SWEP_ANGLE / 2;
                break;
            case STATE_SCANING:
                if (insideRedCircleSpace >= BLUE_CIRCLE_BORDER_WIDTH + INSIDER_RED_CIRCLE_BORDER_WIDTH+ OUTSIDER_CIRCLE_BORDER_WIDTH) {
                    insideRedCircleSpace -= 2;
                }
                blueStartAngle += 4;
                outsiderRedCircleStartAndle += 2;
                if (is2Max) {
                    if (whiteStartAngle == 30) {
                        is2Max = false;
                    } else {
                        whiteStartAngle++;
                    }
                } else {
                    if (whiteStartAngle == -30) {
                        is2Max = true;
                    } else {
                        whiteStartAngle--;
                    }
                }
                scanerY += 6;
                if (scanerY > minLength) {
                    scanerY = 0;
                }
                break;
            case STATE_SUCCESS:
                whiteStartAngle = 5 / 2;
                if (insideRedCircleSpace < whiteCircleSpace + INSIDER_RED_CIRCLE_BORDER_WIDTH) {
                    insideRedCircleSpace += 2;
                }
                break;
        }
    }

    private int insideRedCircleSpace = BLUE_CIRCLE_BORDER_WIDTH + INSIDER_RED_CIRCLE_BORDER_WIDTH + OUTSIDER_CIRCLE_BORDER_WIDTH;
    private int outsiderRedCircleSpace = BLUE_CIRCLE_BORDER_WIDTH + INSIDER_RED_CIRCLE_BORDER_WIDTH + OUTSIDER_CIRCLE_BORDER_WIDTH / 2;
    private static final int OUTSIDER_RED_CIRCLE_SWEP_ANGLE = 30;
    private int outsiderRedCircleStartAndle = -OUTSIDER_RED_CIRCLE_SWEP_ANGLE / 2;

    /**
     * 绘制内部的红色圆圈
     * @param canvas
     */
    private void drawInsiderRedCircle(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius - insideRedCircleSpace, redCirclePaint);
    }

    /**
     * 绘制外部红色的两个弧
     * @param canvas
     */
    private void drawOutsiderRedCircle(Canvas canvas) {
        canvas.drawArc(centerX - radius + outsiderRedCircleSpace, outsiderRedCircleSpace, centerX + radius - outsiderRedCircleSpace, minLength - outsiderRedCircleSpace, outsiderRedCircleStartAndle, OUTSIDER_RED_CIRCLE_SWEP_ANGLE, false, redCirclePaint);
        canvas.drawArc(centerX - radius + outsiderRedCircleSpace, outsiderRedCircleSpace, centerX + radius - outsiderRedCircleSpace, minLength - outsiderRedCircleSpace, outsiderRedCircleStartAndle + 180, OUTSIDER_RED_CIRCLE_SWEP_ANGLE, false, redCirclePaint);
    }

    private int whiteStartAngle = 0;
    private static final int WHITE_CIRCLE_SWEP_ANGLE = 85;
    private int whiteCircleSpace = BLUE_CIRCLE_BORDER_WIDTH + INSIDER_RED_CIRCLE_BORDER_WIDTH + OUTSIDER_CIRCLE_BORDER_WIDTH + WHITE_CIRCLE_BORDER_WIDTH;
    private RectF whiteCircleRect;
    private boolean is2Max = true;

    /**
     * 绘制白色的弧
     * @param canvas
     */
    private void drawWhiteCircle(Canvas canvas) {
        if (whiteCircleRect == null) {
            whiteCircleRect = new RectF(centerX - radius + whiteCircleSpace, whiteCircleSpace, centerX + radius - whiteCircleSpace, minLength - whiteCircleSpace);
        }

        canvas.drawArc(whiteCircleRect, whiteStartAngle, WHITE_CIRCLE_SWEP_ANGLE, false, whiteCirclePaint);
        canvas.drawArc(whiteCircleRect, whiteStartAngle + 90, WHITE_CIRCLE_SWEP_ANGLE, false, whiteCirclePaint);
        canvas.drawArc(whiteCircleRect, whiteStartAngle + 180, WHITE_CIRCLE_SWEP_ANGLE, false, whiteCirclePaint);
        canvas.drawArc(whiteCircleRect, whiteStartAngle + 270, WHITE_CIRCLE_SWEP_ANGLE, false, whiteCirclePaint);

    }

    private int scanerY = 0;

    /**
     * 绘制扫描图片
     * @param canvas
     */
    private void drawScaner(Canvas canvas) {

        int p1, p2, hw;
        if (scanerY >= radius) {
            p1 = (scanerY - radius) * (scanerY - radius);
        } else {
            p1 = (radius - scanerY) * (radius - scanerY);
        }
        p2 = radius * radius;
        hw = (int) Math.sqrt(p2 - p1);

        Rect rect = new Rect(centerX - hw, scanerY - 10, centerX + hw, scanerY + 10);
        canvas.drawBitmap(scanerbitmap, null, rect, null);

    }
}
