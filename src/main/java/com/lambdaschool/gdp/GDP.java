package com.lambdaschool.gdp;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class GDP
{
    private @Id @GeneratedValue Long id;
    private String country;
    private Long gdp;

    public GDP()
    {
        //default constructor
    }

    public GDP(String country, Long gdp)
    {
        this.country = country;
        this.gdp = gdp;
    }
}
