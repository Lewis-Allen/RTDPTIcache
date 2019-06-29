package com.lewisallen.rtdptiCache.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetable")
public class Timetable
{
    @Column(name = "timetableid")
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    @Column(name = "timetablename")
    private String name;

    @Column(name = "timetabledata")
    private String data;

    @Column(name = "createddate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "lastuseddate")
    private LocalDateTime lastUsedDate = LocalDateTime.now();

    public Timetable(){}

    public Timetable(String name, String data)
    {
        this.name = name;
        this.data = data;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
                "Timetable[id=%d, name='%s', data='%s']",
                id, name, data);
    }
}
