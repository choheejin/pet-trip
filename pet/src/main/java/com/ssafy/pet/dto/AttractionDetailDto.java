package com.ssafy.pet.dto;

import lombok.Data;

@Data
public class AttractionDetailDto {
	private AttractionsDto attraction;
    private PetAttractionsDto petAttraction;
}
