package com.chaitanya.mangashelf.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.chaitanya.mangashelf.local.entity.MangaEntity
import com.chaitanya.mangashelf.ui.components.MangaDetailSharedCard
import com.chaitanya.mangashelf.ui.components.noRippleClick
import com.chaitanya.mangashelf.ui.theme.getColorString
import com.chaitanya.mangashelf.ui.theme.textStylePop12Lh16Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop14Lh16Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop18Lh24Fw700
import com.chaitanya.mangashelf.utility.getFormatedNumber

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MangaDetailScreen(
    mangaId:String?,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiState: MangaUiState,
    onBack: () -> Unit,
    onFavoriteToggle: (String, Boolean) -> Unit,
    onMarksAsRead: (String) -> Unit,
    onSimilarSelected:(MangaEntity)-> Unit
) {
    val manga = uiState.mangas.find { it.id == mangaId }
    manga?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = getColorString("#121621")),
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(manga.image)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .blur(radius = 16.dp)
                        .fillMaxSize().animateContentSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                getColorString("#121621").copy(alpha = 0.5f),
                                getColorString("#121621")
                            )
                        )
                    ))
                Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(360.dp))
                                .background(
                                    color = Color.White.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(360.dp)
                                )
                                .noRippleClick {
                                    onBack()
                                }
                                .padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = null,
                                tint = Color.White)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(360.dp))
                                .background(
                                    color = getColorString("#ADD8E6").copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(360.dp)
                                )
                                .noRippleClick {
                                    onFavoriteToggle(manga.id, !manga.isFavorite)
                                }
                                .padding(8.dp)
                        ) {
                            AnimatedContent(targetState =manga.isFavorite, label = "") {
                                Icon(
                                    imageVector = if (it) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = getColorString("#ADD8E6")
                                )
                            }
                        }
                    }
                    MangaDetailSharedCard(modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(8.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image/${manga.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                        manga = manga,
                        onMarksAsRead = {
                            onMarksAsRead(manga.id)
                        },
                        showMarksAsRead = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (manga.isRead) {
                        Text(
                            text = "You Might Like (${manga.category})",
                            style = textStylePop18Lh24Fw700(),
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            if (manga.isRead) {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),) {
                    items(uiState.mangas.filter { it.category == manga.category && it.id != manga.id && !it.isRead }
                        .sortedByDescending { it.popularity }, key = { it.id }) {
                        MangaSimilarCard(
                            manga = it,
                            onSelected = {
                                onSimilarSelected(it)
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun MangaSimilarCard(manga: MangaEntity,onSelected:()->Unit) {
    Column(modifier = Modifier
        .width(150.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable {
            onSelected()
        }
        .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(manga.image)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(200.dp)
            ,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = manga.title ?: "",
            style = textStylePop14Lh16Fw500(),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Score :${(manga.score?:0)}",
            style = textStylePop14Lh16Fw500(),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = manga.popularity?.getFormatedNumber()?:"",
                style = textStylePop14Lh16Fw500(),
                color = Color.White
            )

        }

    }
}
