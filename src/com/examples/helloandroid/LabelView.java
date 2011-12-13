package com.examples.helloandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.util.AttributeSet;
import android.util.Log;

public class LabelView extends View {
    private Paint mTextPaint;
    private String mText;
    private int mAscent;
	
	private int measuredW = 0;
	private int measuredH = 0;
	
	private int mLevel = 0;
	
	private static final String TAG = "LabelView";

    public LabelView(Context context) {
        super(context);
        initLabelView();
		Log.i(TAG, "Invocado o 1o construtor");
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();
		Log.i(TAG, "Invocando o 2o construtor");
    }
	
	// public LabelView(Context context, AttributeSet attrs, Map params) {
		// super
	// }

    private final void initLabelView() {
        mText = "Rodrigo";
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFFFF0000);
		// setHeight(getResources().getDisplayMetrics().heightPixels);
		// setWidth(20);
    }

    public void setLevel(int level) {
        mLevel = level;
        requestLayout();
        invalidate();
    }

    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.i(TAG, "Invocado o evento onMeasure");
		measuredW = measureWidth(widthMeasureSpec);
		measuredH = measureHeight(heightMeasureSpec);
		Log.i(TAG, "measuredW = " + Integer.toString(measuredW) + "; measuredH = " + Integer.toString(measuredH));
        setMeasuredDimension(measuredW, measuredH);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) mTextPaint.measureText(mText) + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
		Log.i(TAG, "Invocado o evento onDraw");
		Log.i(TAG, "Level atual = " + Integer.toString(mLevel));
        super.onDraw(canvas);
        //canvas.drawText(mText, getPaddingLeft(), getPaddingTop() - mAscent, mTextPaint);
		//canvas.drawCircle(1.0, 1.0, 10.0, mTextPaint);
				
		canvas.drawColor(0xFF000000);
		
		// int localLevel = (mLevel < 10) ? 10 : mLevel;
		int localLevel = mLevel;
		float barHeight = (float) ((localLevel * measuredH) / 50f);
		
		RectF rc = new RectF(0.0f, measuredH - barHeight, (float) measuredW, measuredH);
		canvas.drawRoundRect(rc, 3.0f, 3.0f, mTextPaint);
    }
}
