package com.antoniotari.android.injection;

/**
 * @see <a href="https://github.com/square/dagger/blob/master/examples/android-activity-graphs/src/main/java/com/example/dagger/activitygraphs/ForApplication.java">Modified from Dagger Sample Application</a>
 */

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention (RUNTIME)
public @interface ForApplication {
}
