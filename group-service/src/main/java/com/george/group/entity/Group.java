package com.george.group.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupName;

    private String groupDescription;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id", nullable = false)
    )
    @Column(name = "user_id")
    private Set<Long> userId = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;

        return groupId != null && groupId.equals(group.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }
}
