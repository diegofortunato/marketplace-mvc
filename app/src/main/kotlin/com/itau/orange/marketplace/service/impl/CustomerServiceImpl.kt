package com.itau.orange.marketplace.service.impl

import com.itau.orange.marketplace.Customer
import com.itau.orange.marketplace.FindAllRequest
import com.itau.orange.marketplace.FindAllResponse
import com.itau.orange.marketplace.FindCustomerRequest
import com.itau.orange.marketplace.SaveCustomerRequest
import com.itau.orange.marketplace.UpdateCustomerRequest
import com.itau.orange.marketplace.entity.CustomerEntity
import com.itau.orange.marketplace.exception.CustomerNotFoundException
import com.itau.orange.marketplace.extension.toCustomerEntity
import com.itau.orange.marketplace.repository.CustomerRepository
import com.itau.orange.marketplace.service.CustomerService
import io.micronaut.data.model.Pageable
import io.micronaut.tracing.annotation.ContinueSpan
import javax.inject.Singleton

@Singleton
class CustomerServiceImpl(
    private val customeRepository: CustomerRepository
) : CustomerService {

    override fun findAllCustomer(request: FindAllRequest): FindAllResponse = findAll(request)

    override fun findCustomerById(request: FindCustomerRequest): Customer = findById(request)

    override fun saveCustomer(request: SaveCustomerRequest): Customer = save(request)

    override fun updateCustomer(request: UpdateCustomerRequest): Customer = update(request)

    override fun deleteCustomer(request: FindCustomerRequest): Customer = delete(request)

    @ContinueSpan
    private fun findAll(request: FindAllRequest): FindAllResponse {
        val customers = customeRepository.findAll(Pageable.from(request.page, request.size))

        val customerBuilder = FindAllResponse.newBuilder()
        customers.forEach { it ->
            customerBuilder.addCustomers(
                customerBuild(it)
            )
        }
        return customerBuilder.build()
    }

    @ContinueSpan
    private fun findById(request: FindCustomerRequest): Customer {
        val customer = customeRepository.findById(request.id)
            .orElseThrow { CustomerNotFoundException(request.id) }

        return customerBuild(customer)
    }

    @ContinueSpan
    private fun save(request: SaveCustomerRequest): Customer {
        val customer = customeRepository.save(request.toCustomerEntity())

        return customerBuild(customer)
    }

    @ContinueSpan
    private fun update(request: UpdateCustomerRequest): Customer {
        val customer = customeRepository.findById(request.id)
            .orElseThrow { CustomerNotFoundException(request.id) }

        customeUpdate(customer, request)

        val newCustomer = customeRepository.update(customer)

        return customerBuild(newCustomer)
    }

    @ContinueSpan
    private fun delete(request: FindCustomerRequest): Customer {
        val customer = customeRepository.findById(request.id)
            .orElseThrow { CustomerNotFoundException(request.id) }

        customeRepository.delete(customer)

        return customerBuild(customer)
    }

    private fun customerBuild(it: CustomerEntity) = Customer.newBuilder()
        .setId(it.id!!)
        .setName(it.name)
        .setStreet(it.address.street)
        .setStreetNumber(it.address.streetNumber)
        .setZipCode(it.address.zipCode)
        .build()

    private fun customeUpdate(customer: CustomerEntity, request: UpdateCustomerRequest) {
        customer.name = request.name
        customer.address.street = request.street
        customer.address.streetNumber = request.streetNumber
        customer.address.zipCode = request.zipCode
    }
}
