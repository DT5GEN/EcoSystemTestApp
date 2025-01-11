package com.dt5gen.ecosystem.presentation.bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dt5gen.ecosystem.domain.models.BinHistoryItem
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.domain.repository.BinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class BinViewModel @Inject constructor(
    private val repository: BinRepository
) : ViewModel() {

    private val _binInfo = MutableStateFlow<BinInfoResponse?>(null)
    val binInfo: StateFlow<BinInfoResponse?> = _binInfo

    private val _history = MutableStateFlow<List<BinHistoryItem>>(emptyList())
    val history: StateFlow<List<BinHistoryItem>> = _history

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _errorState = MutableSharedFlow<String>()
    val errorState: SharedFlow<String> get() = _errorState


    fun setError(message: String) {
        viewModelScope.launch {
            _errorState.emit(message)
        }
    }


    init {
        loadHistory()
    }

    fun fetchBinInfo(bin: String) {
        viewModelScope.launch {
            if (bin.length in 6..16) {
                try {
                    val response = repository.getBinInfo(bin)
                    // код обработки успешного ответа
                    _binInfo.value = response
                } catch (e: Exception) {
                    _errorState.emit("Ошибка загрузки данных: ${e.message}")
                }
            } else {
                _errorState.emit("Введите от 6 до 16 цифр!")
            }
        }
    }

    suspend fun clearErrorState() {
        _errorState.emit("") // Очистить ошибку
    }


    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getHistory()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _history.value = emptyList() // Обновление состояния списка истории
        }
    }
}
