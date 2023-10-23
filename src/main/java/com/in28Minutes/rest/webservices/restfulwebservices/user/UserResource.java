package com.in28Minutes.rest.webservices.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserResource {
    private UserDaoService  service;
    public UserResource(UserDaoService service) {
        super();
        this.service = service;
    }

//    REST API for All users
    @GetMapping("/users")
    public List<User> retrieveAllUser(){
        return  service.findAll();
    }


//    REST API for Single User
//    http://localhost:8080/users
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id){
        User user = service.findOne(id);
//        Returning 404 if user is not found
        if(user == null){
            throw new UserNotFoundException("Id:-"+id);
        }
//        Code for adding links to response
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUser());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }


//    Create User
//    @Valid adding validation to object
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
       User savedUser= service.save(user);

//       Returning URI after creating user
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        //        Returning 201 after creating a user
        return ResponseEntity.created(location).build();
    }

    //    REST API for Delete a User
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        service.deleteByID(id);
    }
}
