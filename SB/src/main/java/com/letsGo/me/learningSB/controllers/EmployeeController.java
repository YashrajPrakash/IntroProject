package com.letsGo.me.learningSB.controllers;

import com.letsGo.me.learningSB.dto.EmployeeDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

  /*  @GetMapping(path = "/getSecretMessage")
    public String getMySuperSecretMessage(){
        return "Secret message: aasdfalj&21937$$";
    }*/

    @GetMapping(path = "/{employeeID}")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeID") Long id) {             //Mandatory param
        return new EmployeeDTO(id, "Yash", "yash@prakash.com", 25, LocalDate.of(2025, 12, 20), true);
    }

    @GetMapping()
    public String getAllEmployees(@RequestParam(required = false) Integer age,              //Optional param
                                  @RequestParam(required = false) String sortBy) {
        return "Hi age " + age + " -> " +sortBy;
    }

    @PostMapping
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO inputEmployee){
        inputEmployee.setId(100L);
        return inputEmployee;
    }

    @PutMapping
    public String updateEmployeeById(){
        return "HEllo from PUT..";
    }
}
