package com.example.pixabaytest.utils.extensions

import android.content.Context
import android.widget.Toast

fun Context.toast(txt: String) = Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()