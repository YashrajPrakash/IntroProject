package com.letsGo.me.learningSB.services;

import com.letsGo.me.learningSB.dto.EmployeeDTO;
import com.letsGo.me.learningSB.entities.EmployeeEntity;
import com.letsGo.me.learningSB.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    public EmployeeDTO getEmployeeById(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElse(null);
//        ModelMapper mapper = new ModelMapper();  // BAD WAY -> let's do it in spring way.

//      new EmployeeDTO(employeeEntity.getId(),employeeEntity.getName(),); // This will create problem as we have to do it for evey other method and every other entity..

        return modelMapper.map(employeeEntity,EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities =  employeeRepository.findAll();
        return employeeEntities
                .stream()
                    .map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDTO.class))
                        .toList();
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee,EmployeeEntity.class);
        EmployeeEntity savedEmployeeEntity =  employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEmployeeEntity,EmployeeDTO.class);
    }
}
