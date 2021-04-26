package com.itau.orange.marketplace.extension

import com.itau.orange.marketplace.SaveCustomerRequest
import com.itau.orange.marketplace.entity.AddressEntity
import com.itau.orange.marketplace.entity.CustomerEntity

fun SaveCustomerRequest.toCustomerEntity() = CustomerEntity(
    id = null,
    name = this.name,
    address = AddressEntity(null, this.street, this.streetNumber, this.zipCode)
)
