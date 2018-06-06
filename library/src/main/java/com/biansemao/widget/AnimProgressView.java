package com.biansemao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆形进度条
 * Created by Administrator on 2015/06/15.
 */
public class AnimProgressView extends View {

    // View半径
    private float mViewRadius;
    // 各圆颜色，圆个数，动画时间，起始最亮的圆（0开启），当前最亮的圆，动画模式
    private int circleColor, circleNum, animTime, lightIndex, mIndex, animMode;
    // 各圆半径
    private float circleRadius;
    // 度数增量
    private double indentDegree;
    // 透明度增量
    private int indentAlpha;
    // 圆半径增量
    private float indentRaidus;
    // 是否自动开始动画，是否顺时针旋转
    private boolean isAutoAnim, isClockwise;
    private Paint paint;
    // 各圆位置集合
    private List<float[]> posList = new ArrayList<float[]>();

    /**
     * 动画模式（瞎起的）
     */
    public enum AnimMode{
        /** 旋转模式 */
        ROTATEMODE(1),
        /** 点转模式 */
        DOTMODE(2),
        /** 尾巴模式 */
        TAILMODE(3),
        /** 抓尾模式 */
        GARBMODE(4),
        /** 缩放模式 */
        ZOOMMODE(5),
        /** 变幻模式 */
        CHANGEMODE(6);

        private int value;

        AnimMode(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public AnimProgressView(Context context) {
        super(context, null);
    }

    public AnimProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AnimProgressView);
        circleColor = typedArray.getColor(R.styleable.AnimProgressView_circleColor, Color.RED);
        circleNum = typedArray.getInteger(R.styleable.AnimProgressView_circleNum, 8);
        lightIndex = typedArray.getInteger(R.styleable.AnimProgressView_lightIndex, 0);
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.AnimProgressView_circleRadius, 4);
        animTime = typedArray.getInteger(R.styleable.AnimProgressView_animTime, 200);
        isAutoAnim = typedArray.getBoolean(R.styleable.AnimProgressView_isAutoAnim, true);
        isClockwise = typedArray.getBoolean(R.styleable.AnimProgressView_isClockwise, true);
        animMode = typedArray.getInteger(R.styleable.AnimProgressView_animMode, AnimMode.ROTATEMODE.getValue());
        typedArray.recycle();

        if(circleNum <= 1){
            throw new UnsupportedOperationException("The number of circles in this view is not correct.");
        }
        if(circleRadius <= 0){
            throw new UnsupportedOperationException("The circle radius is not correct.");
        }
        if(animTime <= 50){
            throw new UnsupportedOperationException("Animation time is not correct.");
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL); // 实心
        paint.setAntiAlias(true); // 消除锯齿

        indentDegree = Math.toRadians(360 / circleNum);  // 换算成弧度

