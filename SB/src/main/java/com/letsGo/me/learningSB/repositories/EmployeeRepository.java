package com.letsGo.me.learningSB.repositories;

import com.letsGo.me.learningSB.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

//    List<EmployeeEntity> findByName(String name)
}
