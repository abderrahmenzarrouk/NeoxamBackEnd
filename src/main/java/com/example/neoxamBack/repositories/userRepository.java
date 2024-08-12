package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.userRole.id = :roleId")
    List<User> findByUserRoleId(@Param("roleId") int roleId);

    List<User> findByDirectorId(@Param("Id") Long Id);

}
