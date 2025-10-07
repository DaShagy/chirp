package com.jjasystems.chirp.chat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.core.design_system.generated.resources.icon_upload
import chirp.core.design_system.generated.resources.Res as DesignSystemRes
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import chirp.feature.chat.presentation.generated.resources.contact_support_to_change_your_email
import chirp.feature.chat.presentation.generated.resources.current_password
import chirp.feature.chat.presentation.generated.resources.delete
import chirp.feature.chat.presentation.generated.resources.delete_profile_image
import chirp.feature.chat.presentation.generated.resources.delete_profile_image_desc
import chirp.feature.chat.presentation.generated.resources.email
import chirp.feature.chat.presentation.generated.resources.new_password
import chirp.feature.chat.presentation.generated.resources.password
import chirp.feature.chat.presentation.generated.resources.password_change_successful
import chirp.feature.chat.presentation.generated.resources.password_hint
import chirp.feature.chat.presentation.generated.resources.profile_image
import chirp.feature.chat.presentation.generated.resources.save
import chirp.feature.chat.presentation.generated.resources.upload_image
import com.jjasystems.chirp.chat.presentation.profile.components.ProfileHeaderSection
import com.jjasystems.chirp.chat.presentation.profile.components.ProfileSectionLayout
import com.jjasystems.chirp.chat.presentation.profile.mediapicker.rememberImagePickerLauncher
import com.jjasystems.chirp.core.design_system.components.avatar.AvatarSize
import com.jjasystems.chirp.core.design_system.components.avatar.ChirpAvatar
import com.jjasystems.chirp.core.design_system.components.brand.ChirpHorizontalDivider
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButton
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButtonStyle
import com.jjasystems.chirp.core.design_system.components.dialog.ChirpAdaptiveDialogSheetLayout
import com.jjasystems.chirp.core.design_system.components.dialog.DestructiveConfirmationDialog
import com.jjasystems.chirp.core.design_system.components.textfields.ChirpPasswordTextField
import com.jjasystems.chirp.core.design_system.components.textfields.ChirpTextField
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import com.jjasystems.chirp.core.presentation.util.DeviceConfiguration
import com.jjasystems.chirp.core.presentation.util.clearFocusOnTap
import com.jjasystems.chirp.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberImagePickerLauncher { pickedImageData ->
        viewModel.onAction(ProfileAction.OnPictureSelected(
            bytes = pickedImageData.bytes,
            mimeType = pickedImageData.mimeType
        ))
    }

    ChirpAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ProfileScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    is ProfileAction.OnDismiss -> onDismiss()
                    is ProfileAction.OnUploadPictureClick -> {
                        launcher.launch()
                    }
                    else -> Unit
                }

                viewModel.onAction(action)
            }
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeaderSection(
            username = state.username,
            onCloseClick = {
                onAction(ProfileAction.OnDismiss)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp)
        )

        ChirpHorizontalDivider()

        ProfileSectionLayout(
            headerText = stringResource(Res.string.profile_image),
        ) {
            Row {
                ChirpAvatar(
                    displayText = state.userInitials,
                    size = AvatarSize.LARGE,
                    imageUrl = state.profilePictureUrl,
                    onClick = {
                        onAction(ProfileAction.OnUploadPictureClick)
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChirpButton(
                        text = stringResource(Res.string.upload_image),
                        onClick = {
                            onAction(ProfileAction.OnUploadPictureClick)
                        },
                        style = ChirpButtonStyle.SECONDARY,
                        enabled = !state.isDeletingImage && !state.isUploadingImage,
                        isLoading = state.isUploadingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(DesignSystemRes.drawable.icon_upload),
                                contentDescription = stringResource(Res.string.upload_image)
                            )
                        }
                    )

                    ChirpButton(
                        text = stringResource(Res.string.delete),
                        onClick = {
                            onAction(ProfileAction.OnDeletePictureClick)
                        },
                        style = ChirpButtonStyle.DESTRUCTIVE_SECONDARY,
                        enabled = !state.isDeletingImage &&
                                !state.isUploadingImage &&
                                state.profilePictureUrl != null,
                        isLoading = state.isDeletingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.delete)
                            )
                        }
                    )
                }

                state.imageError?.let {
                    Text(
                        text = it.asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        ChirpHorizontalDivider()

        ProfileSectionLayout(
            headerText = stringResource(Res.string.email)
        ) {
            ChirpTextField(
                state = state.emailTextState,
                enabled = false,
                supportingText = stringResource(Res.string.contact_support_to_change_your_email)
            )
        }

        ChirpHorizontalDivider()

        ProfileSectionLayout(
            headerText = stringResource(Res.string.password)
        ) {
            ChirpPasswordTextField(
                state = state.currentPasswordState,
                isPasswordVisible = state.isCurrentPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleCurrentPasswordVisibility)
                },
                placeholder = stringResource(Res.string.current_password),
                isError = state.newPasswordError != null,
            )

            ChirpPasswordTextField(
                state = state.newPasswordState,
                isPasswordVisible = state.isNewPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleNewPasswordVisibility)
                },
                placeholder = stringResource(Res.string.new_password),
                isError = state.newPasswordError != null,
                supportingText = state.newPasswordError?.asString()
                    ?: stringResource(Res.string.password_hint)
            )

            if(state.isPasswordChangeSuccessful) {
                Text(
                    text = stringResource(Res.string.password_change_successful),
                    color = MaterialTheme.colorScheme.extended.success,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.End
                )
            ) {
                ChirpButton(
                    text = stringResource(Res.string.cancel),
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {
                        onAction(ProfileAction.OnDismiss)
                    }
                )

                ChirpButton(
                    text = stringResource(Res.string.save),
                    onClick = {
                        onAction(ProfileAction.OnChangePasswordClick)
                    },
                    enabled = state.canChangePassword,
                    isLoading = state.isChangingPassword
                )
            }
        }

        val deviceConfiguration = currentDeviceConfiguration()

        if (deviceConfiguration in listOf(
                DeviceConfiguration.MOBILE_PORTRAIT,
                DeviceConfiguration.MOBILE_LANDSCAPE
        )) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (state.showDeleteConfirmationDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.delete_profile_image),
            description = stringResource(Res.string.delete_profile_image_desc),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = {
                onAction(ProfileAction.OnConfirmDeleteClick)
            },
            onCancelClick = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialog)
            },
            onDismiss = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialog)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}