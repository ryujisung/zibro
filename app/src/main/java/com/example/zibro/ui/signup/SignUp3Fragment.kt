package com.example.zibro.ui.signup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.zibro.R
import com.example.zibro.databinding.FragmentSignUp3Binding

class SignUp3Fragment : SignUpFragment<FragmentSignUp3Binding>(R.layout.fragment_sign_up3) {
    val viewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.hdSignUp3.setNavigationClickListener {
            gotoPrev()
        }

        binding.next.setOnClickListener {
            hideKeyboard()
            if (viewModel.inputPasswordReCheck()) {
                gotoNext()
            }
            else{
                YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(binding.txtSign3Eror)
                binding.edtSignUp3Email.setBackgroundResource(R.drawable.edt_error)
                binding.txtSign3Eror.visibility = View.VISIBLE
            }
        }
    }
    override val currentPage: Int = 3
}