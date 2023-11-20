package com.joaovq.appsystem.exception

import java.lang.RuntimeException

data class BusinessException(
    override val message: String
) : RuntimeException(message)