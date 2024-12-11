//package com.example.butterapp.common
//
//import com.example.butterapp.common.ViewDataType.INITIAL
//
//enum class ViewDataType {
//    INITIAL, SUCCESS, ERROR, LOADING;
//}
//
//sealed class ViewData<T>(
//    val data: T? = null,
//    private val _message: String? = null,
//    val type: ViewDataType,
//) {
//    class Initial<T>() : ViewData<T>(type = ViewDataType.INITIAL)
//    class Success<T>(data: T) : ViewData<T>(data, type = ViewDataType.SUCCESS)
//    class Error<T>(message: String, data: T? = null) :
//        ViewData<T>(data, message, type = ViewDataType.ERROR)
//    class Loading<T>(data: T? = null) : ViewData<T>(data, type = ViewDataType.LOADING)
//
//    val isInitial = this.type == INITIAL
//    val isSuccess = this.type == ViewDataType.SUCCESS
//    val isError = this.type == ViewDataType.ERROR
//    val isLoading = this.type == ViewDataType.LOADING
//    val message: String // property type is optional since it can be inferred from the getter's return type
//        get() {
//            if (_message == null) {
//                return ""
//            }
//
//            return _message
//        }
//
//    companion object {
//        fun <T> httpException(message: String?): ViewData<T> {
//            return Error(
//                message ?: "An unexpected error occurred"
//            )
//        }
//
//        fun <T> ioException(message: String?): ViewData<T> {
//            return Error(
//                message ?: "Couldn't reach the server. Check your internet Connection"
//            )
//        }
//    }
//}

package com.example.butterapp.common

sealed class ViewData<T>(
    val data: T? = null,
    private val _message: String? = null,
) {
    class Success<T>(data: T) : ViewData<T>(data)
    class Error<T>(message: String, data: T? = null) :
        ViewData<T>(data, message)
    class Loading<T>(data: T? = null) : ViewData<T>(data)

    val message: String
        get() {
            if (_message == null) {
                return ""
            }

            return _message
        }
}