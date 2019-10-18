package com.test.denis.carmap.di

import com.test.denis.carmap.ui.CarsMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCarsMapFragment(): CarsMapFragment
}