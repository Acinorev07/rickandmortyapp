package com.example.rickandmortyapp.util

import android.content.Context
import androidx.annotation.StringRes

sealed class RUiText{
    data class DynamicString(val value: String): RUiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ):RUiText()

    fun asString(context: Context): String = when (this){
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
