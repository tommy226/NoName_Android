package com.sungbin.noname.network

import com.sungbin.noname.login.data.LoginRequest
import com.sungbin.noname.login.data.LoginResponse
import com.sungbin.noname.profile.data.ProfileEditRequest
import com.sungbin.noname.profile.data.ProfileEditResponse
import com.sungbin.noname.profile.data.ProfileImageResponse
import com.sungbin.noname.signup.data.AccountCheckRequest
import com.sungbin.noname.signup.data.AccountCheckResponse
import com.sungbin.noname.signup.data.RegisterRequest
import com.sungbin.noname.signup.data.RegisterResponse
import com.sungbin.noname.upload.data.BoardsContentRequest
import com.sungbin.noname.upload.data.BoardsContentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ServerService {
    @PUT("auth/") // 로그인
    fun login(@Body userInfo: LoginRequest): Call<LoginResponse>

    @POST("auth/authority") // 계정 중복체크
    fun accountDup(@Body account: AccountCheckRequest): Call<AccountCheckResponse>

    @POST("auth/") // 회원가입
    fun register(@Body registerInfo: RegisterRequest): Call<RegisterResponse>

    @POST("boards/") // 게시글 글 작성
    fun boardContentUpload(
        @Header("X-AUTH-TOKEN") token: String,
        @Body Board: BoardsContentRequest
    ): Call<BoardsContentResponse>

    @Multipart
    @POST("files/boards") // 게시글 이미지 업로드
    fun boardFileUpload(
        @Header("X-AUTH-TOKEN") token: String,
        @Part files: MutableList<MultipartBody.Part>,
        @Query("boardId") boardId: String
//        @PartMap data: HashMap<String, RequestBody>
    ) : Call<ResponseBody>

    @PUT("auth/{account}/") // 프로필 편집
    fun profileEdit(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("account") account: String,
        @Body profileInfo: ProfileEditRequest
    ) : Call<ProfileEditResponse>

    @Multipart
    @POST("files/members") // 프로필 이미지 편집
    fun profileImageUpload(
        @Header("X-AUTH-TOKEN") token: String,
        @Part image: MultipartBody.Part,
        @Query("account") account: String
//        @PartMap data: HashMap<String, RequestBody>
    ) : Call<ProfileImageResponse>

    @GET("files/members/{memberId}")
    fun profileImageGet(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("memberId") memberId: String
    ) : Call<ResponseBody>
}