package com.joaovq.appsystem.controller

import com.joaovq.appsystem.dto.CreditDto
import com.joaovq.appsystem.dto.CreditView
import com.joaovq.appsystem.dto.CreditViewList
import com.joaovq.appsystem.service.CreditService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("api/v1/credit/")
class CreditController(
    @Autowired
    private val creditService: CreditService
) {
    @PostMapping
    fun createCredit(@Valid creditDto: CreditDto): ResponseEntity<String> {
        creditService.createCredit(creditDto.toEntity())
        return ResponseEntity.status(200).body("Created")
    }

    @GetMapping
    fun getAllByCustomer(
        @RequestParam(value = "customerId")
        customerId: Long
    ): ResponseEntity<List<CreditViewList>> {
        val creditCustomers = creditService.getAllByCustomers(customerId)
        val creditCustomersList = creditCustomers.map { CreditViewList(it) }
        return ResponseEntity.status(HttpStatus.OK).body(creditCustomersList)
    }

    @GetMapping("{creditCode}")
    fun getByCreditCode(
        @RequestParam(value = "customerId")
        customerId: Long,
        @PathVariable
        creditCode: UUID
    ): ResponseEntity<CreditView> {
        val credit = creditService.getByCreditCode(customerId, creditCode)
        val creditView = CreditView(credit)
        return ResponseEntity.status(HttpStatus.OK).body(creditView)
    }
}