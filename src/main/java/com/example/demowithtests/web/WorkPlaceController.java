package com.example.demowithtests.web;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.dto.WorkPlaceDto;
import com.example.demowithtests.service.WorkPlaceService.WorkPlaceService;
import com.example.demowithtests.util.config.mapper.WorkPlaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "WorkPlace", description = "WorkPlace API")
public class WorkPlaceController {
    private final WorkPlaceService workPlaceService;
    private final WorkPlaceMapper workPlaceMapper;

    @PostMapping("/cab")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is endpoint to add new workplace.",
            description = "Create request to add new workplace.", tags = {"WorkPlace"})
    public WorkPlaceDto saveWorkPlace(@RequestBody @Valid WorkPlaceDto workPlaceDto){
        WorkPlace workPlace = workPlaceMapper.fromDto(workPlaceDto);
        return workPlaceMapper.toDto(workPlaceService.create(workPlace));
    }

    @GetMapping("/cab/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned workplace by his id.",
            description = "Create request to read workplace by id", tags = {"WorkPlace"})
    public WorkPlaceDto getWorkPlaceById(@PathVariable Integer id){
        var workPlaceTemp = workPlaceService.getById(id);
        var dto = workPlaceMapper.toDto(workPlaceTemp);
        return dto;
    }
}
