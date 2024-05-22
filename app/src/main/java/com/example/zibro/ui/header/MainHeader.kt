package com.example.zibro.ui.header

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.zibro.R
import com.example.zibro.databinding.ViewHeaderMainBinding

class MainHeader : LinearLayout {
    private lateinit var binding: ViewHeaderMainBinding

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context)
        getAttrs(attrs, defStyle)
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewHeaderMainBinding.inflate(inflater, this, true)
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.root.layoutParams = lp
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderMain)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderMain, defStyle, 0)
        setTypeArray(typedArray)
    }
    private fun setTypeArray(typedArray: TypedArray) {
        val menuIcon1 = typedArray.getResourceId(R.styleable.HeaderMain_mainMenu1Icon, R.drawable.header_setting_black)
        val menuIcon2 = typedArray.getResourceId(R.styleable.HeaderMain_mainMenu2Icon, R.drawable.notice_black)
        val menuIcon3 = typedArray.getResourceId(R.styleable.HeaderMain_mainMenu3Icon, R.drawable.search_main)
        val menutext = typedArray.getString(R.styleable.HeaderMain_mainMenuText)
        val menutextColor = typedArray.getColor(R.styleable.HeaderMain_mainMenuTextColor, Color.BLACK)


        binding.imgSplashMenu1.apply {
                menuIcon1 != R.drawable.header_setting_black
                setImageResource(menuIcon1)
                visibility = View.VISIBLE

        }

        binding.imgSplashMenu2.apply {
                menuIcon2 != R.drawable.notice_black
                setImageResource(menuIcon2)
                visibility = View.VISIBLE

        }

        binding.imgSplashMenu3.apply {
                if(menuIcon3 == R.drawable.search_main) {
                    setImageResource(menuIcon3)
                    visibility = View.VISIBLE
                } else {
                    visibility = View.INVISIBLE
                }
        }
        binding.headerMainText.apply{
            text = menutext.toString()
            setTextColor(menutextColor)
        }

        typedArray.recycle()
    }

    fun setMenu1IconClickListener(listener: (View) -> Unit) {
        binding.imgSplashMenu1.setOnClickListener { listener(it) }
    }

    fun setMenu2IconClickListener(listener: (View) -> Unit) {
        binding.imgSplashMenu2.setOnClickListener { listener(it) }
    }

    fun setMenu3IconClickListener(listener: (View) -> Unit) {
        binding.imgSplashMenu3.setOnClickListener { listener(it) }
    }
}
