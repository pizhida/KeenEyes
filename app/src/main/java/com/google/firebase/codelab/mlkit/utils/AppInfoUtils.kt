package com.google.firebase.codelab.mlkit.utils

class AppInfoUtils {

    companion object {

        private var INSTACE : AppInfoUtils? = null

        fun getInstance() : AppInfoUtils {
            return if (INSTACE == null) {
                INSTACE = AppInfoUtils()
                INSTACE!!
            } else INSTACE!!
        }

    }

    fun setupConfig(
            appCode: String = "",
            appName: String = "",
            versionCode: Int = 0,
            versionName: String = "",
            restAPIKey: String = "",
            applicationId: String = ""
    ) {
        APP_CODE = appCode
        APP_NAME = appName
        VERSION_CODES = versionCode
        VERSION_NAME = versionName
        APPLICATION_ID = applicationId
        REST_API_CODE = restAPIKey

        USER_AGENT = HEADER_AGENT + "/" + VERSION_NAME + "/" + System.getProperty("http.agent")
        URI_PROVIDER = APPLICATION_ID + ".provider"
        HEADER_AGENT = APP_CODE
    }

    val DBKEY = "OOKBEE_ANDROID_APPLICATION_DATABASE_ENCRIPTION_KEY_ANNA_702"

    var APP_CODE = "SSS"
        private set

    var REST_API_CODE = "AexecBDSYjP1pzZ0oJavz5VX4nZ+eUQWSAt2jV3nxSSUAEFOTkFfNzAy"
        private set

    val PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuz9QJfQ8A+p2fdAMSLEl9RWZyVrhwNbbHqknlgFdGxH6vx4N1gDmpf96zmyOhbCLQSIim9u5FTBXMM4+AgqmmL4TXLhXXXRbSvXvfptSG38rAZfXCv6VkuqF5gtPjEOTcjA25Y36esGwIxDAhOrbzDm7RNt5RufnW+XxicNfcxUbjl1cQhuNjoJqcOHfi8sJfsYUmaHE9UFEo2FCK3wMaZkvYHBXgfMDA6y6gxZ9pMA/AtbRi5hHin/OJVm5/1xyBQWkQmNWwBWOaKnFVbMqpRv9EgMXPMbvUX5OsD1DZynQYdcJJ0bsgVa+7Z3DuKymw8uKTLQhSxFuz8kY5nLhfwIDAQAB"

    var APP_NAME = "Anna"
        private set

    var HEADER_AGENT = APP_CODE

    var VERSION_CODES = 0
        private set

    var VERSION_NAME = ""
        private set

    var APPLICATION_ID = "com.google.firebase.codelab.mlkit"
    //private set

    var USER_AGENT = HEADER_AGENT + "/" + VERSION_NAME + "/" + System.getProperty("http.agent")

    var URI_PROVIDER = APPLICATION_ID + ".provider"

    var isSupportGooglePlay: Boolean = false
        private set

    //val PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuz9QJfQ8A+p2fdAMSLEl9RWZyVrhwNbbHqknlgFdGxH6vx4N1gDmpf96zmyOhbCLQSIim9u5FTBXMM4+AgqmmL4TXLhXXXRbSvXvfptSG38rAZfXCv6VkuqF5gtPjEOTcjA25Y36esGwIxDAhOrbzDm7RNt5RufnW+XxicNfcxUbjl1cQhuNjoJqcOHfi8sJfsYUmaHE9UFEo2FCK3wMaZkvYHBXgfMDA6y6gxZ9pMA/AtbRi5hHin/OJVm5/1xyBQWkQmNWwBWOaKnFVbMqpRv9EgMXPMbvUX5OsD1DZynQYdcJJ0bsgVa+7Z3DuKymw8uKTLQhSxFuz8kY5nLhfwIDAQAB"
}
