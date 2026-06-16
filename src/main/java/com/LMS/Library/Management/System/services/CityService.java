package com.LMS.Library.Management.System.services;

import com.LMS.Library.Management.System.dao.CityDao;
import com.LMS.Library.Management.System.dto.CityResponseDto;
import com.LMS.Library.Management.System.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityDao cityDao;

    public List<CityResponseDto> searchCity(String keyword) {
        List<CityResponseDto> cityResponseDtos = new ArrayList<>();

        return cityDao.findTop10ByNameContainingIgnoreCase(keyword).stream().map(city-> {
            CityResponseDto dto = new CityResponseDto();
            dto.setId(city.getId());
            dto.setName(city.getName());
            dto.setStateName(city.getState().getName());
            return dto;
        }).toList();



    }

}