package com.example.summerproject.utils;

import com.example.summerproject.exception.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public  class GetUserRole {
     public static void checkAuthority(){
         UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication();
         if(!userDetails.getAuthorities().contains("ROLE_ADMIN")){
             throw new NotFoundException("You are not authorized to perform this task");
         }
     }

}
