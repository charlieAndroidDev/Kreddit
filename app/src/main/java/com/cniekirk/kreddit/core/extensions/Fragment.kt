package com.cniekirk.kreddit.core.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

internal inline fun FragmentManager.inTransaction(func:
FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}