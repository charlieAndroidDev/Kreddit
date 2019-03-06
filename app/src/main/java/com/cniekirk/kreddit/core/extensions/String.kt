package com.cniekirk.kreddit.core.extensions

// Just playing with infix notation, pretty nice
infix fun String.contains(otherString: String): Boolean {
    return this.contains(otherString)
}

fun String.isImage(): Boolean {
    return this.endsWith(".png") || this.endsWith(".jpg") || this.endsWith(".jpeg")
}