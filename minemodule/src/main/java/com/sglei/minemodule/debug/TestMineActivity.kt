package com.sglei.minemodule.debug

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class TestMineActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "个人中心"
        setContentView(textView)
    }
}