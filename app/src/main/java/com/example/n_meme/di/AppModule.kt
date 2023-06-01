package com.example.n_meme.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.commons.BASE_URL
import com.example.n_meme.commons.DATA_PREF_NAME
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.FavDataBase
import com.example.n_meme.data.local.PreferenceManager
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesMemeApi(retrofit: Retrofit): MemeApiService {
        return retrofit.create(MemeApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesFavDatabaseInstance(@ApplicationContext context: Context): FavDao {
        return Room.databaseBuilder(context, FavDataBase::class.java, "favDB").build().favDao()
    }

    @Singleton
    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(DATA_PREF_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providesPreferenceManager(sharedPreferences: SharedPreferences): PreferenceManager {
        return PreferenceManager(sharedPreferences)
    }

    @Singleton
    @Provides
    fun providesMixPanelInstance(@ApplicationContext context: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(context, "00e974be97380ba7274d26ea7e6fca24",true)
    }
}