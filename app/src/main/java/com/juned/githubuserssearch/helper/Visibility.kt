package com.juned.githubuserssearch.helper

import android.view.View

fun visibility(visible: Boolean): Int {
    return if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}
