package com.example.demowithtests.service.WorkPlaceService;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkPlaceServiceBean implements WorkPlaceService{
    private final WorkPlaceRepository workPlaceRepository;


    @Override
    public WorkPlace create(WorkPlace workPlace) {
        return workPlaceRepository.save(workPlace);
    }

    @Override
    public WorkPlace getById(Integer id) {
        return workPlaceRepository.findById(id).orElseThrow();
    }
}
