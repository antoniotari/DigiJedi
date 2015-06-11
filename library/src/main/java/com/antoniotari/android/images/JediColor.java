package com.antoniotari.android.images;

import android.graphics.Color;

/**
 * Created by antonio on 11/06/15.
 */
public class JediColor {


    //-------------------------------------------------------------------
    public static int opposeColor(int ColorToInvert) {
        int RGBMAX = 255;

        float[] hsv = new float[3];
        float H;
        Color.RGBToHSV(RGBMAX - Color.red(ColorToInvert), RGBMAX - Color.green(ColorToInvert), RGBMAX - Color.blue(ColorToInvert), hsv);

        H = (float) (hsv[0] + 0.5);

        if (H > 1) {
            H -= 1;
        }

        return Color.HSVToColor(hsv);
    }

    public static int contrastColor(int colorToInvert) {
        int RGBMAX = 255;

        float[] hsv = new float[3];
        float H;
        int r = RGBMAX - Color.red(colorToInvert);
        int g = RGBMAX - Color.green(colorToInvert);
        int b = RGBMAX - Color.blue(colorToInvert);
        //int maj127=(r>127?1:0)+(g>127?1:0)+(b>127?1:0);
        if ((r + b + g) >= ((RGBMAX * 3) / 2)) {
            Color.RGBToHSV(r < 200 ? 200 : r, g < 200 ? 200 : g, b < 200 ? 200 : b, hsv);
        } else {
            Color.RGBToHSV(r > 55 ? 55 : r, g > 55 ? 55 : g, b > 55 ? 55 : b, hsv);
        }

        H = (float) (hsv[0] + 0.5);

        if (H > 1) {
            H -= 1;
        }

        return Color.HSVToColor(hsv);
    }

}
