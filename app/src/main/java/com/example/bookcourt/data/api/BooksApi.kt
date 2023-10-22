package com.example.bookcourt.data.api

import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.BookRetrofit
import com.example.bookcourt.models.book.MarkBook
import com.example.bookcourt.models.feedback.PostReviewRetrofit
import com.example.bookcourt.models.feedback.ReviewRetrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BooksApi {
    @GET("4N4X")
    suspend fun fetchBooks():List<BookDto>

    //---------------------RECOMMENDATION--------------------------------------

    @GET("tinder/books")
    suspend fun getRecommendationBooks(@Body countBooks:Int):List<BookRetrofit>

    @POST("tinder/book/mark")
    suspend fun postMarkBook(@Body markBook: MarkBook)

    //---------------------BOOK-CARD-----------------------------------------------
    @POST("testMetric.json") // book
    suspend fun getBookById(@Body bookId:String):BookRetrofit

    //---------------------REVIEWS-----------------------------------------------

    @GET("book/review")
    suspend fun getReviews(@Body bookId:String):List<ReviewRetrofit>

    @POST("book/review")
    suspend fun postReview(@Body postReviewRetrofit: PostReviewRetrofit)




}