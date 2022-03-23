package com.android.cedecsi.util.launcher

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class IntentLauncher<T, S> {

    var result: (LauncherResult<T?, S>) -> Unit = {}

    private val context: AppCompatActivity
    private val permission: String
    private val permissions: Array<String>

    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionsLauncher: ActivityResultLauncher<Array<String>>? = null
    private val launcher: ActivityResultLauncher<T>
    private var param: T?= null

    constructor(context: AppCompatActivity, contract: ActivityResultContract<T, S>) {
        this.context = context
        this.permission = ""
        this.permissions = arrayOf()
        this.launcher = context.registerForActivityResult(contract) {
            result(LauncherResult.Success(param, it))
        }
    }

    constructor(context: AppCompatActivity, permission: String, contract: ActivityResultContract<T, S>) {
        this.context = context
        this.permission = permission
        this.permissions = arrayOf()
        this.launcher = context.registerForActivityResult(contract) {
            result(LauncherResult.Success(param, it))
        }
        permissionLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it)
                executeLauncher(param)
            else {
                result(LauncherResult.Error(
                    "Permiso denegado", "Debe conceder permisos para continuar"
                ))
            }
        }
    }

    constructor(context: AppCompatActivity, permissions: Array<String>, contract: ActivityResultContract<T, S>) {
        this.context = context
        this.permission = ""
        this.permissions = permissions
        this.launcher = context.registerForActivityResult(contract) {
            result(LauncherResult.Success(param, it))
        }
        permissionsLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (permissions.map { result[it] }.reduce { acc, b -> acc == true && b == true } == true)
                executeLauncher(param)
            else {
                result(LauncherResult.Error(
                    "Permiso denegado", "Debe conceder permisos para continuar"
                ))
            }
        }
    }

    private fun checkCameraPermissions(): Boolean {
        return when {
            permission.isNotEmpty() -> {
                ContextCompat
                    .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
            permissions.isNotEmpty() -> {
                permissions.map {
                    ContextCompat
                        .checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }.reduce { acc, b -> acc && b }
            }
            else -> true
        }
    }

    fun executeLauncher(param: T?) {
        this.param = param
        if (checkCameraPermissions()) {
            launcher.launch(param)
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        when {
            permission.isNotEmpty() -> permissionLauncher?.launch(permission)
            permissions.isNotEmpty() -> permissionsLauncher?.launch(permissions)
        }
    }

}