package com.fithun.di

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import com.fithun.api.ApiInterface
import com.fithun.api.Constants.BASE_URL
import com.fithun.ui.activities.loginFlow.LoginActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.Properties
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DreamWalkModule {




    @Provides
    @Singleton
    fun provideMyApi(@ApplicationContext context: Context): ApiInterface {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()

            .callTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                val responseBody = response.body.string()
                try {
                    val json = JSONObject(responseBody)
                    val message = json.getString("responseMessage")
                    val code = json.getString("responseCode")

                    if (code.toInt() == 440 || code.toInt() == 450 || code.toInt() == 460 || code.toInt() == 401) {
                        redirectToLoginScreen(context, message)
                    }




                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                response.newBuilder()
                    .body(responseBody.toResponseBody(response.body.contentType()))
                    .build()
            }
            .build()

        val retrofitInstance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiInterface::class.java)
        }
        return retrofitInstance
    }


    private fun redirectToLoginScreen(context: Context, message: String) {
        val mainHandler = Handler(context.mainLooper)
        mainHandler.post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }



}