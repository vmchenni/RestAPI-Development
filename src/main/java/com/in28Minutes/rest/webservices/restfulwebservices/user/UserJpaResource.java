package com.in28Minutes.rest.webservices.restfulwebservices.user;

import com.in28Minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28Minutes.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {
    private UserDaoService  service;
    private UserRepository repository;

    private PostRepository postRepository;
    public UserJpaResource(UserDaoService service,UserRepository repository,PostRepository postRepository) {
        super();
        this.service = service;
        this.repository = repository;
        this.postRepository= postRepository;
    }

//    REST API for All users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUser(){
        return  repository.findAll();
    }


//    REST API for Single User
//    http://localhost:8080/users
    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id){
        Optional<User> user;
        user = repository.findById(id);
//        Returning 404 if user is not found
        if(user.isEmpty()){
            throw new UserNotFoundException("Id:-"+id);
        }
//        Code for adding links to response
        EntityModel<User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUser());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }


//    Create User
//    @Valid adding validation to object
    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
       User savedUser= repository.save(user);

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
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        repository.deleteById(id);
    }

    //    REST API for Delete a User
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForAUser(@PathVariable Integer id){
        Optional<User> user;
        user = repository.findById(id);
//        Returning 404 if user is not found
        if(user.isEmpty()){
            throw new UserNotFoundException("Id:-"+id);
        }
        return user.get().getPosts();
    }
}
