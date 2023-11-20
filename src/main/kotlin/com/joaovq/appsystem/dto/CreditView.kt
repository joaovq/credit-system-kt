package com.joaovq.appsystem.dto

import com.joaovq.appsystem.entity.Credit
import com.joaovq.appsystem.entity.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val dayOfInstallment: Int,
    val status: Status,
    val incomeCustomer: BigDecimal?,
    val emailCustomer: String?
) {
    constructor(credit: Credit): this(
        credit.creditCode,
        credit.creditValue,
        credit.numberOfInstallment,
        credit.status,
        credit.customer?.income,
        credit.customer?.email,
    )
}
