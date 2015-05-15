package com.antoniotari.android.injection;

import dagger.ObjectGraph;

/**
 * ObjectGraph set by the Application on app start.
 *
 * Created by joshallen@digiflare.com on 14-12-01
 */
public class ApplicationGraph {

    private static ObjectGraph mObjectGraph;

    public static ObjectGraph getObjectGraph() {
        if (mObjectGraph == null) {
            throw new IllegalStateException("ObjectGraph has not been set");
        }
        return mObjectGraph;
    }

    public static void setObjectGraph(ObjectGraph objectGraph) {
        mObjectGraph = objectGraph;
    }

}
