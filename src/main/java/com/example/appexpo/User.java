package com.example.appexpo;

public class User {
    private final String email, password, name;
    private final int age;
    private final double height, weight;

    public User(String email, String password, String name, int age, double height, double weight) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int    getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }

    public boolean checkPassword(String pw) {
        return this.password.equals(pw);
    }
}

