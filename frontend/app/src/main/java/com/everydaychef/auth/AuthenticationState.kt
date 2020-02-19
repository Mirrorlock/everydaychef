package com.everydaychef.auth

enum class AuthenticationState {
    UNAUTHENTICATED,        // Initial state, the user needs to authenticate
    MANUALLY_AUTHENTICATED, // The user has authenticated successfully
    GOOGLE_AUTHENTICATED,   // The user has authenticated via google successfully
    FACEBOOK_AUTHENTICATED, // The user has authenticated via facebook successfully
    INVALID_AUTHENTICATION  // Authentication failed
}