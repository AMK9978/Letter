package com.fidea.letter.di

import com.fidea.letter.AuthActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [AuthActivity::class])
    abstract fun contributeAuthActivity(): AuthActivity

    companion object {
        @Provides
        fun printLog(): String {
            return "Hello!"
        }
        @Provides
        fun getMsg(): String {
            return "SALAM"
        }
    }
}