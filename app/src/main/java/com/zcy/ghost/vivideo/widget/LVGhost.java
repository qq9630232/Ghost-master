package com.zcy.ghost.vivideo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by lumingmin on 16/6/29.
 */

public class LVGhost extends View {


    float mWidth = 0f;
    float mHight = 0f;
    Paint mPaint, mPaintHand, mPaintShadow, mPaintArms;
    RectF rectFGhost = new RectF();
    RectF rectFGhostShadow = new RectF();
    float mPadding = 0f;
    int mskirtH = 0;
    Path path = new Path();

    public LVGhost(Context context) {
        this(context, null);
    }

    public LVGhost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LVGhost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHight = getMeasuredHeight();
        mPadding = 10;
        mskirtH = (int) (mWidth / 40);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaintHand = new Paint();
        mPaintHand.setAntiAlias(true);
        mPaintHand.setStyle(Paint.Style.FILL);
        mPaintHand.setColor(Color.argb(220, 0, 0, 0));

        mPaintShadow = new Paint();
        mPaintShadow.setAntiAlias(true);
        mPaintShadow.setStyle(Paint.Style.FILL);
        mPaintShadow.setColor(Color.argb(60, 0, 0, 0));


        mPaintArms = new Paint();
        mPaintArms.setAntiAlias(true);
        mPaintArms.setStrokeWidth(8);
        mPaintArms.setStyle(Paint.Style.FILL);
        mPaintArms.setColor(Color.argb(150, 0, 0, 0));

