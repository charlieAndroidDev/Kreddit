package com.cniekirk.kreddit.core.extensions

// Just nicer and more readable
infix fun String.contains(other: String): Boolean {
    return this.contains(other)
}