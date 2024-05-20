package com.example.travel.state

sealed interface HomeUiState {
    object Success: HomeUiState
    object Loading: HomeUiState
    object Error: HomeUiState
}