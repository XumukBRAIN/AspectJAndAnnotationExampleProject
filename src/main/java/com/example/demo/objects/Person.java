package com.example.demo.objects;

import com.example.demo.annotations.SetterBuilder;

public class Person {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    @SetterBuilder
    public void setName(String name) {
        this.name = name;
    }

    @SetterBuilder
    public void setAge(String age) {
        this.age = age;
    }
}
