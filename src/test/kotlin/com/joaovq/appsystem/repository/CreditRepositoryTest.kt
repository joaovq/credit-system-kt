package com.joaovq.appsystem.repository

import com.joaovq.appsystem.entity.Address
import com.joaovq.appsystem.entity.Credit
import com.joaovq.appsystem.entity.Customer
import com.joaovq.appsystem.entity.Status
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired
    lateinit var creditRepository: CreditRepository

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach
    fun setUp() {
        customer = testEntityManager.persist(buildCustomer())
        credit1  = testEntityManager.persist(buildCredit(customer = customer))
        credit2  = testEntityManager.persist(buildCredit(customer = customer))
    }

    @Test
    fun `should credits by credit code`() {
        //given
        val creditCode1 = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val creditCode2 = UUID.fromString("49f740be-46a7-449b-84e7-ff5b7986d7ef")
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2
        //when
        val fakeCredit1 = creditRepository.findByCreditCode(creditCode1)
        val fakeCredit2 = creditRepository.findByCreditCode(creditCode2)
        //then
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)
    }

    @Test
    fun `should credits by customer id`() {
        //given
        val customerId = 1L
        //when
        val fakeCredits = creditRepository.findAllByCustomerId(customerId)
        //then
        Assertions.assertThat(fakeCredits).isNotEmpty
        Assertions.assertThat(fakeCredits.size).isEqualTo(2)
        Assertions.assertThat(fakeCredits).contains(credit1, credit2)
    }
    private fun buildCredit(
        id: Long? = null,
        creditValue: BigDecimal = BigDecimal.valueOf(1000),
        dayOfFirstInstalment: LocalDate =LocalDate.now(),
        numberOfInstallment: Int = 1,
        status: Status = Status.IN_PROGRESS,
        customer: Customer = buildCustomer()
    ): Credit {
        return Credit(
            id,
            creditValue = creditValue,
            dayFirstIntallment = dayOfFirstInstalment,
            numberOfInstallment=numberOfInstallment,
            status=status,
            customer=customer
        )
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