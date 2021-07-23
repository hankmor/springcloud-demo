package com.koobyte.domain;

/**
 * Created by sun on 2021/7/22.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class User {
    //~ Static fields/constants/initializer


    //~ Instance fields

    private Long id;
    private String name;
    private Integer age;

    //~ Constructors

    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    //~ Methods


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}