package com.lewisallen.rtdptiCache.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard")
public class Dashboard
{
    @Column(name = "dashboardid")
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    @Column(name = "template")
    private String template;

    @Column(name = "name")
    private String overrideName;

    @Column(name = "switchid")
    private Long switchId;

    @Column(name = "dashboarddata")
    private String data;

    @Column(name = "createddate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "lastuseddate")
    private LocalDateTime lastUsedDate = LocalDateTime.now();

    public Dashboard(){}

    public Dashboard(String template, String overrideName, String data)
    {
        this.data = data;
        this.overrideName = overrideName;
        this.template = template;
    }

    public Dashboard(String template, String overrideName, String data, Long switchId)
    {
        this(template, overrideName, data);
        this.switchId = switchId;
    }

    public Long getId()
    {
        return id;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public LocalDateTime getCreatedDate()
    {
        return createdDate;
    }

    public LocalDateTime getLastUsedDate()
    {
        return lastUsedDate;
    }

    public void setLastUsedDate(LocalDateTime lastUsedDate)
    {
        this.lastUsedDate = lastUsedDate;
    }

    @Override
    public String toString() {
        return String.format(
                "Dashboard[id=%d, data='%s']",
                id, data);
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public Long getSwitchId()
    {
        return switchId;
    }

    public void setSwitchId(Long switchId)
    {
        this.switchId = switchId;
    }

    public String getOverrideName()
    {
        return overrideName;
    }

    public void setOverrideName(String overrideName)
    {
        this.overrideName = overrideName;
    }
}
