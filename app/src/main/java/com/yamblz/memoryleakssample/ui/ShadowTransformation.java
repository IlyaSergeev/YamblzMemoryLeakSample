package com.yamblz.memoryleakssample.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Volha on 08.08.2016.
 */

public class ShadowTransformation implements Transformation {

    private final Paint paint;
    private final Rect rect;
    private final int colorFrom = Color.argb(0, 0, 0, 0);
    private final int colorTo = Color.argb(255, 0, 0, 0);
    private final int[] gradientColors = new int[]{colorFrom, colorTo};

    public ShadowTransformation() {
        paint = new Paint();
        rect = new Rect();
    }

    @Override
    public Bitmap transform(Bitmap source) {

        int width = source.getWidth();
        int height = source.getHeight();

        int shadowHeight = height / 2;

        rect.set(0, height - shadowHeight, width, height);
        paint.setStyle(Paint.Style.FILL);

        Shader shader = new LinearGradient(0, height - shadowHeight, 0, height, gradientColors, null, Shader.TileMode.MIRROR);
        paint.setShader(shader);

        Bitmap notImmutableBitmap = source.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(notImmutableBitmap);
        canvas.drawRect(rect, paint);
        source.recycle();

        return notImmutableBitmap;
    }

    @Override
    public String key() {
        return "shadow";
    }
}

