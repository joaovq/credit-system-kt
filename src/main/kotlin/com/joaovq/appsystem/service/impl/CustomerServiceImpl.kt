package com.joaovq.appsystem.service.impl

import com.joaovq.appsystem.entity.Customer
import com.joaovq.appsystem.exception.BusinessException
import com.joaovq.appsystem.repository.CustomerRepository
import com.joaovq.appsystem.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    @Autowired
    private val customerRepository: CustomerRepository
) : CustomerService {
    override fun getCustomers(): List<Customer> = customerRepository.findAll()

    override fun createCustomer(customer: Customer): Customer = customerRepository.save(customer)

    override fun getCustomerById(id: Long?): Customer =
        customerRepository.findByIdOrNull(id) ?: throw BusinessException("Customer not exist")

    override fun delete(id: Long): Customer? {
        val customerById = getCustomerById(id).apply {
            customerRepository.delete(this)
        } ?: throw BusinessException("Customer not exist")
        return customerById
    }
}