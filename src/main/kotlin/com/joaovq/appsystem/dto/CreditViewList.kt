package com.joaovq.appsystem.dto

import com.joaovq.appsystem.entity.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallment: Int
) {
    constructor(credit: Credit): this(
        credit.creditCode,
        credit.creditValue,
        credit.numberOfInstallment
    )
}
