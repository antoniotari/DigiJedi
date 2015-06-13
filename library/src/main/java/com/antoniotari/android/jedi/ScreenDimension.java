package com.antoniotari.android.jedi;

import com.antoniotari.android.injection.ApplicationGraph;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebSettings.ZoomDensity;

public class ScreenDimension {
    private int _screenWidthPX = 0;
    private int _screenHeightPX = 0;
    private int _densityDpi = 0;
    private float _screenWidthDP = 0f;
    private float _screenHeightDP = 0f;
    private DisplayMetrics _metrics;

    private ScreenDimension() {
        ApplicationGraph.getObjectGraph().inject(this);
    }

    public ScreenDimension(Context context) {
        this();
        setMetrics(context);
    }

    public static ScreenDimension getInstance(){
        return ApplicationGraph.getObjectGraph().get(ScreenDimension.class);
    }

    private void setMetrics(Context context) {
        _metrics = new DisplayMetrics();
        //activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(_metrics);

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            _screenWidthPX = _metrics.widthPixels;
            _screenHeightPX = _metrics.heightPixels;
            _densityDpi = _metrics.densityDpi;
            _screenWidthDP = _metrics.xdpi;
            _screenHeightDP = _metrics.ydpi;
        } else {
            _screenHeightPX = _metrics.widthPixels;
            _screenWidthPX = _metrics.heightPixels;
            _densityDpi = _metrics.densityDpi;
            _screenHeightDP = _metrics.xdpi;
            _screenWidthDP = _metrics.ydpi;
        }
    }

    public float getWidthInches() {
        return _screenWidthPX / _screenWidthDP;
    }

    public float getHeightInches() {
        return _screenHeightPX / _screenHeightDP;
    }

    public double getDiagonalInches() {
        //The size of the diagonal in inches is equal to the square root of the height in inches squared plus the width in inches squared.
        return Math.sqrt(
                (getWidthInches() * getWidthInches())
                        + (getHeightInches() * getHeightInches())
        );
    }

    public float getScreenWidthDP() {
        return _screenWidthDP;
    }

    public float getScreenHeightDP() {
        return _screenHeightDP;
    }

    public float screenHeightDP() {
        return _screenHeightDP;
    }

    /**
     * @return the screenWidthPX
     */
    public int getScreenWidthPX() {
        return _screenWidthPX;
    }

    public int screenWidthPX() {
        return _screenWidthPX;
    }

    /**
     * @param screenWidthPX the screenWidthPX to set
     */
    public void setScreenWidthPX(int screenWidthPX) {
        _screenWidthPX = screenWidthPX;
    }

    /**
     * @return the screenHeightPX
     */
    public int getScreenHeightPX() {
        return _screenHeightPX;
    }

    /**
     * @param screenHeightPX the screenHeightPX to set
     */
    public void setScreenHeightPX(int screenHeightPX) {
        _screenHeightPX = screenHeightPX;
    }

    /**
     * @return the densityDpi
     */
    public int getDensityDpi() {
        return _densityDpi;
    }

    /**
     * @param densityDpi the densityDpi to set
     */
    public void setDensityDpi(int densityDpi) {
        _densityDpi = densityDpi;
    }

    /**
     * @return the metrics
     */
    protected DisplayMetrics getMetrics() {
        return _metrics;
    }

    /**
     * @return the density
     */
    public ZoomDensity getZoomDensity() {
        if (_densityDpi < 140) {
            return ZoomDensity.CLOSE;
        }
        if (_densityDpi < 210) {
            return ZoomDensity.MEDIUM;
        }
        return ZoomDensity.FAR;
    }

    public int getStatusBarHeight() {
        ZoomDensity zd = getZoomDensity();

        if (zd == ZoomDensity.CLOSE) {
            return 19;
        } else if (zd == ZoomDensity.MEDIUM) {
            return 25;
        } else {
            return 38;
        }
    }
}