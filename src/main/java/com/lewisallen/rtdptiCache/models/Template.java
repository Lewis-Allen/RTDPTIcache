package com.lewisallen.rtdptiCache.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "template")
public class Template
{
    @Id
    @Column(name = "templateid")
    private int templateId;

    @Column(name = "name")
    private String templateName;

    public Template()
    {
    }

    public String getTemplateName()
    {
        return templateName;
    }
}
