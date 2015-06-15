package com.antoniotari.android.injection;

/**
 * Created by antonio on 14/05/15.
 */

import com.antoniotari.android.jedi.ATFontUtil;
import com.antoniotari.android.jedi.JediUtil;
import com.antoniotari.android.jedi.ScreenDimension;
import com.squareup.okhttp.OkHttpClient;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Injection for Activities. You need to add the activity name here if you ever call ObjectGraph.inject(this) from an Activity or you will get an exception.
 * <p/>
 */
@Module (
        // Note: we can get rid of injects= when dagger 2.0 is released.
        injects = {
                ATFontUtil.class,
                ScreenDimension.class,
                JediUtil.class
        },
        includes = {
        },
        complete = false,
        library = true
)
public class JediModule {
    private final Context mContext;

    public JediModule(Application application) {
        mContext = application.getApplicationContext();
    }

    /**
     * Allow the application context to be injected but require that it be annotated with {@link ForApplication @ForApplication} to explicitly differentiate it
     * from an activity context.
     */
    @Provides
    @Singleton @ForApplication
    Context provideApplicationContext() {
        return mContext;
    }

    @Provides @Singleton
    ScreenDimension provideScreenDimension(@ForApplication Context context){
        return new ScreenDimension(context);
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides @Singleton
    JediUtil provideJediUtil(@ForApplication Context context) {
        return new JediUtil(context);
    }
}
