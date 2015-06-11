package com.antoniotari.android.injection;

import android.app.Application;

/**
 * ObjectGraph set by the Application on app start.
 *
 */
public class ApplicationGraph {

    private static JediComponent sComponent;

    public static JediComponent getObjectGraph() {
        if (sComponent == null) {
            throw new IllegalStateException("ObjectGraph has not been set");
        }
        return sComponent;
    }

    public static void setComponent(JediComponent component) {
        sComponent = component;
    }

    public static JediComponent init(Application app) {
        return DaggerJediComponent.builder()
                .jediModule(new JediModule(app))
                .build();
    }

}
