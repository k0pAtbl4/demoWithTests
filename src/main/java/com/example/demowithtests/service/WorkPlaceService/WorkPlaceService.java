package com.example.demowithtests.service.WorkPlaceService;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.dto.WorkPlaceDto;

public interface WorkPlaceService {
    WorkPlace create(WorkPlace workPlace);

    WorkPlace getById(Integer id);
}
