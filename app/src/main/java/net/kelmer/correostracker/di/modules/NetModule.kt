package net.kelmer.correostracker.di.modules

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.di.qualifiers.NetworkLogger
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.NetworkInteractorImpl
import net.kelmer.correostracker.util.adapter.CorreosApiParcelAdapter
import net.kelmer.correostracker.util.adapter.SingleToArray
import net.kelmer.correostracker.util.adapter.SingleToArrayAdapter
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module(
        includes = [Interceptors::class]
)
@InstallIn(ApplicationComponent::class)
open class NetModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, @NetworkLogger loggingInterceptors: Set<@JvmSuppressWildcards
    Interceptor>): OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .apply {
                loggingInterceptors.forEach {
                    addNetworkInterceptor(it)
                }
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
                        gsonConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://localizador.correos.es/canonico/")
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
            Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
//            .add(SingleToArrayAdapter.FACTORY)
                    .add(CorreosApiParcelAdapter.FACTORY)
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .build()

    @Provides
    @Singleton
    fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024L
        return Cache(application.getCacheDir(), cacheSize)
    }


}