package com.LMS.Library.Management.System.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class EmployeeCodeGenrator {
    public static String genrateEmpCode(String name,Integer id){
        String first = name.substring(0,2).toUpperCase();
        Random random = new Random();
        int tmp = 1000 + random.nextInt(8999);
        int mid = tmp + random.nextInt(500);
        Integer sixdigit = Integer.parseInt(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));

        String empId = id + first + (mid + sixdigit) ;

        return empId;
    }
}
