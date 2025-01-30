package com.chaitanya.mangashelf.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaitanya.mangashelf.R
import com.chaitanya.mangashelf.local.entity.MangaEntity
import com.chaitanya.mangashelf.ui.components.CustomBottomSheetWithCross
import com.chaitanya.mangashelf.ui.components.MangaDetailSharedCard
import com.chaitanya.mangashelf.ui.theme.getColorString
import com.chaitanya.mangashelf.ui.theme.textStylePop12Lh16Fw600
import com.chaitanya.mangashelf.ui.theme.textStylePop14Lh16Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop16Lh24Fw500
import com.chaitanya.mangashelf.ui.theme.textStylePop16Lh24Fw700
import com.chaitanya.mangashelf.ui.theme.textStylePop18Lh24Fw700
import com.chaitanya.mangashelf.ui.theme.textStylePop24Lh36Fw600
import kotlinx.coroutines.launch


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.MangaListScreen(
    uiState: MangaUiState,
    onFavoriteToggle: (String,Boolean) -> Unit,
    updateSelectionOnScroll:(Int)->Unit,
    onSelectYear: (Int)->Unit,
    onMangaSelected: (MangaEntity) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRefresh: () -> Unit,
    onSortSelected: (SortType) -> Unit
) {
    var shouldShowFilter by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded =  true
    )
    val scope = rememberCoroutineScope()
    var isProgrammaticScroll by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { index ->
                if (!isProgrammaticScroll) {
                    updateSelectionOnScroll(index)
                }
            }
    }
    val onSheetDismiss  = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                shouldShowFilter = false
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            color = getColorString("#121621")
        )
        .systemBarsPadding()) {
        if (shouldShowFilter)
            CustomBottomSheetWithCross(
                onDismiss = {onSheetDismiss()},
                sheetState = sheetState
            ) {
                ApplySortBottomSheetContent(
                    uiState = uiState,
                    onSortSelected = {
                        onSortSelected(it)
                        scope.launch {
                            lazyListState.scrollToItem(0)
                        }

                    }
                ){
                    onSheetDismiss()
                }
            }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(text = "MangaShelf",
                style = textStylePop24Lh36Fw600(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(onClick = {
                shouldShowFilter = true
            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(painter = painterResource(id = R.drawable.ic_sort), contentDescription = null,
                    tint = Color.White)
            }
        }
        if (uiState.error != null) {
            Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    "Error: ${uiState.error}",
                    style = textStylePop18Lh24Fw700(),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRefresh,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Retry")
                }
            }

        } else {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(color = Color.White
                    )
                }

            } else {

                AnimatedVisibility(
                    visible = uiState.sortType == SortType.Year,
                ) {
                    YearTabs(
                        years = uiState.groupedByYear.keys.toList(),
                        selectedYear = uiState.selectedYear,
                        onYearSelected = { year ->
                            onSelectYear(year)
                            scope.launch {
                                isProgrammaticScroll= true
                                lazyListState.animateScrollToItem(
                                    uiState.yearPositions[year]?.first?:0
                                )
                                isProgrammaticScroll= false
                            }
                        },
                    )
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(color = getColorString("#1B2132"))
                ) {
                    MangaList(
                        mangas = uiState.sortedMangas,
                        onFavoriteToggle = { id, isFavorite -> onFavoriteToggle(id, isFavorite) },
                        lazyListState = lazyListState,
                        onMangaSelected = onMangaSelected,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

            }
        }
    }
}

@Composable
fun ApplySortBottomSheetContent(
    uiState: MangaUiState,
    onSortSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                color = getColorString("#1B2132")
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Apply Sort",
                style = textStylePop16Lh24Fw700(),
                color = Color.White,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .weight(1f)
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .weight(1f)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(color = getColorString("#222E3F"))
            .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Sort by".uppercase(),
                style = textStylePop12Lh16Fw600(),
                color = getColorString("#B9BFD0"),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 8.dp),
                letterSpacing = 3.sp
            )
            SortType.entries.forEach {
                val isSelected = it == uiState.sortType
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(it)
                        onDismiss()
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(size = 360.dp))
                            .border(
                                width = if (isSelected) 4.dp else 2.dp,
                                color = if (isSelected) getColorString("#3373c4") else Color.White,
                                shape = RoundedCornerShape(size = 360.dp)
                            )

                    )
                    Text(
                        text = it.displayName,
                        color = if (isSelected) getColorString("#3373c4") else Color.White,
                        style = textStylePop16Lh24Fw500(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun YearTabs(
    years: List<Int>,
    selectedYear: Int?,
    onYearSelected: (Int) -> Unit
) {
    val selectedTabIndex = years.indexOf(selectedYear)
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        containerColor = Color.Transparent,
        indicator = {
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(it[selectedTabIndex]),
                color = Color.White,
            )

        },
        divider = {}
    ) {
        years.forEach { year ->
            Tab(
                selected = year == selectedYear,
                onClick = {
                    onYearSelected(year)
                },
                text = { Text(
                    year.toString(),
                    style = textStylePop14Lh16Fw500(),
                ) }
            )
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MangaList(
    mangas: List<MangaEntity>,
    onFavoriteToggle: (String, Boolean) -> Unit,
    lazyListState: LazyListState,
    onMangaSelected: (MangaEntity) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        mangas.forEach {
            item {
                MangaCard(
                    manga = it,
                    onFavoriteToggle = { onFavoriteToggle(it.id, !it.isFavorite) },
                    onMangaSelected = onMangaSelected,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }

        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MangaCard(
    manga: MangaEntity,
    onFavoriteToggle: () -> Unit,
    onMangaSelected: (MangaEntity) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Row(modifier = Modifier
        .padding(top = 12.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .height(300.dp)
        .background(color = getColorString("#222E3F"))
        .clickable { onMangaSelected(manga) }
        .padding(8.dp)

    ) {
        MangaDetailSharedCard(modifier = Modifier
            .fillMaxSize()
            .sharedElement(
                state = rememberSharedContentState(key = "image/${manga.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
            ),
            manga = manga,
            onFavoriteToggle = onFavoriteToggle,
            showFavorite = true
        )

    }
}

