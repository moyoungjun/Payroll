package com.example.payroll

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadDatabase(
    val log:Logger = LoggerFactory.getLogger(LoadDatabase::class.java)
) {
    @Bean
    fun initDatabase(employeerepository: EmployeeRepository,orderRepository: OrderRepository) = CommandLineRunner {
        employeerepository.save(Employee(id=1,firstname = "example12", lastname = "database1", role = "w"))
        employeerepository.save(Employee(firstname = "ex2", lastname = "end", role = "final"))

        employeerepository.findAll().forEach{ employee -> log.info("Preloaded $employee") }


        orderRepository.save(Order(description = "MacBook Pro", status = Status.COMPLETED))
        orderRepository.save(Order(description = "complete",status = Status.IN_PROGRESS))

        orderRepository.findAll().forEach { order -> log.info("Preloaded $order") }
    }

}