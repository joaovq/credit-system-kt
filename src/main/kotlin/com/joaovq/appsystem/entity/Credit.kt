package com.joaovq.appsystem.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "credit_tb")
data class Credit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, unique = true) val creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false) val creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) val dayFirstIntallment: LocalDate,
    @Column(nullable = false) val numberOfInstallment: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val status: Status = Status.IN_PROGRESS,
    @ManyToOne(
        fetch = FetchType.LAZY, cascade = [
            CascadeType.REMOVE
        ]
    )
    var customer: Customer? = null,
)
