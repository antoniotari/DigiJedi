package com.antoniotari.android.meanutil;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebSettings.ZoomDensity;

public class ScreenDimension 
{
	private static int _screenWidthPX = 0;
	private static int _screenHeightPX = 0;
	private static int _densityDpi = 0;
	private static DisplayMetrics _metrics;

	private static float _screenWidthDP = 0f;
	private static float _screenHeightDP = 0f;

	//private static ScreenDimension _instance=null;
	//the singleton is in atutil

	private ScreenDimension()
	{
	}

	protected ScreenDimension(Context context)
	{
		this();
		setMetrics(context);
	}

	/**
	 * set this before calling any other
	 * @param context
	 */
	public static void setMetrics(Context context) 
	{
		DisplayMetrics metrics = new DisplayMetrics();
		//activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

		if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{
			_screenWidthPX = metrics.widthPixels;
			_screenHeightPX = metrics.heightPixels;
			_densityDpi = metrics.densityDpi;
			_screenWidthDP = metrics.xdpi;
			_screenHeightDP = metrics.ydpi;
		}
		else
		{
			_screenHeightPX = metrics.widthPixels;
			_screenWidthPX = metrics.heightPixels;
			_densityDpi = metrics.densityDpi;
			_screenHeightDP = metrics.xdpi;
			_screenWidthDP = metrics.ydpi;
		}
	}


	public static float getWidthInches()
	{
		return _screenWidthPX / _screenWidthDP;
	}

	public static float getHeightInches()
	{
		return _screenHeightPX / _screenHeightDP;
	}

	public static double getDiagonalInches()
	{
		//The size of the diagonal in inches is equal to the square root of the height in inches squared plus the width in inches squared.
		return Math.sqrt(
				(getWidthInches() * getWidthInches())
				+ (getHeightInches() * getHeightInches())
				);
	}

	public static float getScreenWidthDP()
	{
		return _screenWidthDP;
	}

	public static float getScreenHeightDP()
	{
		return _screenHeightDP;
	}

	public static float screenHeightDP()
	{
		return _screenHeightDP;
	}

	/**
	 * @return the screenWidthPX
	 */
	public static int getScreenWidthPX() 
	{
		return _screenWidthPX;
	}

	public int screenWidthPX() 
	{
		return _screenWidthPX;
	}

	/**
	 * @param screenWidthPX
	 *            the screenWidthPX to set
	 */
	public static void setScreenWidthPX(int screenWidthPX) 
	{
		ScreenDimension._screenWidthPX = screenWidthPX;
	}

	/**
	 * @return the screenHeightPX
	 */
	public static int getScreenHeightPX() 
	{
		return _screenHeightPX;
	}

	/**
	 * @param screenHeightPX
	 *            the screenHeightPX to set
	 */
	public static void setScreenHeightPX(int screenHeightPX) 
	{
		ScreenDimension._screenHeightPX = screenHeightPX;
	}

	/**
	 * @return the densityDpi
	 */
	public static int getDensityDpi() 
	{
		return _densityDpi;
	}

	/**
	 * @param densityDpi
	 *            the densityDpi to set
	 */
	public static void setDensityDpi(int densityDpi) 
	{
		ScreenDimension._densityDpi = densityDpi;
	}

	/**
	 * @return the metrics
	 */
	protected static DisplayMetrics getMetrics() 
	{
		return _metrics;
	}

	/**
	 * @return the density
	 */
	public static ZoomDensity getZoomDensity() 
	{
		if (_densityDpi < 140)
			return ZoomDensity.CLOSE;
		if (_densityDpi < 210)
			return ZoomDensity.MEDIUM;
		return ZoomDensity.FAR;
	}

	public static int getStatusBarHeight() 
	{
		ZoomDensity zd = getZoomDensity();

		if (zd == ZoomDensity.CLOSE)
			return 19;
		else if (zd == ZoomDensity.MEDIUM)
			return 25;
		else 
			return 38;

	}

}