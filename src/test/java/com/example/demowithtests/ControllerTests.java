package com.example.demowithtests;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.config.mapper.EmployeeMapper;
import com.example.demowithtests.web.EmployeeController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
@DisplayName("Employee Controller Tests")
public class ControllerTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper mapper;

    @MockBean
    EmployeeService service;

    @MockBean
    EmployeeMapper employeeMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/users")
    @WithMockUser(roles = "ADMIN")
    public void createPassTest() throws Exception {
        EmployeeReadDto response = EmployeeReadDto.builder()
                .name("Mike")
                .country("Ukraine")
                .email("mail@mail.com")
                .gender(Gender.M)
                .build();

        Employee employee = Employee.builder()
                .name("Mike")
                .country("Ukraine")
                .email("mail@mail.com")
                .gender(Gender.M)
                .build();

        when(employeeMapper.toReadDto(any(Employee.class))).thenReturn(response);
        when(employeeMapper.toEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(service.create(any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Mike")))
                       .andReturn();

        verify(service).create(any());
    }


    @Test
    @DisplayName("Entity POST /api/users")
    @WithMockUser(roles = "ADMIN")
    public void testEntitySave() throws Exception {
        var employee = Employee.builder()
                .id(1)
                .name("Mark")
                .country("France")
                .email("mail.com")
                .gender(Gender.M)
                .build();

        doReturn(employee).when(service).create(any());
        when(this.service.create(any(Employee.class))).thenReturn(employee);

        // Execute the POST request
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/usersS")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee));
        mockMvc
                .perform(mockRequest)
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("$.name", is("Mike")))
                .andReturn().getResponse();

        verify(service, times(1)).create(any(Employee.class));
        verifyNoMoreInteractions(this.service);
    }


    @Test
    @DisplayName("GET /api/users/{id}")
    @WithMockUser(roles = "USER")
    public void getPassByIdTest() throws Exception {
        var response = EmployeeReadDto.builder()
                .name("Mike")
                .build();
        var employee = Employee.builder()
                .id(1)
                .name("Mike")
                .build();

        when(employeeMapper.toReadDto(any(Employee.class))).thenReturn(response);
        when(service.getById(1)).thenReturn(employee);

        MockHttpServletRequestBuilder mockRequest = get("/api/users/1");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mike")));

        verify(service).getById(anyInt());
    }


    @Test
    @DisplayName("PUT /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void updatePassByIdTest() throws Exception {
        var response = EmployeeReadDto.builder()
                .name("Mike")
                .build();

        var employee = Employee.builder()
                .id(1)
                .name("Mike")
                .build();

        when(employeeMapper.toReadDto(any(Employee.class))).thenReturn(response);
        when(employeeMapper.toEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(service.updateById(eq(1), any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mike")));

        verify(service).updateById(eq(1), any(Employee.class));
    }


    @Test
    @DisplayName("PATCH /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void deletePassTest() throws Exception {

        doNothing().when(service).removeById(1);

        MockHttpServletRequestBuilder mockRequest = patch("/api/users/1");

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());

        verify(service).removeById(1);
    }


    @Test
    @DisplayName("GET /api/users/p")
    @WithMockUser(roles = "USER")
    public void getUsersPageTest() throws Exception {

        var employee1 = Employee.builder().id(1).name("John").country("US").build();
        var employee2 = Employee.builder().id(2).name("Jane").country("UK").build();
        var employee3 = Employee.builder().id(3).name("Bob").country("US").build();
        List<Employee> list = Arrays.asList(employee1, employee2, employee3);
        Page<Employee> employeesPage = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 5);

        when(service.getAllWithPagination(eq(pageable))).thenReturn(employeesPage);

        MvcResult result = mockMvc.perform(get("/api/users/p")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        verify(service).getAllWithPagination(eq(pageable));

        String contentType = result.getResponse().getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
    }

    @Test
    @DisplayName("GET /api/users/e")
    @WithMockUser(roles = "USER")
    public void findByNullEmailTest() throws Exception {
        var employee1 = EmployeeReadDto.builder().name("John").country("US").build();
        var employee2 = EmployeeReadDto.builder().name("Jane").country("UK").build();
        var employee3 = EmployeeReadDto.builder().name("Bob").country("US").build();
        List<EmployeeReadDto> readDtoList = List.of(employee1, employee2, employee3);

        when(employeeMapper.listToReadDto(anyList())).thenReturn(readDtoList);

        MvcResult result = mockMvc.perform(get("/api/users/e"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[1].name", is("Jane")))
                .andExpect(jsonPath("$[2].name", is("Bob")))
                .andReturn();

        verify(service).filterByNullEmails();

        String contentType = result.getResponse().getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
    }

    @Test
    @DisplayName("PUT /api/users/c")
    @WithMockUser(roles = "ADMIN")
    public void changeCountriesFromLowerCaseToUpperCaseTest() throws Exception {
        var employee1 = EmployeeReadDto.builder().name("John").country("Germany").build();
        var employee2 = EmployeeReadDto.builder().name("Jane").country("Ukraine").build();
        var employee3 = EmployeeReadDto.builder().name("Bob").country("France").build();
        List<EmployeeReadDto> readDtoList = List.of(employee1, employee2, employee3);

        when(employeeMapper.listToReadDto(anyList())).thenReturn(readDtoList);

        MvcResult result = mockMvc.perform(put("/api/users/c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country", is("Germany")))
                .andExpect(jsonPath("$[1].country", is("Ukraine")))
                .andExpect(jsonPath("$[2].country", is("France")))
                .andReturn();

        verify(service).filterLowerCaseCountries();

        String contentType = result.getResponse().getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
    }

}