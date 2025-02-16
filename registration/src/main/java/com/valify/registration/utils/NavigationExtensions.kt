package com.valify.registration.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment


fun Fragment.navigationSafe(navDirections: NavDirections) {
    val navDestination = NavHostFragment.findNavController(this).currentDestination
    if (navDestination?.getAction(navDirections.actionId) != null && navDestination.id != navDirections.actionId) NavHostFragment.findNavController(
        this
    ).navigate(navDirections)
}