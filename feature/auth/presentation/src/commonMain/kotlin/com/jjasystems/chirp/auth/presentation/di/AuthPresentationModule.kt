package com.jjasystems.chirp.auth.presentation.di

import com.jjasystems.chirp.auth.presentation.email_verification.EmailVerificationViewModel
import com.jjasystems.chirp.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.jjasystems.chirp.auth.presentation.login.LoginViewModel
import com.jjasystems.chirp.auth.presentation.register.RegisterViewModel
import com.jjasystems.chirp.auth.presentation.register_success.RegisterSuccessViewModel
import com.jjasystems.chirp.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}