package org.example.paymentuserdonatedservice.repository;

import com.gabriel.donation.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Category findById(int id);
}
