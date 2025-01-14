package com.example.stocknewsprovider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocknewsprovider.data.model.AccountInfo
import com.example.stocknewsprovider.data.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 5. ViewModel 생성
class StockViewModel(private val repository: StockRepository) : ViewModel() {
    private val _accountInfo = MutableStateFlow<AccountInfo?>(null)
    val accountInfo: StateFlow<AccountInfo?> = _accountInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getAccountInfo(credentials: Map<String, String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAccountInfo(credentials)
                _accountInfo.value = result
            } catch (e: Exception) {
                // 에러 처리
            } finally {
                _isLoading.value = false
            }
        }
    }
}
