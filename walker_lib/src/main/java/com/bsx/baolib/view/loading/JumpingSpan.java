package com.bsx.baolib.view.loading;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * summary :JumpingSpan
 * time    :2016/10/27 14:52
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */

public class JumpingSpan extends ReplacementSpan {

    private float translationX = 0;
    private float translationY = 0;

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        return (int) paint.measureText(text, start, end);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        canvas.drawText(text, start, end, x + translationX, y + translationY, paint);
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }
}
