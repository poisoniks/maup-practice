package com.maup.practice.repository;

import com.maup.practice.model.AddressModel;
import com.maup.practice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, Long> {
    List<AddressModel> findByUser(UserModel userModel);
}
