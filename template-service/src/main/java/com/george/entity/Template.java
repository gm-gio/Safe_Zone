package com.george.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "templates")
@FieldDefaults(level = PRIVATE)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long templateId;

    String title;
    String content;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Template template = (Template) o;

        return templateId != null && templateId.equals(template.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId);
    }
}
