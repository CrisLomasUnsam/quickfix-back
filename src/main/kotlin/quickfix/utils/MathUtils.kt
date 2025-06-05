package quickfix.utils

fun commission(totalEarnings: Double?): Double {
    return (totalEarnings ?: 0.0) * COMMISSION
}

fun Double.putOffDots() : Double {
    val strValue = this.toString().replace(".", "")
    return strValue.toDouble()
}