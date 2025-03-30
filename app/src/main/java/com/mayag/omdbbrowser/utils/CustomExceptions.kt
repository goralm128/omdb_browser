package com.mayag.omdbbrowser.utils

import java.io.IOException


sealed class CustomException(message: String, cause: Throwable? = null) : IOException(message, cause)

class NetworkException(message: String, cause: Throwable? = null) : CustomException(message, cause)
class ServerException(message: String, cause: Throwable? = null) : CustomException(message, cause)
class ParsingException(message: String, cause: Throwable? = null) : CustomException(message, cause)
class UnknownException(message: String, cause: Throwable? = null) : CustomException(message, cause)
