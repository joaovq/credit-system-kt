package com.joaovq.appsystem.service.impl

import com.joaovq.appsystem.entity.Credit
import com.joaovq.appsystem.exception.BusinessException
import com.joaovq.appsystem.repository.CreditRepository
import com.joaovq.appsystem.service.CreditService
import com.joaovq.appsystem.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*

@Service
class CreditServiceImpl(
    @Autowired
    private val creditRepository: CreditRepository,
    @Autowired
    private val customerService: CustomerService
) : CreditService {
    override fun getCredits(): List<Credit> = creditRepository.findAll()

    override fun createCredit(credit: Credit): Credit {
        credit.apply {
            customer = customerService.getCustomerById(credit.customer?.id)
        }

        return creditRepository.save(credit)
    }

    override fun getByCreditCode(customerId: Long,creditCode: UUID): Credit {
        val credit = creditRepository.findByCreditCode(creditCode)
        return  if (credit.customer?.id == customerId) credit else throw BusinessException("")
    }

    override fun getAllByCustomers(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomerId(customerId)
}