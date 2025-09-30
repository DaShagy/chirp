package com.jjasystems.chirp.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.jjasystems.chirp.auth.presentation.email_verification.EmailVerificationRoot
import com.jjasystems.chirp.auth.presentation.login.LoginRoot
import com.jjasystems.chirp.auth.presentation.register.RegisterRoot
import com.jjasystems.chirp.auth.presentation.register_success.RegisterSuccessRoot

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Login
    ) {
        composable<AuthGraphRoutes.Login> {
            LoginRoot(
                onLoginSuccess = onLoginSuccess,
                onForgotPasswordClick = {
                    navController.navigate(AuthGraphRoutes.ForgotPassword)
                },
                onCreateAccountClick = {
                    navController.navigate(AuthGraphRoutes.Register) {
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AuthGraphRoutes.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(it))
                },
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo(AuthGraphRoutes.Register) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot(
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.RegisterSuccess> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AuthGraphRoutes.EmailVerification>(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = "https://chirp.jja-systems.site/api/auth/verify?token={token}"
                },
                navDeepLink {
                    this.uriPattern = "chirp://chirp.jja-systems.site/api/auth/verify?token={token}"
                }
            )
        ) {
            EmailVerificationRoot(
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.EmailVerification> {
                            inclusive = true
                        }
                    }
                },
                onCloseClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.EmailVerification> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}