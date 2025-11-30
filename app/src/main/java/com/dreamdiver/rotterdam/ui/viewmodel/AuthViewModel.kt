package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.isLoggedIn.collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        userType: String,
        phone: String,
        city: String?,
        skillCategory: String? = null,
        hourlyRate: Double? = null
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.register(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = passwordConfirmation,
                userType = userType,
                phone = phone,
                city = city,
                skillCategory = skillCategory,
                hourlyRate = hourlyRate
            )

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null && response.data.user != null) {
                        // Save user data
                        val user = response.data.user
                        preferencesManager.saveUserData(
                            token = response.data.token,
                            userId = user.id,
                            userName = user.name,
                            userEmail = user.email,
                            userType = user.userType,
                            userPhone = user.phone,
                            userCity = user.city,
                            userAvatar = user.avatar,
                            userAddress = user.address,
                            userState = user.state,
                            userZipCode = user.zipCode,
                            userBio = user.profile?.bio,
                            userSkillCategory = user.skillCategory ?: user.profile?.skillCategory,
                            userHourlyRate = user.hourlyRate ?: user.profile?.hourlyRate,
                            userExperienceYears = user.profile?.experienceYears,
                            userSkills = user.profile?.skills,
                            userCertifications = user.profile?.certifications
                        )
                        _authState.value = AuthState.Success(response.message)
                    } else {
                        _authState.value = AuthState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Registration failed")
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.login(email, password)

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null && response.data.user != null) {
                        // Save user data
                        val user = response.data.user
                        preferencesManager.saveUserData(
                            token = response.data.token,
                            userId = user.id,
                            userName = user.name,
                            userEmail = user.email,
                            userType = user.userType,
                            userPhone = user.phone,
                            userCity = user.city,
                            userAvatar = user.avatar,
                            userAddress = user.address,
                            userState = user.state,
                            userZipCode = user.zipCode,
                            userBio = user.profile?.bio,
                            userSkillCategory = user.skillCategory ?: user.profile?.skillCategory,
                            userHourlyRate = user.hourlyRate ?: user.profile?.hourlyRate,
                            userExperienceYears = user.profile?.experienceYears,
                            userSkills = user.profile?.skills,
                            userCertifications = user.profile?.certifications
                        )
                        _authState.value = AuthState.Success(response.message)
                    } else {
                        _authState.value = AuthState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login failed")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearUserData()
            _authState.value = AuthState.Idle
        }
    }

    fun getProfile(token: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.getProfile(token)

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null && response.data.user != null) {
                        // Update local user data with fresh data from server
                        val user = response.data.user
                        preferencesManager.saveUserData(
                            token = token,
                            userId = user.id,
                            userName = user.name,
                            userEmail = user.email,
                            userType = user.userType,
                            userPhone = user.phone,
                            userCity = user.city,
                            userAvatar = response.data.avatarUrl ?: user.avatar,
                            userAddress = user.address,
                            userState = user.state,
                            userZipCode = user.zipCode,
                            userBio = user.profile?.bio,
                            userSkillCategory = user.skillCategory ?: user.profile?.skillCategory,
                            userHourlyRate = user.hourlyRate ?: user.profile?.hourlyRate,
                            userExperienceYears = user.profile?.experienceYears,
                            userSkills = user.profile?.skills,
                            userCertifications = user.profile?.certifications,
                            userAverageRating = response.data.averageRating,
                            userTotalRatings = response.data.totalRatings
                        )
                        _authState.value = AuthState.Success(response.message ?: "Profile fetched successfully")
                    } else {
                        _authState.value = AuthState.Error(response.message ?: "Failed to fetch profile")
                    }
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Failed to fetch profile")
                }
            )
        }
    }

    fun updateProfile(
        token: String,
        name: String,
        phone: String?,
        address: String?,
        city: String?,
        state: String?,
        zipCode: String?,
        latitude: Double?,
        longitude: Double?,
        avatarFile: java.io.File?,
        // Worker-specific fields
        skillCategory: String?,
        skills: List<String>?,
        bio: String?,
        hourlyRate: Double?,
        experienceYears: Int?,
        certifications: List<String>?,
        availability: Map<String, String>?
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.updateProfile(
                token = token,
                name = name,
                phone = phone,
                address = address,
                city = city,
                state = state,
                zipCode = zipCode,
                latitude = latitude,
                longitude = longitude,
                avatarFile = avatarFile,
                skillCategory = skillCategory,
                skills = skills,
                bio = bio,
                hourlyRate = hourlyRate,
                experienceYears = experienceYears,
                certifications = certifications,
                availability = availability
            )

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null && response.data.user != null) {
                        // Update saved user data
                        val user = response.data.user
                        preferencesManager.saveUserData(
                            token = token,
                            userId = user.id,
                            userName = user.name,
                            userEmail = user.email,
                            userType = user.userType,
                            userPhone = user.phone,
                            userCity = user.city,
                            userAvatar = response.data.avatarUrl ?: user.avatar,
                            userAddress = user.address,
                            userState = user.state,
                            userZipCode = user.zipCode,
                            userBio = user.profile?.bio,
                            userSkillCategory = user.skillCategory ?: user.profile?.skillCategory,
                            userHourlyRate = user.hourlyRate ?: user.profile?.hourlyRate,
                            userExperienceYears = user.profile?.experienceYears,
                            userSkills = user.profile?.skills,
                            userCertifications = user.profile?.certifications,
                            userAverageRating = response.data.averageRating,
                            userTotalRatings = response.data.totalRatings
                        )
                        _authState.value = AuthState.Success(response.message ?: "Profile updated successfully")
                    } else {
                        val errorMessage = response.errors?.entries?.joinToString("\n") {
                            "${it.key}: ${it.value.joinToString(", ")}"
                        } ?: response.message ?: "Profile update failed"
                        _authState.value = AuthState.Error(errorMessage)
                    }
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Profile update failed")
                }
            )
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