        startAnim();
    }


    private void drawShadow(Canvas canvas) {
        canvas.drawArc(rectFGhostShadow, 0, 360, false, mPaintShadow);

    }


    private void drawHead(Canvas canvas) {
        canvas.drawCircle(rectFGhost.left + rectFGhost.width() / 2
                , rectFGhost.width() / 2 + rectFGhost.top
                , rectFGhost.width() / 2 - 15
                , mPaint
        );
    }

    private void drawHand(Canvas canvas) {

        canvas.drawCircle(rectFGhost.left + rectFGhost.width() / 2 - mskirtH * 3 / 2 + mskirtH * onAnimationRepeatFlag

                , rectFGhost.width() / 2 + mskirtH + rectFGhost.top,
                mskirtH * 0.9f, mPaintHand
        );
        canvas.drawCircle(rectFGhost.left + rectFGhost.width() / 2 + mskirtH * 3 / 2 + mskirtH * onAnimationRepeatFlag
                , rectFGhost.width() / 2 + mskirtH + rectFGhost.top,
                mskirtH * 0.9f, mPaintHand
        );


    }


    float wspace = 10f;
    float hspace = 10f;

    private void drawBody(Canvas canvas) {
        path.reset();

        float x = (float) ((rectFGhost.width() / 2 - 15) * Math.cos(5 * Math.PI / 180f));
        float y = (float) ((rectFGhost.width() / 2 - 15) * Math.sin(5 * Math.PI / 180f));

        float x2 = (float) ((rectFGhost.width() / 2 - 15) * Math.cos(175 * Math.PI / 180f));
        float y2 = (float) ((rectFGhost.width() / 2 - 15) * Math.sin(175 * Math.PI / 180f));


        path.moveTo(rectFGhost.left + rectFGhost.width() / 2 - x, rectFGhost.width() / 2 - y + rectFGhost.top);
        path.lineTo(rectFGhost.left + rectFGhost.width() / 2 - x2, rectFGhost.width() / 2 - y2 + rectFGhost.top);
        path.quadTo(rectFGhost.right + wspace / 2, rectFGhost.bottom
                , rectFGhost.right - wspace, rectFGhost.bottom - hspace);


        float a = mskirtH;//(mskirtH/2);

        float m = (rectFGhost.width() - 2 * wspace) / 7f;

        for (int i = 0; i < 7; i++) {
            if (i % 2 == 0) {
                path.quadTo(rectFGhost.right - wspace - m * i - (m / 2), rectFGhost.bottom - hspace - a
                        , rectFGhost.right - wspace - (m * (i + 1)), rectFGhost.bottom - hspace);
            } else {
                path.quadTo(rectFGhost.right - wspace - m * i - (m / 2), rectFGhost.bottom - hspace + a
                        , rectFGhost.right - wspace - (m * (i + 1)), rectFGhost.bottom - hspace);

            }
        }

        path.quadTo(rectFGhost.left - 5, rectFGhost.bottom
                , rectFGhost.left + rectFGhost.width() / 2 - x, rectFGhost.width() / 2 - y + rectFGhost.top);


        path.close();
        canvas.drawPath(path, mPaint);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        float distance = (mWidth - 2 * mPadding) / 3 * 2 * mAnimatedValue;

        rectFGhost.left = mPadding + distance;
        rectFGhost.right = (mWidth - 2 * mPadding) / 3 + distance;
        float moveY = 0f;
        float moveYMax = mHight / 4f / 2f;
        float shadowHighMax = 5f;
        float shadowHigh = 0f;

        if (mAnimatedValue <= 0.25) {
            moveY = (float) (moveYMax / 0.25 * mAnimatedValue);
            rectFGhost.top = moveY;

            rectFGhost.bottom = mHight / 4 * 3 + moveY;

            shadowHigh = shadowHighMax / 0.25f * mAnimatedValue;


        } else if (mAnimatedValue > 0.25 && mAnimatedValue <= 0.5f) {

            moveY = (float) (moveYMax / 0.25 * (mAnimatedValue - 0.25f));
            rectFGhost.top = moveYMax - moveY;
            rectFGhost.bottom = mHight / 4 * 3 + moveYMax - moveY;

            shadowHigh = shadowHighMax - shadowHighMax / 0.25f * (mAnimatedValue - 0.25f);

        } else if (mAnimatedValue > 0.5 && mAnimatedValue <= 0.75f) {
            moveY = (float) (moveYMax / 0.25 * (mAnimatedValue - 0.5f));
            rectFGhost.top = moveY;
            rectFGhost.bottom = mHight / 4 * 3 + moveY;
            shadowHigh = shadowHighMax / 0.25f * (mAnimatedValue - 0.5f);


        } else if (mAnimatedValue > 0.75 && mAnimatedValue <= 1f) {
            moveY = (float) (moveYMax / 0.25 * (mAnimatedValue - 0.75f));
            rectFGhost.top = moveYMax - moveY;
            rectFGhost.bottom = mHight / 4 * 3 + moveYMax - moveY;
            shadowHigh = shadowHighMax - shadowHighMax / 0.25f * (mAnimatedValue - 0.75f);

        }


        rectFGhostShadow.top = mHight - 25 + shadowHigh;
        rectFGhostShadow.bottom = mHight - 5 - shadowHigh;
        rectFGhostShadow.left = rectFGhost.left + 5 + shadowHigh * 3;
        rectFGhostShadow.right = rectFGhost.right - 5 - shadowHigh * 3;
        drawShadow(canvas);
        drawHead(canvas);
        drawBody(canvas);
        drawHand(canvas);
        canvas.restore();

    }


    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 2500);
    }

    private ValueAnimator valueAnimator;
    private float mAnimatedValue = 0.f;

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mAnimatedValue = 0f;
            wspace = 10;
            onAnimationRepeatFlag = 1;
            postInvalidate();
        }
    }

    int onAnimationRepeatFlag = 1;

    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mAnimatedValue = (float) valueAnimator.getAnimatedValue();

                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                onAnimationRepeatFlag = onAnimationRepeatFlag * -1;

                if (onAnimationRepeatFlag == -1) {
                    wspace = 22;
                } else {
                    wspace = -2;
                }


            }

        });
        if (!valueAnimator.isRunning()) {
            wspace = -2;
            valueAnimator.start();

        }

        return valueAnimator;
    }


}
