package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model is used to represent a database record for a template.
 * Used by JPA to retrieve and save records to database.
 * Templates are html files used to construct the webpages in the application.
 * Templates can be found in resources/templates
 */
@Entity
@Table(name = "template")
public class Template {
    @Id
    @Column(name = "templateid")
    private int templateId;

    @Column(name = "name")
    private String templateName;

    // Default constructor used by Spring
    public Template() {
    }

    public String getTemplateName() {
        return templateName;
    }
}
