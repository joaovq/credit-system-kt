package com.joaovq.appsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.joaovq.appsystem.dto.CustomerDto
import com.joaovq.appsystem.dto.CustomerUpdateDto
import com.joaovq.appsystem.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should create a customer and return 201 STATUS`() {
        //given
        val fakeCustomerDto = buildCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(fakeCustomerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João Vítor"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Queiroz Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("53889951007"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("34857384"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save customer with same CPF and return 409 STATUS`() {
        //given
        customerRepository.save(buildCustomerDto().toEntity())
        val fakeCustomerDto = buildCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(fakeCustomerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        ).andExpect {
            MockMvcResultMatchers.status().isConflict
        }.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            ).andExpect(
                MockMvcResultMatchers.jsonPath("$.details[*]")
                    .isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save customer with firstName empty and return 400 STATUS`() {
        //given
        val fakeCustomerDto = buildCustomerDto(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(fakeCustomerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            ).andExpect(
                MockMvcResultMatchers.jsonPath("$.details[*]")
                    .isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return 200 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val valueAsString = objectMapper.writeValueAsString(customer)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${URL}${customer.id}").accept(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not found customer with invalid id and return 400 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val invalidId = customer.id?.plus(100L)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${URL}${invalidId}").accept(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.title")
                    .value("Bad request! Consult documentation")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class com.joaovq.appsystem.exception.BusinessException")
            )
    }


    @Test
    fun `should delete customer by id and return 204 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("${URL}${customer.id}").accept(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent
        ).andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer with invalid id and return 400 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val invalidId = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("${URL}${invalidId}").accept(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class com.joaovq.appsystem.exception.BusinessException")
            ).andExpect(
                MockMvcResultMatchers.jsonPath("$.details[*]")
                    .isNotEmpty
            )
    }

    @Test
    fun `should update data customer with customerId and return 200 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto = buildCustomerUpdateDto(firstName = "Joao atualizado")
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("${URL}?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joao atualizado"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Queiroz Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("53889951007"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
    }

    @Test
    fun `should not update data customer with invalid customer id and return 400 status`() {
        //given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto = buildCustomerUpdateDto(firstName = "Joao atualizado")
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)
        val invalidId = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("${URL}?customerId=${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
    }

    private fun buildCustomerUpdateDto(
        firstName: String = "João Vítor",
        lastName: String = "Queiroz Santos",
        email: String = "test@gmail.com",
        income: BigDecimal = BigDecimal.valueOf(1000),
        zipCode: String = "34857384",
        street: String = "Street californnia impa"
    ): CustomerUpdateDto {
        return CustomerUpdateDto(
            firstName = firstName,
            lastName = lastName,
            email = email,
            income,
            street,
            zipCode,
        )
    }

    private fun buildCustomerDto(
        id: Long? = null,
        firstName: String = "João Vítor",
        lastName: String = "Queiroz Santos",
        cpf: String = "53889951007",
        email: String = "test@gmail.com",
        password: String = "1234567",
        income: BigDecimal = BigDecimal.valueOf(1000),
        zipCode: String = "34857384",
        street: String = "Street californnia impa"
    ): CustomerDto {
        return CustomerDto(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            income,
            street,
            zipCode,
            password,
        )
    }

    companion object {
        const val URL = "/api/v1/customers/"
    }
}