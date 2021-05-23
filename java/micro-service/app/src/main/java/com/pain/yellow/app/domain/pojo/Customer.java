package com.pain.yellow.app.domain.pojo;

import javax.persistence.*;

public class Customer {
    @Id
    private Long id;

    @Column(name = "is_member")
    private Boolean isMember;

    private Integer age;

    private String name;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return is_member
     */
    public Boolean getIsMember() {
        return isMember;
    }

    /**
     * @param isMember
     */
    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}