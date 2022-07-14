package com.example.payroll

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository :JpaRepository<Order,Long> {
    abstract fun save(order: Order): Order
}