package com.joaovq.appsystem.controller

import com.joaovq.appsystem.dto.CustomerDto
import com.joaovq.appsystem.dto.CustomerUpdateDto
import com.joaovq.appsystem.dto.CustomerView
import com.joaovq.appsystem.entity.Customer
import com.joaovq.appsystem.service.CustomerService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpClientErrorException.BadRequest

@RestController
@RequestMapping("/api/v1/customers/")
class CustomerController(
    @Autowired
    private val customerService: CustomerService
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Customer>> {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomers())
    }

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customer: CustomerDto): ResponseEntity<Customer> {
        val savedCustomer = customerService.createCustomer(customer = customer.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer)
    }

    @PutMapping("{id}")
    fun getCustomer(@PathVariable id: Long, @RequestBody @Valid newCustomer: CustomerUpdateDto): ResponseEntity<CustomerView> {
        val customer = customerService.getCustomerById(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            CustomerView(
                customer
            )
        )
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long) {
        customerService.delete(id)
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId")
        id: Long,
        @RequestBody
        @Valid
        customerUpdateDto: CustomerUpdateDto,
    ): ResponseEntity<CustomerView> {
        val customer = customerService.getCustomerById(id) ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST)
        val entity = customerUpdateDto.toEntity(customer)
        customerService.createCustomer(customer)
        return  ResponseEntity.status(HttpStatus.OK).body(CustomerView(entity))
    }
}