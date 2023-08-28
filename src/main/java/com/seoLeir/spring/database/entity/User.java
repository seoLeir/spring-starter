package com.seoLeir.spring.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedEntityGraph(
        name = "User.company",
        attributeNodes = @NamedAttributeNode("company")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class User extends AuditingEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private LocalDate birthDate;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ToString.Exclude
    @Builder.Default
    @NotAudited
    @OneToMany(mappedBy = "user")
    private List<UsersChat> usersChats = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", birthDate=" + birthDate +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", role=" + role +
                ", company=" + company +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
