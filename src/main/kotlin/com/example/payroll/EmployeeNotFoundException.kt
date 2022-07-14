package com.example.payroll


class EmployeeNotFoundException(id: Long) : RuntimeException("Could not find employee $id")