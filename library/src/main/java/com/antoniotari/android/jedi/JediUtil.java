package com.antoniotari.android.jedi;

import com.antoniotari.android.networking.HttpValues;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.x500.X500Principal;

public class JediUtil {
    private static JediUtil instance = null;
    private static String packageName;
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String NULL_VALUE = "__NULL__";
    public static final String EMPTY_VALUE = "__EMPTY__";
    public static String UTF8 = "UTF-8";
    String appName = JediUtil.class.getSimpleName();
    static boolean isDebug = true;
    static boolean isReleaseSigned = false;
    //boolean useExternalStorage=false;
    boolean sendDataToServer = false;
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    private static ScreenDimension _screen = null;

    //-----------------------------------------------------------------------------
    //-----------------the constructor should be placed in the application onCreate
    public JediUtil(Context context) {
        //if(!(c instanceof Application))
        //	throw new IllegalArgumentException ("please provide application context, it's a good idea to initialize ATUtil inside onCreate of the Application class");
        //if(instance == null){
        //context=c;
        packageName = context.getPackageName();
        isReleaseSigned = !isDebuggable(context);
        instance = this;

        //initialize the screenDimension
        _screen = new ScreenDimension(context);
        //ScreenDimension.setMetrics(context);

        HttpValues.generateHttpValues(context);

        //load the saved md5s, save status in case the file not exist
        saveStatus(context);
        MD5.readMD5Ints(context);
        MD5.readMD5s(context);
    }

    //-----------------------------------------------------------------------------
    //-----------------

    /**
     * this must be placed at the termination of the app
     */
    public static void close(Context context) {
        saveStatus(context);
    }

    public static void saveStatus(Context context) {
        MD5.storeMD5s(context);
    }

//	//-----------------------------------------------------------------------------
//	//used internally in this class
//	private ATUtil()
//	{
//		packageName=ATUtil.class.getPackage().getName();
//		isReleaseSigned=false;
//		instance=this;
//	}

    //-----------------------------------------------------------------------------
    public JediUtil(Context c, String dateFormat, String appNameVar, Boolean isDebugVar) {
        this(c);
        dateFormat = dateFormat == null ? dateFormat : dateFormat;
        appName = appNameVar == null ? appName : appNameVar;
        isDebug = isDebugVar == null ? isDebug : isDebugVar;
        instance = this;
    }

    //---------------------------------------------
    //-------methods for BUILDING the object

    //-----------------------------------------------------------------------------
    public JediUtil setDateFormat(String dateFormat) {
        dateFormat = dateFormat == null ? dateFormat : dateFormat;
        return this;
    }

    //-----------------------------------------------------------------------------
    public JediUtil setAppName(String appName) {
        this.appName = appName == null ? this.appName : appName;
        return this;
    }

    //-----------------------------------------------------------------------------
    public JediUtil setSendDataToServer(Boolean sendDataToServerVar) {
        if (sendDataToServerVar != null) {
            sendDataToServer = sendDataToServerVar;
        }
        return this;
    }

    //-----------------------------------------------------------------------------
    public JediUtil setDebug(Boolean isDebugVar) {
        if (isDebugVar != null) {
            isDebug = isDebugVar;
        }
        return this;
    }

    //-----------------------------------------------------------------------------
    public JediUtil useExternalStorage(Boolean useExternalStorageVar) {
        if (useExternalStorageVar != null) {
            //useExternalStorage=useExternalStorageVar;
            FileUtil.getInstance().setUseExternalStorage(useExternalStorageVar);
        }
        return this;
    }

    //-----------------------------------------------------------------------------
    //-----------------singleton get instance
    public static JediUtil getInstance(Context context) {
        if (instance == null) {
            instance = new JediUtil(context);
        }
        return instance;
    }

//	//-----------------------------------------------------------------------------
//	//-----------------singleton get instance only used internally in this class
//	public static ATUtil getInstance()
//	{
//		if(instance == null) 
//			instance = new ATUtil();
//		return instance;
//	}

