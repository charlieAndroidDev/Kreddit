package com.cniekirk.kreddit.core.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Extension function to make Fragment transactions more expressive
 */
internal inline fun FragmentManager.inTransaction(func:
FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}