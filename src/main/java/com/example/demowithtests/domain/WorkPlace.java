package com.example.demowithtests.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workplaces")
@Builder
public class WorkPlace {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "air_conditioning")
    private Boolean airConditioning;

    @Column(name = "coffee_machine")
    private Boolean coffeeMachine;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getAirConditioning() {
        return airConditioning;
    }

    public Boolean getCoffeeMachine() {
        return coffeeMachine;
    }
}
