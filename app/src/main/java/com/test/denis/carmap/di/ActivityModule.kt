package com.test.denis.carmap.di

import com.test.denis.carmap.ui.CarMapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeCarMapActivity(): CarMapActivity
}