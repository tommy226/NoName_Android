package com.sungbin.noname.network

import com.sungbin.noname.detail.data.DetailResponse
import com.sungbin.noname.home.data.FeedPagingResponse
import com.sungbin.noname.home.data.GetProfileImageResponse
import com.sungbin.noname.home.data.MemberResponse
import com.sungbin.noname.home.data.SubscribeRequest
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
import com.sungbin.noname.upload.data.BoardsImageResponse
import okhttp3.MultipartBody
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
    ) : Call<BoardsImageResponse>

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

    @GET("files/members/{memberId}")  // 유저 이미지 가져오기
    fun getProfileImage(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("memberId") memberId: Int
    ) : Call<GetProfileImageResponse>

    @GET("members/{id}") // 유저 정보 가져오기
    fun getInfo(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") memberId: Int
    ) : Call<MemberResponse>

    @GET("boards") // 보드 페이징
    fun getBoards(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("page") page: Int,
    ): Call<FeedPagingResponse>

    @GET("boards/users/{memberId}")  // 유저가 올린 게시물 확인 (프로필 게시글)
    fun getUserBoards(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("memberId") memberId: Int,
        @Query("page") page: Int
    ): Call<FeedPagingResponse>

    @GET("boards/{id}") // 보드 상세 정보
    fun getBoard(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") boardId: Int
    ): Call<DetailResponse>

    @POST("subscribes/")            // 팔로우 or 좋아요 (ownerId or fallowId)
    fun subscribe(
        @Header("X-AUTH-TOKEN") token: String,
        @Body subscribeDTO: SubscribeRequest
    ): Call<ResponseBody>

    @DELETE("subscribes/{id}")      // 언팔로우 or 좋아요 취소
    fun unSubscribe(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") subscribeId: Int
    ): Call<ResponseBody>

    @GET("subscribes/fallow/{ownerId}")
    fun getSubscribePageByOwenerId(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("ownerId") owenerId: Int
    ): Call<ResponseBody>

    @GET("subscribes/like/{boardId}")
    fun getSubscribePageByBoardId(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("boardId") boardId: Int
    ): Call<ResponseBody>

}