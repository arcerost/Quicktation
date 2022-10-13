package com.onurdemirbas.quicktation.repository

import com.onurdemirbas.quicktation.model.*
import com.onurdemirbas.quicktation.service.*
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.Exception

@ActivityScoped
class QuicktationRepo @Inject constructor(private val api: RegisterApi, private val api2: LoginApi, private val api3: ForgotPwApi, private val api4: HomeApi, private val api5: NotificationsApi, private val api6: LikeApi, private val api7: QuoteDetailApi, private val api8: MyProfileApi, private val api9: LikeSoundApi, private val api10: FollowerApi, private val api11: EditProfileApi, private val api12: ReportUserApi, private val api13: DeleteQuoteApi, private val api14: FollowUnfollowUserApi) {
    private lateinit var hatametni: String
    private lateinit var loginError: String
    suspend fun postRegisterApi(email: String, password: String, namesurname: String): Resource<RegisterResponse> {
        val request = Register(email, password, namesurname)
        val response = api.postRegisterApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    hatametni = response.errorText
                    return Resource.Error(hatametni)
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postLoginApi(email: String, password: String): Resource<LoginResponse> {
        val request = Login(email, password)
        val response = api2.postLoginApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postForgotPwApi(email: String): Resource<ForgotPasswordResponse> {
        val request = ForgotPassword(email)
        val response = api3.postForgotApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postCheckApi(email: String, code: String): Resource<CheckAuthPwResponse> {
        val request = CheckAuthPw(email,code)
        val response = api3.postCheckApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
    suspend fun postUpdateApi(email: String, newpassword: String): Resource<UpdatePwResponse> {
        val request = UpdatePw(email,newpassword)
        val response = api3.postUpdateApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    suspend fun postMainApi(userid: Int): Resource<HomeResponse> {

        val request = Home(userid,0)
        val response = api4.postMainApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postLikeApi(userId: Int,quoteId: Int): Resource<LikeResponse>
    {
        val request = Like(userId,quoteId)
        val response = api6.postLikeApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    suspend fun postMainScanApi(userid: Int, scanIndex: Int): Resource<HomeResponse> {

        val request = Home(userid,scanIndex)
        val response = api4.postMainScanApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postQuoteDetailApi(userId: Int, quoteId: Int): Resource<QuoteDetailResponse> {

        val request = QuoteDetail(userId,quoteId)
        val response = api7.postQuoteDetailApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postMyProfileApi(userId: Int, myUserId: Int): Resource<MyProfileResponse> {

        val request = ProfileDetail(userId,myUserId,0)
        val response = api8.postMyProfileApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    suspend fun postLikeSoundApi(userId: Int, quotesound_id: Int): Resource<LikeSoundResponse> {

        val request = LikeQuoteSound(userId,quotesound_id)
        val response = api9.postLikeSoundApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    suspend fun postFollowerApi(userId: Int, toUserId: Int, action: String): Resource<FollowResponse> {

        val request = Follower(userId,toUserId,action)
        val response = api10.postFollowerApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postEditProfileApi(userId: Int,namesurname: String, userPhoto: String?): Resource<EditProfileResponse>
    {
        val request = EditProfile(userId,namesurname, userPhoto!!)
        val response = api11.postEditProfileApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    suspend fun postReportUserApi(userId: Int, toUserId: Int, reason: String): Resource<ReportUserResponse> {

        val request = ReportUser(userId, toUserId, reason)
        val response = api12.postReportUserApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postDeleteQuoteApi(userId: Int, quoteId: Int): Resource<DeleteQuoteResponse> {

        val request = DeleteQuote(userId, quoteId)
        val response = api13.postDeleteQuoteApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postFollowUnfollowUserApi(userId: Int, toUserId: Int): Resource<FollowUnFollowUserResponse> {

        val request = FollowUnFollowUser(userId, toUserId)
        val response = api14.postFollowUnfollowUserApi(request)
        try {
            when (response.error) {
                "0" -> {
                }
                "1" -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


    //yöntem 2
    suspend fun getNotificationsList(error:String): Resource<ArrayList<NotificationsResponse>>
    {
        val response = try{
            api5.postNotificationApi(Notifications(error))
        }catch (e:Exception) {
            return Resource.Error("error")
        }
        return Resource.Success(response)
    }
}
