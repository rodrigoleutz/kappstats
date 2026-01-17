package com.kappstats.presentation.util

actual fun exitApplication() {
    val window = kotlinx.browser.window
    window.close()
    if (!window.closed) {
        window.location.href = "about:blank"
    }
}