    //-----------------------------------------------------------------------------
    //screen dimension
    public static ScreenDimension getScreen(Context context) {
        if (_screen == null) {
            _screen = new ScreenDimension(context);
        }
        return _screen;
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public String getDateFormat() {
        return dateFormat;
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public String getAppName() {
        return appName;
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public boolean getIsDebug() {
        return isDebug;
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public boolean getIsReleaseSigned() {
        return isReleaseSigned;
    }

    //-----------------------------------------------------------------------------
    //-----------------
//	public String getPackageName()
//	{
//		return packageName;
//	}

    //-----------------------------------------------------------------------------
    //-----------------
    public static String getPackageName() {
        return packageName;
    }

	/*
	//-----------------------------------------------------------------------------
	//-----------------
	public Context getContext()
	{
		return context;
	}
	 */

    //-----------------------------------------------------------------------------
    //-----------------
    public static Date StringToDate(String dateS) {
        return StringToDate(dateS,
                //ATUtil.getInstance(context).getDateFormat()
                dateFormat);
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static Date StringToDate(String dateS, String dateFormat) {
        if (dateS == null) {
            return todayD();
        }
        try {
            Date date = new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateS);
            return date;
        } catch (ParseException d) {
        }
        return todayD();
    }

    //-----------------------------------------------------------------
    //------------a method that takes the .txt file and returns a String
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        String resS = sb.toString();
        //remove the last escape char
        return resS.substring(0, resS.length() - 1);
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static String ConvertRawToString(Context context, int resorceRaw) {
        InputStream is = context.getResources().openRawResource(resorceRaw);
        return convertStreamToString(is);
    }

    //-----------------------------------------------------------------
    //------------
    public static void hideKeyboard(Activity c) {
        InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);

        //check if no view has focus:
        View v = c.getCurrentFocus();
        if (v == null) {
            return;
        }

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //-----------------------------------------------------------------
    //------------
    public static void showKeyboard(Context context, EditText edittext) {
        final InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static void SimpleDialog(Context c, String message, String title) {
        AlertDialog.Builder buyAlert = null;
        buyAlert = new AlertDialog.Builder(c);

        buyAlert.setTitle(title);
        buyAlert.setMessage(message);

        buyAlert.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

		/*buyAlert.setPositiveButton("Buy Now", new DialogInterface.OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int id)
    		{
    		}
    	});*/
        buyAlert.show();
    }

    public static void SimpleDialog(Context c, String message) {
        SimpleDialog(c, message, JediUtil.getInstance(c).getAppName());
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static void ShowWebDialog(String title, String url, Context c) {
        AlertDialog.Builder alert = new AlertDialog.Builder(c);

        alert.setTitle(title);
        WebView wv = new WebView(c);

        wv.loadUrl(url);

        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alert.show();
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static Date todayD() {
        return new Date();
    }

    public static String today() {
        String dateS = new SimpleDateFormat(
                //ATUtil.getInstance().getDateFormat(),
                dateFormat,
                Locale.ENGLISH).format(todayD());
        //Log.d(tags.TAG_DEBUG,dateS);
        return dateS;
    }

    //-----------------------------------------------------------------------------
    //-----------------
    public static boolean isDebuggable(Context context) {
        boolean debuggable = false;

        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (NameNotFoundException e) {
            //debuggable variable will remain false
        } catch (CertificateException e) {
            //debuggable variable will remain false
        }
        return debuggable;
    }

    /**
     * Device Type Enumerator.
     */
    public enum DeviceType {
        UNKNOWN,
        PHONE,
        TABLET
    }

    private static final int TABLET_INCH_THRESHOLD = 8;

    /**
     * Gets a Device Type. Either Phone, Tablet or Unknown.
     *
     * @param context               context
     * @param tablet_inch_threshold threshold for deciding if is tablet
     *
     * @return PHONE, TABLET OR UNKNOWN
     */
    public static DeviceType getDeviceType(Context context, int tablet_inch_threshold) {
        //if (context == null) return DeviceType.UNKNOWN;

        //DisplayMetrics metrics = new DisplayMetrics();
        //WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //window.getDefaultDisplay().getMetrics(metrics);

        //int widthPixels = metrics.widthPixels;
        //int heightPixels = metrics.heightPixels;

        //float widthDpi = metrics.xdpi;
        //float heightDpi = metrics.ydpi;

        //float widthInches = widthPixels / widthDpi;
        //float heightInches = heightPixels / heightDpi;

        //The size of the diagonal in inches is equal to the square root of the height in inches squared plus the width in inches squared.
        //double diagonalInches = Math.sqrt(
        //		(widthInches * widthInches)
        //		+ (heightInches * heightInches)
        //		);

        ScreenDimension.setMetrics(context);
        double diagonalInches = ScreenDimension.getDiagonalInches();

        if (diagonalInches > tablet_inch_threshold) {
            return DeviceType.TABLET;
        } else {
            return DeviceType.PHONE;
        }
    }

    /**
     *
     * @param context
     * @return
     */
    public static DeviceType getDeviceType(Context context) {
        return getDeviceType(context, TABLET_INCH_THRESHOLD);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {
        return (getDeviceType(context) == DeviceType.PHONE);
    }

    public static boolean isPhone(Context context, int tabletThreshold) {
        return (getDeviceType(context, tabletThreshold) == DeviceType.PHONE);
    }

    //-------------------------------------------------------------------
    public static boolean isAndroidEmulator(Context context) {
        //String model = Build.MODEL;
        //Log.d(tags.TAG_DEBUG, "model=" + model);
        String product = Build.PRODUCT;
        //Log.d(tags.TAG_DEBUG, "product=" + product);
        boolean isEmulator = false;
        if (product != null) {
            isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
        }
        //Log.d(tags.TAG_DEBUG, "isEmulator=" + isEmulator);
        return isEmulator;
    }

    //-------------------------------------------------------------------
    public static int OpposeColor(int ColorToInvert) {
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

    public static int ContrastColor(int colorToInvert) {
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

    public static boolean isMainThread() {
        return Thread.currentThread().getName().equalsIgnoreCase("main");
    }

    public static int getDrawableIdFromName(Context context, String name) {
        return context.getResources().getIdentifier(name
                //.toLowerCase(Locale.getDefault())
                , "drawable", context.getPackageName());
    }

    public static int getResourceIdFromName(Context context, String resourceName, String name) {
        return context.getResources().getIdentifier(name
                //.toLowerCase(Locale.getDefault())
                , resourceName, context.getPackageName());
    }
}