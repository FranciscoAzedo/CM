package com.example.challenge1;

public class Animal {
    private String type;
    private String owner;
    private String name;
    private int age;

    public Animal(String type, String owner, String name, int age) {
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
