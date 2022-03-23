package com.android.cedecsi.util.launcher

sealed class LauncherResult<T, S> {
    data class Success<T, S>( var data: T, var result: S): LauncherResult<T, S>()
    data class Error<T, S>(var title: String, var error: String): LauncherResult<T, S>()
}