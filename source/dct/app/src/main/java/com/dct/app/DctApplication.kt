package com.dct.app

import android.app.Application
import com.dct.app.di.AppContainer

class DctApplication : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
