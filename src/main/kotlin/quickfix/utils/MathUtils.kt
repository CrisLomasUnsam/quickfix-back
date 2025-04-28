package quickfix.utils

const val COMISSION = 0.4

fun comission(totalEarnings: Double?): Double {
    return (totalEarnings ?: 0.0) * COMISSION
}
