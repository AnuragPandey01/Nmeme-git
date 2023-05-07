package com.example.n_meme.di

import android.content.Context
import androidx.room.Room
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.commons.BASE_URL
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.FavDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
}