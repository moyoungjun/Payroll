package com.example.payroll

import java.util.Objects
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var firstname: String,
    var lastname : String,
    var role: String
) {
    override fun equals(other: Any?): Boolean
    {
        if(this == other)
            return true
        if(!(other is Employee))
            return false
        val employee:Employee = other as Employee

        return Objects.equals(this.id,employee.id) && Objects.equals(this.firstname,employee.firstname)&&
                Objects.equals(this.lastname,employee.lastname)&&
                Objects.equals(this.role,employee.role)

    }
}