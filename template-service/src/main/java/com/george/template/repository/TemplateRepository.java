package com.george.template.repository;

import com.george.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    boolean existsTemplateByTitle(String title);
}
