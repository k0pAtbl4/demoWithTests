package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.config.mapper.EmployeeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Employee", description = "Employee API")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    //Операция сохранения юзера в базу данных
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is endpoint to add a new employee.", description = "Create request to add a new employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED. The new employee is successfully created and added to database."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    public EmployeeReadDto saveEmployee(@RequestBody @Valid EmployeeDto requestForSave) {
        Employee employee = employeeMapper.toEmployee(requestForSave);
        EmployeeReadDto response = employeeMapper.toReadDto(employeeService.create(employee));
        return response;
    }

    @PostMapping("/usersS")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEmployee1(@RequestBody EmployeeDto employeeDto) {
        final Employee employee = employeeMapper.INSTANCE.toEmployee(employeeDto);
        employeeService.create(employee);
    }

    //Получение списка юзеров
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllUsers() {
        return employeeMapper.listToReadDto(employeeService.getAll());
    }

    @GetMapping("/users/p")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getPage(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        return employeeService.getAllWithPagination(paging).map(employeeMapper::toReadDto);
    }

    //Получения юзера по id
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned a employee by his id.", description = "Create request to read a employee by id", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK. pam pam param."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    public EmployeeReadDto getEmployeeById(@PathVariable Integer id) {
        log.debug("getEmployeeById() EmployeeController - start: id = {}", id);
        Employee employee = employeeService.getById(id);
        log.debug("getById() EmployeeController - to dto start: id = {}", id);
        EmployeeReadDto dto = employeeMapper.toReadDto(employee);
        log.debug("getEmployeeById() EmployeeController - end: name = {}", dto.name);
        return dto;
    }

    //Обновление юзера
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto refreshEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDto employeeDto) {

        return employeeMapper.toReadDto(employeeService.updateById(id, employeeMapper.toEmployee(employeeDto)));
    }

    //Удаление по id
    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployeeById(@PathVariable Integer id) {
        employeeService.removeById(id);
    }

    //Удаление всех юзеров
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllUsers() {
        employeeService.removeAll();
    }

    @GetMapping("/users/country")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> findByCountry(@RequestParam(required = false) String country,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,
                                        @RequestParam(defaultValue = "") List<String> sortList,
                                        @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        //Pageable paging = PageRequest.of(page, size);
        //Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());
        return employeeService.findByCountryContaining(country, page, size, sortList, sortOrder.toString()).map(employeeMapper::toReadDto);
    }

    @GetMapping("/users/e")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> findByNullEmail() {
        return employeeMapper.listToReadDto(employeeService.filterByNullEmails());
    }

    @PutMapping("/users/c")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> changeCountriesFromLowerCaseToUpperCase() {
        List<Employee> employeeList = employeeService.filterLowerCaseCountries();
        for(Employee e : employeeList) {
            employeeService.updateById(e.getId(), e);
        }
        return employeeMapper.listToReadDto(employeeList);

    }

    @GetMapping("/users/c")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersC() {
        return employeeService.getAllEmployeeCountry();
    }

    @GetMapping("/users/s")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersSort() {
        return employeeService.getSortCountry();
    }

    @GetMapping("/users/emails")
    @ResponseStatus(HttpStatus.OK)
    public Optional<String> getAllUsersSo() {
        return employeeService.findEmails();
    }

    @GetMapping("/users/countryBy")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getByCountry(@RequestParam(required = true) String country) {
        return employeeMapper.listToReadDto(employeeService.filterByCountry(country));
    }

    @GetMapping("/users/ua")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllUkrainians() throws Exception {

        return employeeMapper.listToReadDto(employeeService.getAllUkrainians());
    }

    @GetMapping("/users/nc")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllByNameAndCountry(@RequestParam(required = true) String country,
                                                        @RequestParam(required = true) String name) throws Exception {
        return employeeMapper.listToReadDto(employeeService.filterByNameAndCountry(name, country));
    }
}
