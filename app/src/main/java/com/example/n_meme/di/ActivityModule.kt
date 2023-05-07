package com.example.n_meme.di

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @ActivityScoped
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @ActivityScoped
    @Provides
    fun provideMixPanelInstance(@ActivityContext appContext: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(appContext, "00e974be97380ba7274d26ea7e6fca24",true)
    }
}