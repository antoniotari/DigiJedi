package com.antoniotari.android.injection;

import com.antoniotari.android.networking.QueueManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by antonio on 11/06/15.
 */

@Singleton
@Component(modules = {JediModule.class})
public interface JediComponent {
    /* Functionally equivalent to objectGraph.get(Foo.class). */
    QueueManager getQueueManager();
    /* Functionally equivalent to objectGraph.inject(aBaz). */
    QueueManager injectQueueManager(QueueManager baz);
}