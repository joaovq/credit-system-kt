package com.joaovq.appsystem.repository

import com.joaovq.appsystem.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {
    fun findByCreditCode(creditCode: UUID): Credit
    @Query("SELECT C FROM Credit C where C.customer.id = :customerId")
    fun findAllByCustomerId(customerId: Long): List<Credit>
}