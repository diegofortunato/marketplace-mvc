package com.itau.orange.marketplace.repository

import com.itau.orange.marketplace.entity.CustomerEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository

@Repository
interface CustomerRepository : PageableRepository<CustomerEntity, Long>
