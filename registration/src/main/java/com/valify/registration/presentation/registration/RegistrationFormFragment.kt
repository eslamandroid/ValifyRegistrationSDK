package com.valify.registration.presentation.registration

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.valify.registration.R
import com.valify.registration.databinding.FragmentRegistrationFormBinding
import com.valify.registration.domain.model.UserModel
import com.valify.registration.domain.utils.AppError
import com.valify.registration.domain.utils.ValidationType
import com.valify.registration.presentation.registration.viewmodel.RegistrationIntent
import com.valify.registration.presentation.registration.viewmodel.RegistrationViewModel
import com.valify.registration.utils.hiddenKeyboard
import com.valify.registration.utils.navigationSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFormFragment : Fragment(R.layout.fragment_registration_form) {
    private var _binding: FragmentRegistrationFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()
    private var userInput = UserModel("", "", "", "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentRegistrationFormBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindClicks()
        binding.bindTextFields()
        binding.collectRegistrationState()
    }

    private fun FragmentRegistrationFormBinding.bindClicks() {
        saveDataButton.setOnClickListener {
            requireActivity().currentFocus?.let { requireContext().hiddenKeyboard(it) }
            viewModel.sendIntent(RegistrationIntent.SaveUserIntent(userInput))
        }
    }

    private fun FragmentRegistrationFormBinding.bindTextFields() {
        userNameInputLayout.editText?.addTextChangedListener {
            userNameInputLayout.error = null
            userInput = userInput.copy(username = it?.toString() ?: "")
        }
        emailInputLayout.editText?.addTextChangedListener {
            emailInputLayout.error = null
            userInput = userInput.copy(email = it?.toString() ?: "")
        }
        phoneInputLayout.editText?.addTextChangedListener {
            phoneInputLayout.error = null
            userInput = userInput.copy(phone = it?.toString() ?: "")
        }
        passwordInputLayout.editText?.addTextChangedListener {
            passwordInputLayout.error = null
            userInput = userInput.copy(password = it?.toString() ?: "")
        }
        passwordInputLayout.editText?.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    requireContext().hiddenKeyboard(this)
                    true
                } else {
                    false
                }
            }
        }

    }

    private fun FragmentRegistrationFormBinding.bindErrorInField(fieldKey: String, type: ValidationType) {
        when (fieldKey) {
            "username" -> {
                userNameInputLayout.error = if (type == ValidationType.Empty) getString(R.string.required_text_field)
                else getString(R.string.username_text_field_label)
            }

            "email" -> {
                emailInputLayout.error = if (type == ValidationType.Empty) getString(R.string.required_text_field)
                else getString(R.string.please_enter_valid_email_error_msg)
            }

            "phone" -> {
                phoneInputLayout.error = if (type == ValidationType.Empty) getString(R.string.required_text_field)
                else getString(R.string.please_enter_valid_phone_error_msg)
            }

            "password" -> {
                passwordInputLayout.error = if (type == ValidationType.Empty) getString(R.string.required_text_field)
                else getString(R.string.please_enter_valid_password_error_msg)
            }
        }
    }

    private fun FragmentRegistrationFormBinding.collectRegistrationState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    with(state) {
                        loadingIndicator.visibility = if (loading) View.VISIBLE else View.GONE
                        saveDataButton.visibility = if (loading) View.INVISIBLE else View.VISIBLE

                        appFailure?.apply {
                            if (this is AppError.ValidateError) {
                                bindErrorInField(fieldKey, type)
                            } else {
                                val message = when (this) {
                                    is AppError.DatabaseError -> getString(R.string.database_error_msg)
                                    else -> getString(R.string.something_went_wrong_msg)
                                }

                                Snackbar.make(root, message, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).setTextColor(Color.WHITE).show()
                            }
                        }

                        if (success) {
                           navigationSafe(RegistrationFormFragmentDirections.actionRegistrationFormFragmentToCaptureImageFragment())
                        }
                    }


                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}