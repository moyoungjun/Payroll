package com.example.payroll

class OrderNotFoundException(id: Long) : RuntimeException("Could not find order $id")