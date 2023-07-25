package com.example.demowithtests.dto;

public record WorkPlaceDto (
        Integer id,
        String name,
        Boolean airConditioning,
        Boolean coffeeMachine
) {
    public WorkPlaceDto {
        //airConditioning = Boolean.TRUE;
        //coffeeMachine = Boolean.TRUE;
    }
}
