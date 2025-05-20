package ru.buzynnikov.user_acount_service.models;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Email> emails;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Phone> phones;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
//        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
//        this.dateOfBirth = dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, password);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", dateOfBirth='").append(dateOfBirth).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
