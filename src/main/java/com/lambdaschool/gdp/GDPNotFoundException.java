package com.lambdaschool.gdp;

public class GDPNotFoundException extends RuntimeException
{
    public GDPNotFoundException(Long id)
    {
        super("Could not find GDP");
    }
}
