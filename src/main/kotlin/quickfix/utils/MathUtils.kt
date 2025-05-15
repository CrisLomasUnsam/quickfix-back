package quickfix.utils

const val COMMISSION = 0.04

fun commission(totalEarnings: Double?): Double {
    return (totalEarnings ?: 0.0) * COMMISSION
}
