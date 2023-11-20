package com.joaovq.appsystem.service

import com.joaovq.appsystem.entity.Customer

interface CustomerService {
    fun getCustomers(): List<Customer>
    fun createCustomer(customer: Customer): Customer
    fun getCustomerById(id: Long?): Customer
    fun delete(id: Long): Customer?
}