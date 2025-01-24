package com.george.repository;

import com.george.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    boolean existsTemplateByTitle(String title);
}
