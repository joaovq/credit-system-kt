package com.joaovq.appsystem.dto

import com.joaovq.appsystem.entity.Address
import com.joaovq.appsystem.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.br.CPF
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Invalid input")
    var firstName: String,
    @field:NotEmpty(message = "Invalid input")
    var lastName: String,
    @field:NotEmpty(message = "Invalid input")
    @field:CPF(message = "This invalid cpf")
    val cpf: String,
    @field:NotEmpty(message = "Invalid input")
    @field:Email(message = "Invalid email")
    var email: String,
    @field:NotNull("Invalid input")
    var income: BigDecimal,
    @field:NotEmpty(message = "Invalid input")
    var password: String,
    @field:NotEmpty(message = "Invalid input")
    var zipCode: String,
    @field:NotEmpty(message = "Invalid input")
    var street: String,
) {
    fun toEntity(): Customer {
        return Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = cpf,
            email = this.email,
            password = this.password,
            address = Address(
                zipCode,
                street
            ),
            income = this.income
        )
    }
}
