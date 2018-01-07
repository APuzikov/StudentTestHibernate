package ru.mera.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "difficulty_level")
public enum DifficultyLevel {

    EASY(), NORMAL(), HARD();

    private int id;
    private String name;
    private String systemName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "system_name")
    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
