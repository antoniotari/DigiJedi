package com.antoniotari.android.jedi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ATFontUtil {

    //font adapting
    public static final float FONT_SIZE = 14.0f;
    public static final float DESCRIPTION_FONT_SIZE = 13.0f;
    public static final float SPINNER_FONT_SIZE = 16.0f;
    public static final float CONTESTLIST_FONT_SIZE = 15.0f;
    public static final float VIDEOINFO_FONT_SIZE = 13.0f;

    private static Typeface defaultFont = null;
    private static Typeface defaultFontDigital = null;
    private static Typeface defaultFontCaption = null;

    public final static String DEFAULT_FONT = "Muli-Regular.ttf";
    public final static String DEFAULT_FONT_DIGITAL = "DIGITALDREAMFAT.ttf";
    public final static String DEFAULT_FONT_CAPTION = "Muli-Light.ttf";//"DIGITALDREAMFAT.ttf";
    private static Float fontResizeFactor = null;
    //the difference in dimension between the standard font and the custom font
    private static float fontSizeFact = 0.5f;

    private static String appFontName = null;
    private static String appFontSecName = null;
    private static Float appFontSize = null;
    private static Float appFontSecSize = null;
    private static final String KEY_FONTNAME = ATFontUtil.class.getCanonicalName() + "keys.fontname";
    private static final String KEY_FONTSECNAME = ATFontUtil.class.getCanonicalName() + "keys.fontsecname";
    private static final String KEY_FONTSIZE = ATFontUtil.class.getCanonicalName() + "keys.fontsize";
    private static final String KEY_FONTSECSIZE = ATFontUtil.class.getCanonicalName() + "keys.fontsecsize";
    private static final String KEY_COMPLEX = ATFontUtil.class.getCanonicalName() + "keys.complexpreferenceskey";
    private static final String KEY_FONTBUNDLE = ATFontUtil.class.getCanonicalName() + "keys.fontbundle";
    private static final String KEY_FONTRESIZEFACTOR = ATFontUtil.class.getCanonicalName() + "keys.fontresizefactor";
    private static Typeface appTypeface = null;
    private static Typeface appTypefaceSec = null;

    static class FontConfig {
        float fontSize;
        float fontSizeSecondary;
        String fontName;
        String fontNameSecondary;

        FontConfig(String fontName, float fontSize, String fontNameSecondary, float fontSizeSecondary) {
            this.fontName = fontName;
            this.fontNameSecondary = fontNameSecondary;
            this.fontSize = fontSize;
            this.fontSizeSecondary = fontSizeSecondary;
        }
    }

    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------

    public static Typeface appTypeface(Context context) {
        if (appTypeface == null) {
            appTypeface = Typeface.createFromAsset(context.getAssets(), appFontName(context));
        }
        return appTypeface;
    }

    //-----------------------------------------------------------------------------------------------------

    public static Typeface appSecondaryTypeface(Context context) {
        if (appTypefaceSec == null) {
            appTypefaceSec = Typeface.createFromAsset(context.getAssets(), appFontSecondaryName(context));
        }
        return appTypefaceSec;
    }

    //-----------------------------------------------------------------------------------------------------

    public static void appFont(Context c, String name, float size, String secname, float secsize) {
        if (name == null || secname == null) {
            return;
        }
        if (size <= 0) {
            size = FONT_SIZE;
        }
        if (secsize <= 0) {
            secsize = FONT_SIZE;
        }

        Float savedSize = appFontSize(c);
        String savedName = appFontName(c);

        if (savedSize == null || savedName == null || savedSize <= 0 || (!savedName.equalsIgnoreCase(name)) || (!(size == savedSize))) {
//			Bundle b=new Bundle();
//			b.putFloat(KEY_FONTSIZE, size);
//			b.putString(KEY_FONTNAME,name);
//			b.putFloat(KEY_FONTSECSIZE, secsize);
//			b.putString(KEY_FONTSECNAME,secname);
//			ComplexPreferences cPref = ComplexPreferences.getComplexPreferences(c, KEY_COMPLEX, Context.MODE_PRIVATE);
//			cPref.putObject(c,KEY_FONTBUNDLE, b);
//			cPref.commit();	
//			ComplexPreferences.putBundle(c, KEY_FONTBUNDLE, b);

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(c, "font_prefs", Context.MODE_PRIVATE);
            ;
            complexPreferences.putObject(KEY_FONTBUNDLE, new FontConfig(name, size, secname, secsize));
            complexPreferences.commit();

            appFontSize = size;
            appFontName = name;
            appFontSecSize = secsize;
            appFontSecName = secname;
        }
    }

    //-----------------------------------------------------------------------------------------------------
    public static Bundle __appFont(Context c) {
        return ComplexPreferences
                //.getComplexPreferences(c, KEY_COMPLEX, Context.MODE_PRIVATE)
                .getBundle(c, KEY_FONTBUNDLE);
    }

    public static FontConfig appFont(Context c) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(c, "font_prefs", Context.MODE_PRIVATE);
        return complexPreferences.getObject(KEY_FONTBUNDLE, FontConfig.class);
    }

    //-----------------------------------------------------------------------------------------------------
    public static String appFontName(Context c) {
        if (appFontName == null) {
            if (appFont(c) != null) {
                appFontName = appFont(c).fontName;//getString(KEY_FONTNAME);
            }
            if (appFontName == null) {
                appFontName = DEFAULT_FONT;
            }
        }
        return appFontName;
    }

    //-----------------------------------------------------------------------------------------------------
    public static float appFontSize(Context c) {
        if (appFontSize == null || appFontSize <= 0) {
            if (appFont(c) != null) {
                appFontSize = appFont(c).fontSize;//getFloat(KEY_FONTSIZE);
            }
            if (appFontSize == null || appFontSize <= 0) {
                appFontSize = FONT_SIZE;
            }
        }
        return appFontSize;
    }

    //-----------------------------------------------------------------------------------------------------
    public static String appFontSecondaryName(Context c) {
        if (appFontSecName == null) {
            if (appFont(c) != null) {
                appFontSecName = appFont(c).fontNameSecondary;//getString(KEY_FONTSECNAME);
            }
            if (appFontSecName == null) {
                appFontSecName = DEFAULT_FONT;
            }
        }
        return appFontSecName;
    }

    //-----------------------------------------------------------------------------------------------------
    public static Float appFontSecondarySize(Context c) {
        if (appFontSecSize == null || appFontSecSize <= 0) {
            if (appFont(c) != null) {
                appFontSecSize = appFont(c).fontSizeSecondary;//getFloat(KEY_FONTSECSIZE);
            }
            if (appFontSecSize == null || appFontSecSize <= 0) {
                appFontSecSize = FONT_SIZE;
            }
        }
        return appFontSecSize;
    }

    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------
    // getting the font resize factor. factor 1 is 320x480
    public static void GetFontResizeFactor_(Activity context) {
        if (context == null) {
            fontResizeFactor_(context, 1.0f);
        }
        ScreenDimension.setMetrics(context);

        float widthFact = Float.valueOf((float) ScreenDimension.getScreenWidthPX() / 360.0f);
        float heightFact = Float.valueOf((float) ScreenDimension.getScreenHeightPX() / 640.0f);
        float dpiFact = Float.valueOf((float) ScreenDimension.getDensityDpi() / 160.0f);

        float fact = (((widthFact / dpiFact + heightFact / dpiFact) / 2.0f)) - 0.0f;
        fontResizeFactor_(context, fact < 0.66f ? 0.66f : fact);
        //Log.d(tags.WIDGET,"resize factor="+fontResizeFactor()+"    fact:"+fact+" "+ScreenDimension.getScreenWidthPX()+" "+ScreenDimension.getScreenHeightPX()+" "+ScreenDimension.getDensityDpi());
        //fontResizeFactor((((widthFact/dpiFact+heightFact/dpiFact)/2))-0.0f);
        //if (fontResizeFactor()<1)fontResizeFactor(1.0f);
    }

    //-------------------------------------------------------------------

    public static Typeface defaultFontDigital(Context context) {
        if (defaultFontDigital == null) {
            defaultFontDigital = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT_DIGITAL);
        }
        return defaultFontDigital;
    }

    //-------------------------------------------------------------------

    public static Typeface defaultFontCaption(Context context) {
        if (defaultFontCaption == null) {
            defaultFontCaption = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT_CAPTION);
        }
        return defaultFontCaption;
    }
    //-------------------------------------------------------------------

    public static Typeface defaultFont(Context context) {
        if (defaultFont == null) {
            defaultFont = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT);
        }
        return defaultFont;
    }

    //-------------------------------------------------------------------

    public static float fontSize(Context context) {
        return appFontSize(context)/* *fontResizeFactor(context)*/;
    }

    //-------------------------------------------------------------------

	/*public static float fontSize(TextView tv)
	{*/
    //return (tv.getTextSize()/* *fontResizeFactor(tv.getContext()))*/*fontSizeFact);
    //}

    //-------------------------------------------------------------------
    //getter
    public static float fontResizeFactor_(Context context) {
        if (fontResizeFactor == null || fontResizeFactor == 0.0f) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Float savedValue = preferences.getFloat(KEY_FONTRESIZEFACTOR, 1.0f);
            fontResizeFactor = savedValue;
        }
        return fontResizeFactor;
    }

    //setter
    public static Float fontResizeFactor_(Context context, float newValue) {
        float actualValue = fontResizeFactor_(context);
        if (actualValue == newValue) {
            return actualValue;
        }
        fontResizeFactor = newValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(KEY_FONTRESIZEFACTOR, newValue);
        editor.commit();
        return newValue;
    }

    //------------------------------------------------------------------------------------------
}
