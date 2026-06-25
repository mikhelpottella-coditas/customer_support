package com.example.customersupport.repo;

import com.example.customersupport.entity.Customer;
import com.example.customersupport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Optional<Customer> findCustomerByUser(User user);
}
