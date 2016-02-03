package com.baranau.sergey.scarlett_w.Global;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.baranau.sergey.scarlett_w.R;

/**
 * Created by sergey on 2/3/16.
 */
public class GlobalFunc {

    public static void addScaledImage(Activity activity, TextView view, int left, int top, int right, int bottom) {
        int size = view.getHeight();
        Drawable leftDrawable = null;
        Drawable rightDrawable = null;
        Drawable topDrawable = null;
        Drawable bottomDrawable = null;
        if (left > 0) {
            try {
                Drawable dr = activity.getResources().getDrawable(left);
                assert ((BitmapDrawable) dr) != null;
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                leftDrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (right > 0) {
            try {
                Drawable dr = activity.getResources().getDrawable(right);
                Bitmap bitmap = ((BitmapDrawable) dr) != null ? ((BitmapDrawable) dr).getBitmap() : null;
                rightDrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (left > 0) {
            try {
                Drawable dr = activity.getResources().getDrawable(top);
                Bitmap bitmap = ((BitmapDrawable) dr) != null ? ((BitmapDrawable) dr).getBitmap() : null;
                topDrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (left > 0) {
            try {
                Drawable dr = activity.getResources().getDrawable(bottom);
                Bitmap bitmap = ((BitmapDrawable) dr) != null ? ((BitmapDrawable) dr).getBitmap() : null;
                bottomDrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        view.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }
}
