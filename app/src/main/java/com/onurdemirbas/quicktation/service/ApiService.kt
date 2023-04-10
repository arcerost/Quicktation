package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.*
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type:application/json")
    @POST("createQuote")
    suspend fun postCreateQuoteApi(@Body request: CreateQuote): CreateQuoteResponse

    @Headers("Content-Type:application/json")
    @POST("createQuoteSound")
    suspend fun postCreateQuoteSoundApi(@Body request: CreateQuoteSound): CreateQuoteSoundResponse

    @Headers("Content-Type:application/json")
    @POST("deleteQuote")
    suspend fun postDeleteQuoteApi(@Body request: DeleteQuote): DeleteQuoteResponse

    @Headers("Content-Type:application/json")
    @POST("editprofile")
    suspend fun postEditProfileApi(@Body request: EditProfile): EditProfileResponse

    @Headers("Content-Type:application/json")
    @POST("userfollows")
    suspend fun postFollowerApi(@Body request: Follower): FollowResponse

    @Headers("Content-Type:application/json")
    @POST("followUnfollowUser")
    suspend fun postFollowUnfollowUserApi(@Body request: FollowUnFollowUser): FollowUnFollowUserResponse

    @Headers("Content-Type:application/json")
    @POST("forgotpassword")
    suspend fun postForgotApi(@Body request: ForgotPassword): ForgotPasswordResponse

    @Headers("Content-Type:application/json")
    @POST("checkauthcode")
    suspend fun postCheckApi(@Body request: CheckAuthPw): CheckAuthPwResponse

    @Headers("Content-Type:application/json")
    @POST("updateuserpassword")
    suspend fun postUpdateApi(@Body request: UpdatePw): UpdatePwResponse

    @Headers("Content-Type:application/json")
    @POST("homepageitems")
    suspend fun postMainApi(@Body request: Home): HomeResponse

    @Headers("Content-Type:application/json")
    @POST("search")
    suspend fun postSearchApi(@Body request: Search): SearchResponse //user

    @Headers("Content-Type:application/json")
    @POST("search")
    suspend fun postSearchQuoteApi(@Body request: Search): SearchQuoteResponse //quote

    @Headers("Content-Type:application/json")
    @POST("")
    suspend fun postInMessageApi(@Body request: Home) : HomeResponse //InmessageResponse

    @Headers("Content-Type:application/json")
    @POST("likequote")
    suspend fun postLikeApi(@Body request: Like): LikeResponse

    @Headers("Content-Type:application/json")
    @POST("likequotesound")
    suspend fun postLikeSoundApi(@Body request: LikeQuoteSound): LikeSoundResponse

    @Headers("Content-Type:application/json")
    @POST("login")
    suspend fun postLoginApi(@Body request: Login): LoginResponse

    @Headers("Content-Type:application/json")
    @POST("profiledetail")
    suspend fun postMyProfileApi(@Body request: ProfileDetail) : MyProfileResponse

    @Headers("Content-Type:application/json")
    @POST("profiledetail")
    suspend fun postMyProfileScanApi(@Body request: ProfileDetail) : MyProfileResponse

    @Headers("Content-Type:application/json")
    @POST("notification")
    suspend fun postNotificationApi(@Body request: Notifications): ArrayList<NotificationsResponse>

    @Headers("Content-Type:application/json")
    @POST("quotationdetail")
    suspend fun postQuoteDetailApi(@Body request: QuoteDetail): QuoteDetailResponse

    @Headers("Content-Type:application/json")
    @POST("quotationdetail")
    suspend fun postQuoteDetailScanApi(@Body request: QuoteDetail): QuoteDetailResponse

    @Headers("Content-Type:application/json")
    @POST("register")
    suspend fun postRegisterApi(@Body request: Register): RegisterResponse

    @Headers("Content-Type:application/json")
    @POST("reportUser")
    suspend fun postReportUserApi(@Body request: ReportUser): ReportUserResponse
}