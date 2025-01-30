package com.chaitanya.mangashelf.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.chaitanya.mangashelf.local.entity.MangaEntity
import com.chaitanya.mangashelf.ui.theme.getColorString
import com.chaitanya.mangashelf.ui.theme.textStylePop12Lh16Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop14Lh16Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop18Lh24Fw700
import com.chaitanya.mangashelf.utility.getFormatedNumber
import com.chaitanya.mangashelf.utility.toReadableDate

@Composable
fun MangaDetailSharedCard(
    manga: MangaEntity,
    onFavoriteToggle: () -> Unit= {},
    onMarksAsRead: (String) -> Unit= {},
    modifier: Modifier,
    showMarksAsRead: Boolean = false,
    showFavorite: Boolean = false
){
    Row (modifier = modifier
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(manga.image)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .width(150.dp)
                .fillMaxHeight()
            ,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxSize()
        ) {
            if (showFavorite) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(360.dp))
                        .background(
                            color = getColorString("#ADD8E6").copy(alpha = 0.5f),
                            shape = RoundedCornerShape(360.dp)
                        )
                        .noRippleClick {
                            onFavoriteToggle()
                        }
                        .padding(8.dp)
                        .align(Alignment.End)
                ) {
                    AnimatedContent(targetState = manga.isFavorite, label = "") {
                        Icon(
                            imageVector = if (it) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = getColorString("#ADD8E6")
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                manga.title?:"",
                style = textStylePop18Lh24Fw700(),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Published: ${manga.publishedChapterDate?.toReadableDate()?:""}",
                style = textStylePop14Lh16Fw500(),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text ="Popularity: ${manga.popularity?.getFormatedNumber()?:""}",
                    style = textStylePop14Lh16Fw500(),
                    color = Color.White
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
            ){
                Text(text = manga.category?:"",
                    style = textStylePop14Lh16Fw500(),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Score: ",
                    style = textStylePop14Lh16Fw500(),
                    color = Color.White
                )
                Text(
                    text = manga.score?.toString()?:"",
                    style = textStylePop12Lh16Fw500(),
                    color = Color.White
                )
            }
            if (showMarksAsRead){
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = getColorString("#3373c4"),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .then(
                            if (!manga.isRead) {
                                Modifier.clickable {
                                    onMarksAsRead(manga.id)
                                }
                            } else Modifier
                        )
                        .padding(8.dp)
                ){
                    Text(
                        text = if (!manga.isRead) "Mark as read" else "Completed",
                        style = textStylePop14Lh16Fw500(),
                        color = Color.White,)
                }
            }

        }
    }
}