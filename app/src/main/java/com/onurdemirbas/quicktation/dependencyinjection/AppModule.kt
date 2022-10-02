package com.onurdemirbas.quicktation.dependencyinjection

import com.onurdemirbas.quicktation.repository.QuicktationRepo
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
    fun provideQuicktationRepository(api: RegisterApi, api2: LoginApi, api3: ForgotPwApi, api4: HomeApi, api5: NotificationsApi, api6: LikeApi, api7: QuoteDetailApi, api8: MyProfileApi) = QuicktationRepo(api,api2,api3,api4,api5,api6,api7,api8)


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
    fun provideQuicktationMainApi(): HomeApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(HomeApi::class.java)
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
    @Singleton
    @Provides
    fun provideQuicktationLikeApi(): LikeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(LikeApi::class.java)
    }
    @Singleton
    @Provides
    fun provideQuicktationQuoteDetailApi(): QuoteDetailApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(QuoteDetailApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationMyProfileApi(): MyProfileApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MyProfileApi::class.java)
    }
}