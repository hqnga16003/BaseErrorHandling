package com.example.myapplication.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.AuthRepository
import com.example.myapplication.domain.UserDataValidator
import com.example.myapplication.domain.Result

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userDataValidator: UserDataValidator,
    private val repository: AuthRepository
): ViewModel() {

    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onRegisterClick(password: String) {
        when(val result = userDataValidator.validatePassword(password)) {
            is Result.Error -> {
                when(result.error) {
                    UserDataValidator.PasswordError.TOO_SHORT -> TODO()
                    UserDataValidator.PasswordError.NO_UPPERCASE -> TODO()
                    UserDataValidator.PasswordError.NO_DIGIT -> TODO()
                }
            }
            is Result.Success -> {

            }
        }

        viewModelScope.launch {
            when(val result = repository.register(password)) {
                is Result.Error -> {
                    val errorMessage = result.error.asUiText()
                    eventChannel.send(UserEvent.Error(errorMessage))
                }
                is Result.Success -> {
                    result.data
                }
            }
        }
    }
}

sealed interface UserEvent {
    data class Error(val error: UiText): UserEvent
}