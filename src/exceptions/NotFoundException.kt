package exceptions

sealed class ServiceException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)

class NotFoundException : ServiceException()