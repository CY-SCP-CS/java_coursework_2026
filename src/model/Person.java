package model;

import java.util.Objects;

/**
 * 系统用户抽象父类
 * Player 和 Admin 继承此类
 */
public abstract class Person implements Reportable {
    private String id;
    private String name;
    private Role role;

    public Person() {
    }

    public Person(String id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // === Getters and Setters ===

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // === 抽象方法 ===

    /**
     * 返回用户的描述信息
     */
    public abstract String getDescription();

    // === equals, hashCode, toString ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + name + "', role=" + role + "}";
    }
}
