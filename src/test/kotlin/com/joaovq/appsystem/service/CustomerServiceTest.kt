package com.joaovq.appsystem.service

import com.joaovq.appsystem.entity.Address
import com.joaovq.appsystem.entity.Customer
import com.joaovq.appsystem.exception.BusinessException
import com.joaovq.appsystem.repository.CustomerRepository
import com.joaovq.appsystem.service.impl.CustomerServiceImpl
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerServiceImpl

    @Test
    fun `should create customer`() {
        //        given
        val fakeCustomer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer
        //        when
        val actual = customerService.createCustomer(fakeCustomer)
        //        then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerService.createCustomer(fakeCustomer) }
    }

    @Test
    fun `shoud find customer by id`() {
//        given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
//        when
        val actual = customerService.getCustomerById(fakeId)
//        then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        Assertions.assertThat(actual.id).isEqualTo(fakeId)

        verify { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should throw BusinessException for not found user by id`() {
        //        given
        val fakeId: Long = Random.nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        //        when
        //        then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy {
                customerService.getCustomerById(fakeId)
            }.withMessage("Customer not exist")

        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should delete customer by id`() {
        // given
        val fakeId = Random.nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs
        // when
        customerService.delete(fakeId)
        // then
        verify(exactly = 1) {
            customerRepository.findById(fakeId)
        }
        verify(exactly = 1) {
            customerRepository.delete(fakeCustomer)
        }
    }

    private fun buildCustomer(
        id: Long? = null,
        firstName: String = "João Vítor",
        lastName: String = "Queiroz Santos",
        cpf: String = "0786594734",
        email: String = "test@gmail.com",
        password: String = "1234567",
        income: BigDecimal = BigDecimal.valueOf(1000),
        zipCode: String = "34857384",
        street: String = "Street californnia impa"
    ): Customer {
        return Customer(
            id,
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            address = Address(zipCode, street),
            password = password,
            income = income
        )
    }
}