package com.sglei.sortmodule.debug

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class TestSortActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "书城"
        setContentView(textView)
    }
}