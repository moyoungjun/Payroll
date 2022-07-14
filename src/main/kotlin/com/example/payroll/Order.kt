package com.example.payroll

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="CUSTOMER_ORDER")
class Order(
    @Id @GeneratedValue var id: Long? = null,
    var description: String,
    var status: Status
            )


    {
        @Override
        override fun equals(other:Any?): Boolean {
            if(this==other)
                return true
            if(!(other is Order))
                return false
            val order:Order = other as Order
                    return Objects.equals(this.id,order.id)&&Objects.equals(this.description,order.description) && this.status == order.status

        }
        @Override
        override fun hashCode(): Int {
            return Objects.hash(this.id, this.description, this.status)
        }
        @Override
        override fun toString():String{
            return "Order{"+this.id+",description'"+this.description+'\''+",status="+this.status+"}"
        }

}