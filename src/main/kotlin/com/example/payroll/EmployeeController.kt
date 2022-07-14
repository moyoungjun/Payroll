package com.example.payroll

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


@RestController
class EmployeeController(
    val repository: EmployeeRepository,
    val assembler: EmployeeModelAssembler
){

    @GetMapping("/employees")

    fun all():CollectionModel<EntityModel<Employee>>{
        val employees : List<EntityModel<Employee>> =
            repository.findAll().stream()
                .map(assembler::toModel)
//                .map {
//                        employee: Employee->
//                    EntityModel.of(
//                        employee,
//                        linkTo<EmployeeController> { employee.id }.withSelfRel(),
//                        linkTo<EmployeeController> { all() }.withRel("employees")
//
//                    )
//              }
                .collect(Collectors.toList())
            return CollectionModel.of(employees,
                linkTo<EmployeeController> { all() }.withSelfRel())
    }


    @PostMapping("/employees")

    fun newEmployee(@RequestBody newEmployee:Employee): ResponseEntity<EntityModel<Employee>>
    {
        val entityModel = assembler.toModel(repository.save(newEmployee))

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel)
//        return repository.save(newEmployee)
    }
    @GetMapping("/employees/{id}")
    fun one(@PathVariable id:Long):EntityModel<Employee>
    {
//        val employee = repository.findById(id)
//            .orElseThrow {
//                 EmployeeNotFoundException(id)
//            }
//        return EntityModel.of(
//            employee,
//            linkTo<EmployeeController> { one(id) }.withSelfRel(),
//            linkTo<EmployeeController> { all() }.withRel("employees")
//        )

        val employee:Employee = repository.findById(id)
            .orElseThrow{EmployeeNotFoundException(id)}
        return assembler.toModel(employee)
    }

    @PutMapping("/employees/{id}")
    fun replaceEmployee(@RequestBody newEmployee: Employee, @PathVariable id: Long?): ResponseEntity<*>?  {

    val updatedEmployee = repository.findById(id!!)
        .map { employee:Employee ->
            employee.firstname = newEmployee.firstname
            employee.lastname = newEmployee.lastname
            employee.role=newEmployee.role
            repository.save(employee)
        }
        .orElseGet{
            newEmployee.id = id
            repository.save(newEmployee)
        }
        val entityModel = assembler.toModel(updatedEmployee)
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel)



//        return repository.findById(id!!)
//            .map { employee: Employee ->
//                employee.firstname = newEmployee.firstname
//                employee.lastname = newEmployee.lastname
//
//                employee.role = newEmployee.role
//                repository.save(employee)
//            }
//            .orElseGet {
//                newEmployee.id=id
//                repository.save(newEmployee)
//            }
    }


    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id:Long)
    {
        repository.deleteById(id)
    }
}