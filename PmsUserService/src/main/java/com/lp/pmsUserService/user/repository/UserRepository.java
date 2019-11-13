package com.lp.pmsUserService.user.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp.pmsUserService.user.model.UserModel;


public interface UserRepository extends JpaRepository<UserModel, Long> {
   

	public Optional<UserModel> findByMail(String mail);

}
