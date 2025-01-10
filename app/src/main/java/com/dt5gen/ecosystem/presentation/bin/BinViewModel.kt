package com.dt5gen.ecosystem.presentation.bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.domain.repository.BinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinViewModel @Inject constructor(
    private val repository: BinRepository
) : ViewModel() {
    private val _binInfo = MutableStateFlow<BinInfoResponse?>(null)
    val binInfo: StateFlow<BinInfoResponse?> = _binInfo

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchBinInfo(bin: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBinInfo(bin)
                _binInfo.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }
}
