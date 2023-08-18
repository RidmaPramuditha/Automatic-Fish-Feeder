package com.company.automaticfishfeederapp;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class CustomProgressBar extends View {
    private Paint progressPaint;
    private Paint fillPaint;
    private final int BAR_HEIGHT = 8; // = radius of small half circle at left of cus tom bar
    private final int SMALL_CIRLCE_RADIUS = BAR_HEIGHT; //5dp
    private final int CIRCLE_RADIUS = 20; // 15dp
    private final int STROKE_WIDTH = 1;
    private int heightWithoutPadding; // without Padding
    private int widthWithoutPaddding;  // without Padding
    private float barWidth; // in pixel
    private float pxBarHeight;
    private float pxBarLenght;
    private float pxCirleRadius;
    private int progress;
    private float textSize;

    public CustomProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float deviceDensity = getResources().getDisplayMetrics().density;
        pxBarHeight = BAR_HEIGHT * deviceDensity;
        pxCirleRadius = CIRCLE_RADIUS * deviceDensity;
        textSize = 12 * deviceDensity;

        progressPaint = new Paint();
        progressPaint.setStrokeWidth(2);
        progressPaint.setColor(getResources().getColor(R.color.red));

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(getResources().getColor(R.color.red));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        heightWithoutPadding = getHeight() - getPaddingBottom();
        widthWithoutPaddding = getWidth() - getPaddingRight();
        barWidth = widthWithoutPaddding - pxBarHeight - pxCirleRadius - (float) (Math.sqrt(Math.pow(pxCirleRadius, 2) - Math.pow(pxBarHeight, 2)));


        // Draw the half at the end
        // if the progess > 0 , fill the small half cicle at the left
        if (progress > 0) {
            progressPaint.setStyle(Paint.Style.FILL);
            progressPaint.setColor(getResources().getColor(R.color.red));
        }
        RectF segment = new RectF();
        Path path = new Path();
        segment.set(getPaddingLeft(), getHeight() / 2 - pxBarHeight, getPaddingLeft() + 2 * pxBarHeight, getHeight() / 2 + pxBarHeight);
        path.addArc(segment, 90, 180);
        canvas.drawPath(path, progressPaint);

        // Draw the two line == the main of bar
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(getResources().getColor(R.color.red));
        linePaint.setStrokeWidth(2);
        float endlineX = (float) (widthWithoutPaddding - pxCirleRadius - 2 * Math.sqrt(2) / 3 * pxCirleRadius);
        float startlineX = getPaddingLeft() + pxBarHeight; // == + small circle radius
        float abouveLineY = getHeight() / 2 - pxBarHeight;
        float belowLineY = getHeight() / 2 + pxBarHeight;
        pxBarLenght = endlineX - startlineX;
        canvas.drawLine(startlineX, abouveLineY, endlineX, abouveLineY, linePaint);
        canvas.drawLine(startlineX, belowLineY, endlineX, belowLineY, linePaint);

//         Draw the big circle at right

        segment.set(widthWithoutPaddding - 2 * pxCirleRadius, getHeight() / 2 - pxCirleRadius, widthWithoutPaddding, getHeight() / 2 + pxCirleRadius);
        path.rewind();
        float angle = (float) Math.toDegrees(Math.atan(1 / (2 * Math.sqrt(2)))); // Why ? because I said that
        path.addArc(segment, 180 + angle, 360 - 2 * angle);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(getResources().getColor(R.color.red));
        canvas.drawPath(path, progressPaint);

        // Draw the color fill progress on bar
        float totalLength = getWidth() - getPaddingRight() - getPaddingLeft();
        int firstThresol = (int) (100 * pxBarHeight / totalLength);
        int secondThresol = (int) (100 * (pxBarHeight + pxBarLenght) / totalLength);
        int thirdThresol = (int) (100 * (totalLength - pxCirleRadius) / totalLength);
        float lengthofProgress = totalLength * progress / 100;
        if (progress > firstThresol) {
            if (progress <= secondThresol) {
                // fill the bar with color
                canvas.drawRect(startlineX, abouveLineY, getPaddingLeft() + lengthofProgress, belowLineY, fillPaint);
            } else {
                // fill all the bar with color and also the cirle at right
                canvas.drawRect(startlineX, abouveLineY, getPaddingLeft() + pxBarHeight + pxBarLenght, belowLineY, fillPaint); // fill the bar
                path.rewind();
                float angle2 = 0;
                if (progress <= thirdThresol) {
                    angle2 = (float) Math.toDegrees(Math.acos((totalLength - lengthofProgress - pxCirleRadius) / pxCirleRadius));
                    path.addArc(segment, 180 - angle2, 2 * angle2);
                } else {
                    angle2 = (float) Math.toDegrees(Math.acos((lengthofProgress - totalLength + pxCirleRadius) / pxCirleRadius));
                    path.addArc(segment, angle2, 360 - 2 * angle2);
                }
                canvas.drawPath(path, fillPaint);
            }
        }

        // Draw the text display progress
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();
        canvas.drawText(progress + "°C", segment.centerX(), segment.centerY() + textOffset, textPaint);

    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
