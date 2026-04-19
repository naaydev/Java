package com.taskmanager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private long id;
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDate dueDate;
    private String category;
    private List<String> tags;
    private Instant createdAt;
    private Instant completedAt;

    public Task(String title) {
        this(title, "", Priority.MEDIUM);
    }

    public Task(String title, String description, Priority priority) {
        this.id = Instant.now().toEpochMilli();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.tags = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }
    public String getCategory() { return category; }
    public List<String> getTags() { return tags; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setCategory(String category) { this.category = category; }

    public void complete() {
        this.status = TaskStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void reopen() {
        this.status = TaskStatus.PENDING;
        this.completedAt = null;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }
}