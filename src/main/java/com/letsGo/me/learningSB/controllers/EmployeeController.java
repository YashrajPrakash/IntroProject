package com.letsGo.me.learningSB.controllers;

import com.letsGo.me.learningSB.dto.EmployeeDTO;
import com.letsGo.me.learningSB.entities.EmployeeEntity;
import com.letsGo.me.learningSB.repositories.EmployeeRepository;
import com.letsGo.me.learningSB.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

  /*  @GetMapping(path = "/getSecretMessage")
    public String getMySuperSecretMessage(){
        return "Secret message: aasdfalj&21937$$";
    }*/

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/{employeeID}")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeID") Long id) {             //Mandatory param
//        return new EmployeeDTO(id, "Yash", "yash@prakash.com", 25, LocalDate.of(2025, 12, 20), true);
            return employeeService.getEmployeeById(id);
    }

    @GetMapping()
    public List<EmployeeDTO> getAllEmployees(@RequestParam(required = false) Integer age,              //Optional param
                                                @RequestParam(required = false) String sortBy) {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO inputEmployee){
//        inputEmployee.setId(100L);
        return employeeService.createNewEmployee(inputEmployee);
    }

    @PutMapping
    public String updateEmployeeById(){
        return "HEllo from PUT..";
    }
}
