package com.chaitanya.mangashelf.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chaitanya.mangashelf.R
import com.chaitanya.mangashelf.ui.theme.getColorString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheetWithCross(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier,
        containerColor = Color.Transparent,
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        dragHandle = null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        this.detectTapGestures(onTap = {
                            onDismiss.invoke()
                        })
                    }
                    .padding(bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.circle_cross),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onDismiss.invoke()
                        },
                    contentDescription = null,
                )
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .fillMaxWidth()
                    .background(
                        color = getColorString("#222E3F"),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            ) {
                content()
            }
        }

    }
}