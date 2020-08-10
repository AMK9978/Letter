package com.fidea.letter.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    companion object {
        @Provides
        fun getApp(application: Application?): Boolean {
            return application == null
        }

        @Provides
        fun getMsg(): String {
            return "SALAM"
        }
    }
}