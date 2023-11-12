package com.example.bookcourt.data.api

import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.BookRetrofit
import com.example.bookcourt.models.book.MarkBook
import com.example.bookcourt.models.feedback.PostReviewRetrofit
import com.example.bookcourt.models.feedback.ReviewRetrofit
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BooksApi {
    @GET("4N4X")
    suspend fun fetchBooks():List<BookDto>

    //---------------------RECOMMENDATION--------------------------------------

    @GET("tinder/books")
    fun getRecommendationBooks(@Query("countBooks") countBooks:Int):Call<List<BookRetrofit>>

    @POST("tinder/book/mark")
    suspend fun postMarkBook(@Body markBook: MarkBook)

    //---------------------BOOK-CARD-----------------------------------------------
    @GET("book") // book
    fun getBookById(@Query("bookId") bookId:String): Call<BookRetrofit>

    //---------------------REVIEWS-----------------------------------------------

    @GET("book/review")
    fun getReviews(@Query("bookId") bookId: String):Call<List<ReviewRetrofit>>

    @POST("book/review")
    suspend fun postReview(@Body postReviewRetrofit: PostReviewRetrofit)




}