package com.i.player.config

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(context: Context) : Interceptor {
//    private val sessionManager = SessionPreferences(context)
//    private val session = sessionManager.getSession()
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        // If token has been saved, add it to the request
//        session.isLogin.let {isLogin->
//            if(isLogin){
//                requestBuilder.addHeader("x-client-platform", "Android")
//                requestBuilder.addHeader("Content-Type", "application/json;charset=utf-8")
//                requestBuilder.addHeader("Accept", "application/json")
//                requestBuilder.addHeader("Authorization", "Bearer ${session.bearerToken}")
//                requestBuilder.addHeader("x-client-id", "${session.clientId}")
//            }
//        }

        return chain.proceed(requestBuilder.build())
    }
}