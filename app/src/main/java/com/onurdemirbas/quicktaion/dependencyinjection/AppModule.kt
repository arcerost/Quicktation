package com.onurdemirbas.quicktaion.dependencyinjection

import com.onurdemirbas.quicktaion.repository.QuicktationRepo
import com.onurdemirbas.quicktaion.service.LoginApi
import com.onurdemirbas.quicktaion.service.RegisterApi
import com.onurdemirbas.quicktaion.util.Constants.BASE_URL
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
    fun provideQuicktationRepository(api: RegisterApi, api2: LoginApi) = QuicktationRepo(api,api2)


    @Singleton
    @Provides
    fun provideQuicktationApi(): RegisterApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RegisterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationLoginApi(): LoginApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(LoginApi::class.java)
    }

}