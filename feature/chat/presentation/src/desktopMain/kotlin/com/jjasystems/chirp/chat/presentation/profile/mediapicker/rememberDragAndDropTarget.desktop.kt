package com.jjasystems.chirp.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DropTargetDropEvent
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun rememberDragAndDropTarget(
    onHover: (Boolean) -> Unit,
    onDrop: (PickedImageData) -> Unit
): DragAndDropTarget {
    val scope = rememberCoroutineScope()

    return remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val nativeEvent = event.nativeEvent as DropTargetDropEvent
                val fileList = nativeEvent
                    .transferable
                    .getTransferData(DataFlavor.javaFileListFlavor)
                        as List<*>

                if (fileList.size != 1) return false

                val file = fileList.first() as File
                val hasValidExtension = allowedImageExtensions.any {
                    file.extension.lowercase() == it
                }

                if (hasValidExtension) {
                    scope.launch(Dispatchers.IO) {
                        onDrop(
                            PickedImageData(
                            file.readBytes(),
                            getMimeTypeFromFilename(file.name)
                        ))
                    }
                    return true
                }

                return false
            }

            override fun onStarted(event: DragAndDropEvent) {
                onHover(true)
            }

            override fun onEnded(event: DragAndDropEvent) {
                onHover(false)
            }
        }
    }
}