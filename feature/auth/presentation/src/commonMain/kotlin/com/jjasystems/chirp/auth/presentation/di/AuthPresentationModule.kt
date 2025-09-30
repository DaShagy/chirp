package com.jjasystems.chirp.auth.presentation.di

import com.jjasystems.chirp.auth.presentation.register.RegisterViewModel
import com.jjasystems.chirp.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
}