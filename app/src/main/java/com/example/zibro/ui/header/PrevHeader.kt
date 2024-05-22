package com.example.zubro.ui.header

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.zibro.databinding.ViewHeaderPrevBinding

class PrevHeader : LinearLayout {
    lateinit var view: ViewHeaderPrevBinding

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(context)
    }

    fun setNavigationIcon(iconRes: Int) {
        view.prev.setImageResource(iconRes)
    }

    fun setNavigationClickListener(listener: (icon: View) -> Unit) {
        view.prev.setOnClickListener(listener)
    }


    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = ViewHeaderPrevBinding.inflate(inflater)

        val lp =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.root.layoutParams = lp

        addView(view.root)
    }



}