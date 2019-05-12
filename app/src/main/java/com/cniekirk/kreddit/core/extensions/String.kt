package com.cniekirk.kreddit.core.extensions

// Just playing with infix notation, pretty nice
infix fun String.has(otherString: String): Boolean {
    return this.contains(otherString)
}

/**
 * Extension function to determine if web address is to an image
 */
fun String.isImage(): Boolean {
    return this.endsWith(".png") || this.endsWith(".jpg") ||
            this.endsWith(".jpeg") || this.endsWith(".gif") || this.endsWith(".gifv")
}

/**
 * Extension function to determine if web address is to a gif
 */
fun String.isGif(): Boolean {
    return this.endsWith(".gif") || this.endsWith(".gifv")
}

/**
 * Extension function to determine if web address is to an Imgur image
 */
fun String.isImgur(): Boolean {
    return this.contains("imgur")
}