        if(lightIndex < 0 || lightIndex > circleNum - 1){
            lightIndex = 0;
        }
        if(animMode == AnimMode.ROTATEMODE.getValue()){
            indentAlpha = 250 / circleNum;
            mIndex = lightIndex;
        } else if(animMode == AnimMode.DOTMODE.getValue()){
            indentAlpha = 250 / circleNum;
            mIndex = -1;
        } else if(animMode == AnimMode.TAILMODE.getValue()){
            indentRaidus = circleRadius / (2 * circleNum);
            mIndex = lightIndex;
        } else if(animMode == AnimMode.GARBMODE.getValue()){
            indentAlpha = 250 / circleNum;
            indentRaidus = circleRadius / (2 * circleNum);
            mIndex = lightIndex;
        } else if(animMode == AnimMode.ZOOMMODE.getValue()){
            mIndex = lightIndex;
            indentRaidus = circleRadius / circleNum;
        } else if(animMode == AnimMode.CHANGEMODE.getValue()){
            indentAlpha = 250 / circleNum;
            mIndex = lightIndex;
            indentRaidus = circleRadius / circleNum;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = this.getWidth();
        int height = this.getHeight();

        if(width >= height){
            mViewRadius = height / 2 - circleRadius - getPaddingLeft() - getPaddingRight();
        } else {
            mViewRadius = width / 2 - circleRadius - getPaddingTop() - getPaddingBottom();
        }

        posList.clear();
        if(animMode == AnimMode.ROTATEMODE.getValue()
                || animMode == AnimMode.TAILMODE.getValue()
                || animMode == AnimMode.GARBMODE.getValue()
                || animMode == AnimMode.ZOOMMODE.getValue()
                || animMode == AnimMode.CHANGEMODE.getValue()){
            for (int i = 0; i < circleNum; i ++){
                posList.add(calculatePos(i));
            }
        } else if(animMode == AnimMode.DOTMODE.getValue()){
            for (int i = lightIndex; i < circleNum; i ++){
                posList.add(calculatePos(i));
            }
            for (int i = 0; i < lightIndex; i ++){
                posList.add(calculatePos(i));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < circleNum; i ++){
            drawCircle(canvas, i);
        }

        if(isAutoAnim){
            mIndex ++;
            if(mIndex >= circleNum){
                if(animMode == AnimMode.DOTMODE.getValue()){
                    mIndex = -1;
                } else if(animMode == AnimMode.ROTATEMODE.getValue()
                        || animMode == AnimMode.TAILMODE.getValue()
                        || animMode == AnimMode.GARBMODE.getValue()
                        || animMode == AnimMode.ZOOMMODE.getValue()
                        || animMode == AnimMode.CHANGEMODE.getValue()) {
                    mIndex = 0;
                }
            }
            postInvalidateDelayed(animTime);
        }
    }

    private void drawCircle(Canvas canvas, int index){
        float radius = circleRadius;
        if(animMode == AnimMode.ROTATEMODE.getValue()){
            radius = circleRadius;
            paint.setColor(changeAlpha(circleColor, calculateAlpha(index)));
        } else if(animMode == AnimMode.DOTMODE.getValue()){
            radius = circleRadius;
            if(index <= mIndex){
                paint.setColor(circleColor);
            } else {
                paint.setColor(changeAlpha(circleColor, 50));
            }
        } else if(animMode == AnimMode.TAILMODE.getValue()){
            radius = calculateTailRadius(index);
            paint.setColor(circleColor);
        } if(animMode == AnimMode.GARBMODE.getValue()){
            radius = calculateTailRadius(index);
            paint.setColor(changeAlpha(circleColor, calculateAlpha(index)));
        } else if(animMode == AnimMode.ZOOMMODE.getValue()){
            radius = calculateZoomRadius(index);
            paint.setColor(circleColor);
        } else if(animMode == AnimMode.CHANGEMODE.getValue()){
            radius = calculateZoomRadius(index);
            paint.setColor(changeAlpha(circleColor, calculateChangeAlpha(index)));
        }
        float[] pos = posList.get(index);
        canvas.drawCircle(pos[0], pos[1], radius, paint);
    }

    /**
     * 计算各个圆圈透明度，ROTATEMODE模式、GARBMODE模式
     * @param i index
     * @return int
     */
    private int calculateAlpha(int i){
        int value = i - mIndex;
        if(value >= 0){
            return 255 - indentAlpha * value;
        }
        return 255 - indentAlpha * (circleNum + value);
    }

    /**
     * 计算各圆半径，TAILMODE、GRABMODE模式
     * @param i index
     * @return float
     */
    private float calculateTailRadius(int i){
        int value = i - mIndex;
        if(value >= 0){
            return circleRadius - indentRaidus * value;
        }
        return circleRadius - indentRaidus * (circleNum + value - 1);
    }

    /**
     * 计算各个圆圈位置
     * @param i index
     * @return float[]
     */
    private float[] calculatePos(int i){
        float[] pos = new float[2];
        double degree;
        if(isClockwise){
            degree = indentDegree * i;
        } else {
            if(i == 0){
                degree = indentDegree * i;
            } else {
                degree = indentDegree * (circleNum - i);
            }
        }
        pos[0] = mViewRadius + (float) (mViewRadius * Math.sin(degree)) + circleRadius + getPaddingLeft();
        pos[1] = mViewRadius - (float) (mViewRadius * Math.cos(degree)) + circleRadius + getPaddingTop();
        return pos;
    }

    /**
     * 计算各圆透明度，CHANGEMODE模式
     * @param i index
     * @return int
     */
    private int calculateChangeAlpha(int i){
        if(mIndex >= circleNum / 2){
            if(i < mIndex - circleNum / 2){
                return 255 - (i + circleNum - mIndex) * indentAlpha;
            } else if(i > mIndex){
                return 255 - (i - mIndex) * indentAlpha;
            } else {
                return 255 - (mIndex - i) * indentAlpha;
            }
        } else {
            if(i < mIndex){
                return 255 - (mIndex - i) * indentAlpha;
            } else if(i > mIndex + circleNum / 2){
                return 255 - (mIndex + circleNum - i) * indentAlpha;
            } else {
                return 255 - (i - mIndex) * indentAlpha;
            }
        }
    }

    /**
     * 计算各圆半径，ZOOMMODE模式、CHANGEMODE模式
     * @param i index
     * @return float
     */
    private float calculateZoomRadius(int i){
        if(mIndex >= circleNum / 2){
            if(i < mIndex - circleNum / 2){
                return circleRadius - (i + circleNum - mIndex) * indentRaidus;
            } else if(i > mIndex){
                return circleRadius - (i - mIndex) * indentRaidus;
            } else {
                return circleRadius - (mIndex - i) * indentRaidus;
            }
        } else {
            if(i < mIndex){
                return circleRadius - (mIndex - i) * indentRaidus;
            } else if(i > mIndex + circleNum / 2){
                return circleRadius - (mIndex + circleNum - i) * indentRaidus;
            } else {
                return circleRadius - (i - mIndex) * indentRaidus;
            }
        }
    }

    /**
     * 修改颜色透明度
     * @param color 颜色
     * @param alpha 透明度
     * @return int
     */
    private int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    /**
     * 开启动画
     */
    public void startAnim(){
        this.isAutoAnim = true;
        invalidate();
    }

    /**
     * 停止动画
     */
    public void stopAnim(){
        this.isAutoAnim = false;
    }

    /**
     * 停止并重置动画
     */
    public void resetAnim(){
        this.isAutoAnim = false;
        if(animMode == AnimMode.ROTATEMODE.getValue()
                || animMode == AnimMode.TAILMODE.getValue()
                || animMode == AnimMode.GARBMODE.getValue()
                || animMode == AnimMode.ZOOMMODE.getValue()
                || animMode == AnimMode.CHANGEMODE.getValue()){
            this.mIndex = lightIndex;
        } else if(animMode == AnimMode.DOTMODE.getValue()){
            this.mIndex = -1;
        }
        invalidate();
    }

}
