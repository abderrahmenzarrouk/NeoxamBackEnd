package com.example.neoxamBack.repositories;
import com.example.neoxamBack.entities.module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface moduleRepository extends JpaRepository<module,Integer> {

}
