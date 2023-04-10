package com.onurdemirbas.quicktation.dependencyinjection

import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.service.*
import com.onurdemirbas.quicktation.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuicktationRepository(api: ApiService) = QuicktationRepository(api)


    @Singleton
    @Provides
    fun provideQuicktationApi(): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }
}