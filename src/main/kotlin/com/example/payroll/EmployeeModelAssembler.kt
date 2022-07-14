package com.example.payroll

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class EmployeeModelAssembler:RepresentationModelAssembler<Employee,EntityModel<Employee>>  {

    @Override
    override fun toModel(employee : Employee):EntityModel<Employee>{
        return EntityModel.of(
            employee,
            linkTo<EmployeeController> { one(employee.id!!) }.withSelfRel(),
            linkTo<EmployeeController> { all() }.withRel("employees")
            )

    }


}


