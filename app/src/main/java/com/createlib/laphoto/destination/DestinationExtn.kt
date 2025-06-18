package com.createlib.laphoto.destination

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController.Companion.KEY_DEEP_LINK_INTENT
import com.createlib.laphoto.utils.Constants.NAVIGATION_AUTHORITY
import com.createlib.laphoto.utils.Constants.NAVIGATION_SCHEME
import com.createlib.laphoto.utils.ReflectionUtilities.getValueFrom
import kotlin.reflect.full.primaryConstructor

/**
 * Function to get instance of [destination] from [SavedStateHandle]
 *
 * What It actually does is using reflection, call primary constructor to get the instance of
 * the [destination] by also providing it with parameters which we will get the value from the
 * given [SavedStateHandle]
 *
 * @return [destination] instance if reflection succeed and null if it's not
 * @receiver [SavedStateHandle]
 */
inline fun <reified destination : Any> SavedStateHandle.getArg(): destination? = try {
    destination::class.primaryConstructor?.callBy(
        destination::class.primaryConstructor?.parameters?.associate { parameter ->
            parameter to parameter.getValueFrom(this)
        }.orEmpty()
    )
} catch (t: Throwable) {
    null
}

fun SavedStateHandle.isFromDeeplink(): Boolean {
    val currentUri = get<Intent>(KEY_DEEP_LINK_INTENT)?.data
    val nonNativeScheme = currentUri?.scheme.orEmpty() != NAVIGATION_SCHEME
    val nonNativeAuthority = currentUri?.authority.orEmpty() != NAVIGATION_AUTHORITY
    return nonNativeScheme && nonNativeAuthority
}

/**
 * Function to get instance of [destinationClass] from [NavBackStackEntry]
 *
 * @return instance of [destinationClass] from [NavBackStackEntry]'s [SavedStateHandle]
 * @receiver [NavBackStackEntry]
 */
inline fun <reified destinationClass : Any> NavBackStackEntry.getArg(): destinationClass? = try {
    destinationClass::class.primaryConstructor?.callBy(
        destinationClass::class.primaryConstructor?.parameters?.associate { parameter ->
            parameter to parameter.getValueFrom(this.arguments ?: Bundle())
        }.orEmpty()
    )
} catch (t: Throwable) {
    null
}

/**
 * Function to get does the destination opened from a Uri parsed deeplink or not
 *
 * @return [Boolean] true if navigation from uri-parsed deeplink, and false if not
 * @receiver [NavBackStackEntry]
 */
@Suppress("DEPRECATION")
fun NavBackStackEntry.isFromDeeplink(): Boolean {
    val currentUri = this.arguments?.getParcelable<Intent>(KEY_DEEP_LINK_INTENT)?.data
    val nonNativeScheme = currentUri?.scheme.orEmpty() != NAVIGATION_SCHEME
    val nonNativeAuthority = currentUri?.authority.orEmpty() != NAVIGATION_AUTHORITY
    return nonNativeScheme && nonNativeAuthority
}