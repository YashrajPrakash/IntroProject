package com.letsGo.me.learningSB.services;

import com.letsGo.me.learningSB.dto.EmployeeDTO;
import com.letsGo.me.learningSB.entities.EmployeeEntity;
import com.letsGo.me.learningSB.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper; //Injected by spring (Bean configuration)
    }


    public Optional<EmployeeDTO> getEmployeeById(Long id) {
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
//        ModelMapper mapper = new ModelMapper();  // BAD WAY -> let's do it in spring way.

//      new EmployeeDTO(employeeEntity.getId(),employeeEntity.getName(),); // This will create problem as we have to do it for evey other method and every other entity.

        return employeeRepository.findById(id).map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));
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

    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
      /*  EmployeeEntity employeeEntity = modelMapper.map(employeeDTO,EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);   //This works as a hashMap if the ID's present update it or if not make a new record.
        return modelMapper.map(savedEmployeeEntity,EmployeeDTO.class);*/

        //Hibernate does not allow changing the primary key of a managed entity.

          /*
            ✅ Path variable ID ≠ DTO ID
            ✅ Spring does not merge them
            ✅ ModelMapper blindly copies nulls
            ✅ Entity ID must never change
            ✅ Skip ID mapping during update
           */

        //The below approach does only update if ID exists previously.
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found!!"));
        modelMapper.map(employeeDTO,existingEmployee);
        EmployeeEntity savedEmployee = employeeRepository.save(existingEmployee);
        return modelMapper.map(savedEmployee,EmployeeDTO.class);


        /*
        The above method would not work as id would be null when -> modelMapper.map(employeeDTO,existingEmployee);
        ModelMapper sees: employeeDTO.id == null
        So it does: existingEmployee.setId(null); // 💥 illegal
        And Hibernate says: “You changed a managed entity’s primary key. Not allowed.”

        Solution is to skip the ID by mapper config or create a new dto without an ID field.

        Also , Client could send conflicting IDs,
                ID should be immutable during update,
                    JPA entities rely on stable identifiers
         */

    }

    public boolean isExistsByEmployeeId(Long employeeId){
        return  employeeRepository.existsById(employeeId);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        boolean exists = employeeRepository.existsById(employeeId);
        if(!exists) return false;
        employeeRepository.deleteById(employeeId);
        return true;
    }

    public EmployeeDTO updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
        boolean exists = isExistsByEmployeeId(employeeId);
        if(!exists) return null;
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
        updates.forEach((field,value)->{
            Field fieldToBeUpdated = ReflectionUtils.findField(EmployeeEntity.class,field);
            if (fieldToBeUpdated == null) {
                throw new RuntimeException("Invalid field: " + field);
            }
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated,employeeEntity,value);
        });
        return modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDTO.class);
    }
}
