package com.joaovq.appsystem.dto

import com.joaovq.appsystem.entity.Credit
import com.joaovq.appsystem.entity.Customer
import com.joaovq.appsystem.entity.Status
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreditDto(
    @field:NotNull(message = "Invalid input") val creditCode: UUID,
    @field:NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field:Future
    @field:NotNull(message = "Invalid input")
    val dayFirstIntallment: LocalDate,
    @field:NotNull(message = "Invalid input") val numberOfInstallment: Int,
    @field:NotNull(message = "Invalid input") val status: Status,
    @field:NotNull(message = "Invalid input") val customerId: Long,
) {
    fun toEntity(): Credit {
        return Credit(
            creditCode = creditCode,
            creditValue = creditValue,
            dayFirstIntallment =  dayFirstIntallment,
            numberOfInstallment = numberOfInstallment,
            status = status,
            customer = Customer(id=this.customerId),
        )
    }
}
