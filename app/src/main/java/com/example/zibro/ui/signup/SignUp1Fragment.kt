package com.example.zibro.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.zibro.R
import com.example.zibro.databinding.FragmentSignUp1Binding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class SignUp1Fragment : SignUpFragment<FragmentSignUp1Binding>(R.layout.fragment_sign_up1) {
    val viewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        val a = 10

        binding.hdSignUp1.setNavigationClickListener {
            requireActivity().finish()
        }

        binding.next.setOnClickListener {
            hideKeyboard()
            Log.e("dd",viewModel.inputCheckEmailPassword().toString())
            if (viewModel.inputEmailCheck()) {
                viewModel.errorMessage.value = null
                gotoNext()
            }
            else{
                YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(binding.txtSign1Eror)
                binding.edtSignUp1Email.setBackgroundResource(R.drawable.edt_error)
                binding.txtSign1Eror.visibility = View.VISIBLE
            }
        }
    }

    override val currentPage: Int = 1
}