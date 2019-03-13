package com.cniekirk.kreddit.core.extensions

// Just playing with infix notation, pretty nice
infix fun String.has(otherString: String): Boolean {
    return this.contains(otherString)
}

fun String.isImage(): Boolean {
    return this.endsWith(".png") || this.endsWith(".jpg") ||
            this.endsWith(".jpeg") || this.endsWith(".gif") || this.endsWith(".gifv")
}

fun String.isGif(): Boolean {
    return this.endsWith(".gif") || this.endsWith(".gifv")
}

fun String.isImgur(): Boolean {
    return this.contains("imgur")
}