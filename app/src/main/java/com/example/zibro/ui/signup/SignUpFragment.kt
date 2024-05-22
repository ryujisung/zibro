package com.example.zibro.ui.signup

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.example.zibro.R
import com.example.zibro.ui.base.BaseFragment

abstract class SignUpFragment<D : ViewDataBinding>(
    @LayoutRes
    resId: Int
) : BaseFragment<D>(resId) {
    protected abstract val currentPage: Int

    protected fun gotoNext() {
        val actionId = when (currentPage) {
            1  -> R.id.action_signUp1Fragment_to_signUp2Fragment
            2  -> R.id.action_signUp2Fragment_to_signUp3Fragment
            3  -> R.id.action_signUp3Fragment_to_signUp4Fragment
            4  -> R.id.action_signUp4Fragment_to_signUp5Fragment
            5  -> R.id.action_signUp5Fragment_to_signUp6Fragment
            else  -> throw Exception("Navigation page index overflow")
        }

        findNavController().navigate(actionId)
    }

    protected fun gotoPrev() {
        val actionId = when (currentPage) {
            2  -> R.id.action_signUp2Fragment_to_signUp1Fragment
            3  -> R.id.action_signUp3Fragment_to_signUp2Fragment
            4  -> R.id.action_signUp4Fragment_to_signUp3Fragment
            5  -> R.id.action_signUp5Fragment_to_signUp4Fragment
            6  -> R.id.action_signUp6Fragment_to_signUp5Fragment
            else  -> throw Exception("Navigation page index overflow")
        }

        findNavController().navigate(actionId)
    }
}