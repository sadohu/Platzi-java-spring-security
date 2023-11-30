package com.platzi.pizza.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.platzi.pizza.persistence.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

}
