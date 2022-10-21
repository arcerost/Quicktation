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
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuicktationRepository(api: RegisterApi, api2: LoginApi, api3: ForgotPwApi, api4: HomeApi, api5: NotificationsApi, api6: LikeApi, api7: QuoteDetailApi, api8: MyProfileApi, api9: LikeSoundApi, api10: FollowerApi, api11: EditProfileApi, api12: ReportUserApi, api13: DeleteQuoteApi, api14: FollowUnfollowUserApi, api15: CreateQuoteSoundApi, api16: CreateQuoteApi) = QuicktationRepo(api,api2,api3,api4,api5,api6,api7,api8,api9,api10,api11,api12,api13,api14,api15, api16)


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

    @Singleton
    @Provides
    fun provideQuicktationLikeSoundApi(): LikeSoundApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(LikeSoundApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationFollowerApi(): FollowerApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(FollowerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationEditProfileApi(): EditProfileApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(EditProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationReportUserApi(): ReportUserApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ReportUserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationDeleteQuoteApi(): DeleteQuoteApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(DeleteQuoteApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationFollowUnfollowUserApi(): FollowUnfollowUserApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(FollowUnfollowUserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationCreateQuoteSoundApi(): CreateQuoteSoundApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CreateQuoteSoundApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuicktationCreateQuoteApi(): CreateQuoteApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CreateQuoteApi::class.java)
    }
}