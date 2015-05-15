package com.antoniotari.android.injection;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @see <a href="https://github.com/square/dagger/blob/master/examples/android-activity-graphs/src/main/java/com/example/dagger/activitygraphs/ForActivity.java">Modified from Dagger Sample Application</a>
 */
@Qualifier @Retention (RUNTIME)
public @interface ForActivity {
}
