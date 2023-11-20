package com.joaovq.appsystem.service

import com.joaovq.appsystem.entity.Credit
import com.joaovq.appsystem.entity.Customer
import java.util.UUID

interface CreditService {
    fun getCredits(): List<Credit>
    fun createCredit(credit: Credit): Credit
    fun getByCreditCode(customerId: Long,creditCode: UUID): Credit
    fun getAllByCustomers(customerId: Long): List<Credit>
}