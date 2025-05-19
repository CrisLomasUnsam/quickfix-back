package quickfix.utils

fun commission(totalEarnings: Double?): Double {
    return (totalEarnings ?: 0.0) * COMMISSION
}
