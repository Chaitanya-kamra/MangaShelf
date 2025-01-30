package com.chaitanya.mangashelf.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.mangashelf.local.entity.MangaEntity
import com.chaitanya.mangashelf.utility.toYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



data class MangaUiState(
    val mangas: List<MangaEntity> = emptyList(),
    val sortedMangas: List<MangaEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedManga: MangaEntity? = null,
    val selectedYear: Int? = null,
    val groupedByYear: Map<Int, List<MangaEntity>> = emptyMap(),
    val yearPositions: Map<Int, IntRange> = emptyMap(),
    val sortType: SortType = SortType.Year
)
enum class SortType(val displayName: String){
    Year("Year"),
    ScoreASC("Score (Ascending)"),
    ScoreDESC("Score (Descending)"),
    PopularityASC("Popularity (Ascending)"),
    PopularityDESC("Popularity (Descending)"),
}
@HiltViewModel
class MangaViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MangaUiState())
    val uiState: StateFlow<MangaUiState> = _uiState.asStateFlow()
    init {
        fetchMangas()
    }


    fun fetchMangas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getAllMangasFlow()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { mangaList ->
                    if (mangaList.isNotEmpty()) {

                        val groupedByYear =
                            mangaList
                                .filter { it.publishedChapterDate != null }
                                .groupBy { manga ->
                                manga.publishedChapterDate!!.toYear()
                            }.toSortedMap()

                        val yearPositions = mutableMapOf<Int, IntRange>()
                        var position = 0
                        groupedByYear.forEach { (year, mangas) ->
                            val last = position + mangas.size
                            yearPositions[year] = position..<last
                            position = last
                        }
                        _uiState.update {
                            it.copy(
                                mangas = mangaList,
                                groupedByYear = groupedByYear,
                                isLoading = false,
                                selectedYear = it.selectedYear?:groupedByYear.firstKey(),
                                yearPositions = yearPositions,
                                error = null
                            )
                        }
                        updateSortedMangas(mangaList,_uiState.value.sortType)
                    }
                }
        }
        viewModelScope.launch {
            try {
                repository.fetchAndCacheMangas()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load data.") }
                return@launch
            }
        }
    }

    private fun updateSortedMangas(mangaList: List<MangaEntity>, sortType: SortType) {
        when (sortType) {
            SortType.Year -> {
                _uiState.update { uiState -> uiState.copy(sortedMangas = mangaList.sortedBy { it.publishedChapterDate }) }
            }
            SortType.ScoreASC -> {
                _uiState.update { uiState -> uiState.copy(sortedMangas = mangaList.sortedBy { it.score }) }
            }
            SortType.ScoreDESC -> {
                _uiState.update { uiState -> uiState.copy(sortedMangas = mangaList.sortedByDescending { it.score }) }
            }
            SortType.PopularityASC -> {
                _uiState.update { uiState -> uiState.copy(sortedMangas = mangaList.sortedBy { it.popularity }) }
            }
            SortType.PopularityDESC -> {
                _uiState.update { uiState -> uiState.copy(sortedMangas = mangaList.sortedByDescending { it.popularity }) }
            }
        }
    }

    fun updateSortType(sortType: SortType) {
        _uiState.update { it.copy(sortType = sortType) }
        updateSortedMangas(_uiState.value.mangas, sortType)
    }

    fun selectYear(year: Int) {
        _uiState.update { it.copy(selectedYear = year) }
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavorite(id, isFavorite)
        }
    }

    fun updateSelectedOnScroll(index: Int) {
        val visibleYear = _uiState.value.yearPositions.entries.firstOrNull { (_, range) ->
            index in range
        }?.key
        if (visibleYear != null) {
            selectYear(visibleYear)
        }
    }

    fun updateSelectedManga(manga: MangaEntity) {
        _uiState.update { it.copy(selectedManga = manga) }
    }

    fun toggleFavoritefromDetail(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavorite(id, isFavorite)
            _uiState.update {
                it.copy(
                    selectedManga = it.selectedManga?.copy(isFavorite = isFavorite)
                )
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.updateReadStatus(id)
            _uiState.update {
                it.copy(
                    selectedManga = it.selectedManga?.copy(isRead = true)
                )
            }
        }
    }
}