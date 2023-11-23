package com.joaovq.appsystem.dto

import com.joaovq.appsystem.entity.Customer
import java.math.BigDecimal

data class CustomerView(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String
) {
    constructor(customer: Customer) : this(
        customer.id ?: 0,
        customer.firstName,
        customer.lastName,
        customer.cpf,
        customer.income,
        customer.email,
        customer.address.zipCode,
        customer.address.street
    )
}
