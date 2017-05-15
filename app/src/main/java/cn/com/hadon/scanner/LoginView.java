package cn.com.hadon.scanner;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by 王黎军 on 2017/5/5.
 */

public class LoginView extends View {


    private Paint shapePaint;
    private Paint progresspaint;
    private Paint textPaint;
    private static final float PROGRESS_ANGLE = 60;
    private static final String LOGIN_STRING = "login";
    private float progressStartAngle = 0;
    private boolean isLoging = false;
    private boolean loginSuccess = false;

    private float circleAngle = 0;
    private int distance = 0;


    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        shapePaint = new Paint();
        shapePaint.setStyle(Paint.Style.FILL);
        shapePaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);


        progresspaint = new Paint();
        progresspaint.setStyle(Paint.Style.STROKE);
        progresspaint.setColor(Color.WHITE);
        progresspaint.setStrokeWidth(5);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        draw_oval_to_circle(canvas);
        canvas.drawText(LOGIN_STRING, 0, LOGIN_STRING.length(), getMeasuredWidth() / 2, getMeasuredHeight() / 2, textPaint);

        if (isLoging) {
            canvas.drawArc(getMeasuredWidth() / 2 - getMeasuredHeight() / 2, 0, getMeasuredWidth() / 2 + getMeasuredHeight() / 2, getMeasuredHeight(), progressStartAngle, PROGRESS_ANGLE, false, progresspaint);
            progressStartAngle = progressStartAngle + 10;
            invalidate();
        }

    }


    private void set_rect_to_ciecle_animation() {
        ValueAnimator cornerAnimator = ValueAnimator.ofFloat(0, getMeasuredHeight() / 2);
        cornerAnimator.setDuration(600);
        cornerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator toCiecleAnimator = ValueAnimator.ofInt(0, getMeasuredWidth() / 2 - getMeasuredHeight() / 2);
        toCiecleAnimator.setDuration(600);
        toCiecleAnimator.setInterpolator(new BounceInterpolator());
        toCiecleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                distance = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        toCiecleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isLoging = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(cornerAnimator, toCiecleAnimator);
        animatorSet.start();
    }

    private void login_success_animation() {

    }


    public void loginSuccess() {
        loginSuccess = true;
        isLoging = false;
    }


    public void loginFail() {
        resetView();
        invalidate();
    }


    private void resetView() {
        circleAngle = 0;
        distance = 0;
        isLoging = false;
        loginSuccess = false;
    }


    private void draw_oval_to_circle(final Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = 0 + distance;
        rectF.top = 0;
        rectF.right = getMeasuredWidth() - distance;
        rectF.bottom = getMeasuredHeight();
        canvas.drawRoundRect(rectF, circleAngle, circleAngle, shapePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (distance == 0) {
                    set_rect_to_ciecle_animation();
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
