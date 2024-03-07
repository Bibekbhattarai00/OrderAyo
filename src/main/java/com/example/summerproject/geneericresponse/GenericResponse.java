package com.example.summerproject.geneericresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GenericResponse<T> implements Serializable {
    private boolean success;
    private String message;
    //T is a type parameter or type variable
    private T data;
}