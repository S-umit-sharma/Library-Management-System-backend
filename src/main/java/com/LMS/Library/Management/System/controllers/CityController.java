package com.LMS.Library.Management.System.controllers;

import com.LMS.Library.Management.System.dto.CityResponseDto;
import com.LMS.Library.Management.System.entities.City;
import com.LMS.Library.Management.System.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/search")
    public ResponseEntity<List<CityResponseDto>> searchCity(@RequestParam String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(cityService.searchCity(keyword));
    }
}
