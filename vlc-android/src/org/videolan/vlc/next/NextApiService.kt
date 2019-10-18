/*
 * ************************************************************************
 *  NextApiService.kt
 * *************************************************************************
 * Copyright © 2019 VLC authors and VideoLAN
 * Author: Nicolas POMEPUY
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 * **************************************************************************
 *
 *
 */

package org.videolan.vlc.next

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.videolan.vlc.VLCApplication
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

private const val USER_AGENT = "VLSub 0.9"

private fun buildClient() =
        Retrofit.Builder()
                .baseUrl(VLCApplication.nextApiUrl)
                .client(OkHttpClient.Builder()
                        .addInterceptor(UserAgentInterceptor(USER_AGENT))
                        .addInterceptor(ConnectivityInterceptor())
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe()).build()))
                .build()
                .create(INextApiService::class.java)

private class UserAgentInterceptor(val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val userAgentRequest: Request = request.newBuilder().header("User-Agent", userAgent).build()
        return chain.proceed(userAgentRequest)
    }
}

private class ConnectivityInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //if (!ExternalMonitor.isConnected) throw NoConnectivityException()

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NoConnectivityException : IOException() {

    override val message: String?
        get() = "No connectivity exception"
}

interface NextApiClient {

    companion object {
        val instance: INextApiService by lazy { buildClient() }
    }
}
