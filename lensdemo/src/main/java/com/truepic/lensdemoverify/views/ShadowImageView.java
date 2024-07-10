package com.truepic.lensdemoverify.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.truepic.lensdemoverify.R;

import jp.wasabeef.blurry.Blurry;

/**
 * Modified image view that adds drop shadow
 */
public class ShadowImageView extends androidx.appcompat.widget.AppCompatImageView {

    private boolean createShadow = true;
    private boolean firstInit = true;

    public ShadowImageView(@NonNull Context context) {
        super(context);
    }

    public ShadowImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        setBackground(null); // reset old background
        super.setImageResource(resId);
        createShadow = true;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setBackground(null); // reset old background
        super.setImageBitmap(bm);
        createShadow = true;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        setBackground(null); // reset old background
        super.setImageDrawable(drawable);
        createShadow = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (createShadow) {
            createShadow = false;

            // add little delay on first initialization for Blurry to capture everything
            int delay = 0;
            if (firstInit) {
                firstInit = false;
                delay = 100;
            }

            postDelayed(() -> {
                try {
                    Blurry.with(getContext()).radius(6).color(ContextCompat.getColor(getContext(), R.color.shadow)).capture(this).getAsync(bitmap -> {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        setBackground(drawable);
                    });
                } catch (Exception e) {
                    // safe to ignore
                }
            }, delay);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            createShadow = true;
        } else {
            createShadow = false;
            setBackground(null);
        }
    }
}
