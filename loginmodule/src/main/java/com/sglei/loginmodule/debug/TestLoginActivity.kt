package com.sglei.loginmodule.debug

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class TestLoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "登录"
        setContentView(textView)
    }
}