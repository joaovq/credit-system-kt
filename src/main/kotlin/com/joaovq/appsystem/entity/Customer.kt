package com.joaovq.appsystem.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "customer_tb")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) var firstName: String = "",
    @Column(nullable = false) var lastName: String = "",
    @Column(nullable = false, unique = true) val cpf: String = "",
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(nullable = false) var password: String = "",
    @Embedded
    @Column(nullable = false)
    var address: Address = Address(),
    @Column(nullable = false)
    var income: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false)
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [
            CascadeType.PERSIST
        ],
        mappedBy = "customer"
    )
    var credit: List<Credit> = mutableListOf(),
)
