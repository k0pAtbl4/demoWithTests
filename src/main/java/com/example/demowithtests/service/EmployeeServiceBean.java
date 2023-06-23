package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.util.exception.ResourceWasDeletedException;
import com.example.demowithtests.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeServiceBean implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    // @Transactional(propagation = Propagation.MANDATORY)
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee createEM(Employee employee) {
        return entityManager.merge(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());
    }

    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        PageRequest pr = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        log.debug("getAllWithPagination() - start: pageable = {}", pageable);
        Page<Employee> list = employeeRepository.findAll(pageable);
        log.debug("getAllWithPagination() - end: list = {}", list);


        List<Employee> employees = list.stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());
        list = new PageImpl<>(employees, pr, employees.size());

        return list;
    }

    @Override
    public Employee getById(Integer id) {
        var employee = employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (employee.isIs_deleted()) {
            throw new ResourceWasDeletedException();
        }
        return employee;
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {
        return employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    entity.setIs_deleted(employee.isIs_deleted());
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public void removeById(Integer id) {
        var employee = employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (employee.isIs_deleted()) {
            throw new ResourceWasDeletedException();
        } else {
            employee.setIs_deleted(true);
            updateById(id, employee);
        }

    }







   /* public boolean isValid(Employee employee) {
        String regex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is valid");
            return true;
        } else {
            System.out.println("Number is invalid");
            return false;
        }
    }*/

    /*public boolean isVodafone(Employee employee) {
        String regex = "^[0][9][5]{1}[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is Vodafone");
            return true;
        } else {
            System.out.println("Number is not Vodafone");
            return false;
        }
    }*/

    @Override
    public void removeAll() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            if (!employee.isIs_deleted()) {
                employee.setIs_deleted(true);
                updateById(employee.getId(), employee);
            }
        }
    }

    /*@Override
    public Page<Employee> findByCountryContaining(String country, Pageable pageable) {
        return employeeRepository.findByCountryContaining(country, pageable);
    }*/

    @Override
    public Page<Employee> findByCountryContaining(String country, int page, int size, List<String> sortList, String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        Page<Employee> list = employeeRepository.findByCountryContaining(country, pageable);

        List<Employee> employees = list.stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());

        return new PageImpl<>(employees, pageable, employees.size());
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }

    @Override
    public List<String> getAllEmployeeCountry() {
        log.info("getAllEmployeeCountry() - start:");
        List<Employee> employeeList = employeeRepository.findAll().
                stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());


        List<String> countries = employeeList.stream()
                .map(country -> country.getCountry())
                .collect(Collectors.toList());

        /*List<String> countries = employeeList.stream()
                .map(Employee::getCountry)
                //.sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());*/

        log.info("getAllEmployeeCountry() - end: countries = {}", countries);
        return countries;
    }

    @Override
    public List<String> getSortCountry() {
        List<Employee> employeeList = employeeRepository.findAll().
                stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());
        return employeeList.stream()
                .map(Employee::getCountry)
                .filter(c -> c.startsWith("U"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> findEmails() {
        var employeeList = employeeRepository.findAll().
                stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());

        var emails = employeeList.stream()
                .map(Employee::getEmail)
                .collect(Collectors.toList());

        var opt = emails.stream()
                .filter(s -> s.endsWith(".com"))
                .findFirst()
                .orElse("error?");
        return Optional.ofNullable(opt);
    }

    @Override
    public List<Employee> filterByCountry(String country) {
        List<Employee> list = employeeRepository.findByCountry(country);

        return list.stream().filter(el -> !el.isIs_deleted()).collect(Collectors.toList());
    }

    @Override
    public List<Employee> filterByNullEmails() {
        return employeeRepository.findByEmailNull();
    }

    @Override
    public List<Employee> changeLowerCaseToUpperCaseCountries() {
        List<Employee> list = employeeRepository.findAllLowerCaseCountries();
        String country = "";
        for(Employee e : list) {
            country = e.getCountry();
            if(country != null && country.length() != 0) {
                e.setCountry(country.substring(0, 1).toUpperCase() + country.substring(1));
            }
            updateById(e.getId(), e);
        }
        return list;
    }
}
