package com.example.line_generator.data.user

// View only needs the name of the User
// It is not important for the View to know the id

data class UserViewState(
    val name: String
)