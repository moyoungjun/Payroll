package com.example.payroll

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
class OrderController
    (
        val orderRepository: OrderRepository,
        val assembler: OrderModelAssembler
            ){
        @GetMapping("/orders")
        fun all():CollectionModel<EntityModel<Order>>
        {
            val orders:List<EntityModel<Order>> = orderRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList())
            return CollectionModel.of(orders,
            linkTo<OrderController> {all()}.withSelfRel())

        }
       @GetMapping("/orders/{id}")
       fun one(@PathVariable id:Long):EntityModel<Order>{
           val order = orderRepository.findById(id)
               .orElseThrow {
                   OrderNotFoundException(id)
               }
           return assembler.toModel(order)
       }
    @PostMapping("/orders")
    fun newOrder(@RequestBody order:Order):ResponseEntity<EntityModel<Order>>
    {
        order.status = Status.IN_PROGRESS
        val newOrder = orderRepository.save(order)
        return ResponseEntity
            .created(linkTo<OrderController> { newOrder.id?.let { one(it) } }.toUri())
            .body(assembler.toModel(newOrder))
    }


    @DeleteMapping("/orders/{id}/cancel")
    fun cancel(@PathVariable id:Long):ResponseEntity<*>{
        val order = orderRepository.findById(id)
            .orElseThrow{ OrderNotFoundException(id) }
        if(order.status == Status.IN_PROGRESS)
        {
            order.status = Status.CANCELLED
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
        }
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE,MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                 .withTitle("Method not allowed")
                 .withDetail("You can't cancel an order that is in the"+order.status+"status"))
    }

    @PutMapping("orders/{id}/complete")
    fun complete(@PathVariable id:Long):ResponseEntity<*>?{
        val order = orderRepository.findById(id)
            .orElseThrow{OrderNotFoundException(id)}

        if(order.status==Status.IN_PROGRESS)
        {
            order.status = Status.COMPLETED
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))

        }
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                .withTitle("Method not allowed")
                .withDetail("You can't cancel an order that is in the"+order.status+"status"))
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "hello World!!"
    }
}