package com.onurdemirbas.quicktaion.dependencyinjection

import com.onurdemirbas.quicktaion.model.Main
import com.onurdemirbas.quicktaion.repository.QuicktationRepo
import com.onurdemirbas.quicktaion.service.*
import com.onurdemirbas.quicktaion.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuicktationRepository(api: RegisterApi, api2: LoginApi, api3: ForgotPwApi, api4: MainApi, api5: NotificationsApi) = QuicktationRepo(api,api2,api3,api4,api5)


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

    @Singleton
    @Provides
    fun provideQuicktationForgotPasswordApi(): ForgotPwApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ForgotPwApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationMainApi(): MainApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MainApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationNotificationsApi(): NotificationsApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(NotificationsApi::class.java)
    }
}