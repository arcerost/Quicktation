package com.onurdemirbas.quicktation.repository

import com.onurdemirbas.quicktation.model.*
import com.onurdemirbas.quicktation.service.ApiService
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class QuicktationRepository @Inject constructor(private val api: ApiService) {
    suspend fun postRegisterApi(email: String, password: String, namesurname: String, username:String): Resource<RegisterResponse>{
        val request = Register(email, password, namesurname, username)
        return try {
            val response = api.postRegisterApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postLoginApi(email: String, password: String): Resource<LoginResponse> {
        val request = Login(email, password)
        return try {
            val response = api.postLoginApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postForgotPwApi(email: String): Resource<ForgotPasswordResponse> {
        val request = ForgotPassword(email)
        return try {
            val response = api.postForgotApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postCheckApi(email: String, code: String): Resource<CheckAuthPwResponse> {
        val request = CheckAuthPw(email,code)
        return try {
            val response = api.postCheckApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postUpdateApi(email: String, newpassword: String): Resource<UpdatePwResponse> {
        val request = UpdatePw(email,newpassword)
        return try {
            val response = api.postUpdateApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postMainApi(userid: Int, scanIndex: Int = 0): Resource<HomeResponse> {
        val request = Home(userid, scanIndex)
        return try {
            val response = api.postMainApi(request)
            when (response.error) {
                0 -> Resource.Success(response)
                1 -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postLikeApi(userId: Int,quoteId: Int): Resource<LikeResponse> {
        val request = Like(userId,quoteId)
        return try {
            val response = api.postLikeApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postQuoteDetailApi(userId: Int, quoteId: Int): Resource<QuoteDetailResponse> {
        val request = QuoteDetail(userId,quoteId,0)
        return try {
            val response = api.postQuoteDetailApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postQuoteDetailScanApi(userId: Int, quoteId: Int, scanIndex: Int): Resource<QuoteDetailResponse> {
        val request = QuoteDetail(userId,quoteId,scanIndex)
        return try {
            val response = api.postQuoteDetailScanApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postMyProfileApi(userId: Int, myUserId: Int): Resource<MyProfileResponse> {
        val request = ProfileDetail(userId,myUserId,0)
        return try {
            val response = api.postMyProfileApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postMyProfileScanApi(userId: Int, myUserId: Int, scanIndex: Int): Resource<MyProfileResponse> {
        val request = ProfileDetail(userId,myUserId,0)
        return try {
            val response = api.postMyProfileScanApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postLikeSoundApi(userId: Int, quotesound_id: Int): Resource<LikeSoundResponse> {
        val request = LikeQuoteSound(userId,quotesound_id)
        return try {
            val response = api.postLikeSoundApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postFollowerApi(userId: Int, toUserId: Int, action: String): Resource<FollowResponse> {
        val request = Follower(userId,toUserId,action)
        return try {
            val response = api.postFollowerApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postEditProfileApi(userId: Int,namesurname: String, userPhoto: String?, username: String?): Resource<EditProfileResponse> {
        val request = EditProfile(userId,username,userPhoto!!,namesurname)
        return try {
            val response = api.postEditProfileApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postReportUserApi(userId: Int, toUserId: Int, reason: String): Resource<ReportUserResponse> {
        val request = ReportUser(userId, toUserId, reason)
        return try {
            val response = api.postReportUserApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postDeleteQuoteApi(userId: Int, quoteId: Int): Resource<DeleteQuoteResponse> {
        val request = DeleteQuote(userId, quoteId)
        return try {
            val response = api.postDeleteQuoteApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postFollowUnfollowUserApi(userId: Int, toUserId: Int): Resource<FollowUnFollowUserResponse> {
        val request = FollowUnFollowUser(userId, toUserId)
        return try {
            val response = api.postFollowUnfollowUserApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postCreateQuoteSoundApi(userId: Int, quoteSound: String, quoteId: Int): Resource<CreateQuoteSoundResponse> {
        val request = CreateQuoteSound(userId, quoteSound,quoteId)
        return try {
            val response = api.postCreateQuoteSoundApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postCreateQuoteApi(userId: Int, quote_sound: String, quote_text: String): Resource<CreateQuoteResponse> {
        val request = CreateQuote(userId, quote_sound,quote_text)
        return try {
            val response = api.postCreateQuoteApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postSearchApi(userId: Int, action: String, searchKey: String, scanIndex: Int): Resource<SearchResponse> {
        val request = Search(userId, action, searchKey, scanIndex)
        return try {
            val response = api.postSearchApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun postSearchQuoteApi(userId: Int, action: String, searchKey: String, scanIndex: Int): Resource<SearchQuoteResponse> {
        val request = Search(userId, action, searchKey, scanIndex)
        return try {
            val response = api.postSearchQuoteApi(request)
            when (response.error) {
                "0" -> Resource.Success(response)
                "1" -> Resource.Error(response.errorText)
                else -> Resource.Error("Beklenmeyen bir hata oluştu.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun getNotificationsList(error:String): Resource<ArrayList<NotificationsResponse>> {
        val response = try{
            api.postNotificationApi(Notifications(error))
        }catch (e:Exception) {
            return Resource.Error("error")
        }
        return Resource.Success(response)
    }
}