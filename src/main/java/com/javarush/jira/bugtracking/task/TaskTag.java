package com.javarush.jira.bugtracking.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "task_tag")
@Getter
@Setter
@NoArgsConstructor
public class TaskTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "tag")
    private String tag;

    public TaskTag(String tag) {
        this.tag = tag;
    }
}
