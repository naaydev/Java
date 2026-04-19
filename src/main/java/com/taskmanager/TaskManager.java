package com.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private final List<Task> tasks;
    private final List<Category> categories;
    private final Path dataDir;
    private final Gson gson;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.dataDir = Path.of(System.getProperty("user.home"), ".task_manager");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    private void loadData() {
        try {
            Files.createDirectories(dataDir);
            Path tasksFile = dataDir.resolve("tasks.json");
            Path categoriesFile = dataDir.resolve("categories.json");

            if (Files.exists(tasksFile)) {
                String json = Files.readString(tasksFile);
                Type listType = new TypeToken<List<Task>>(){}.getType();
                List<Task> loaded = gson.fromJson(json, listType);
                if (loaded != null) tasks.addAll(loaded);
            }

            if (Files.exists(categoriesFile)) {
                String json = Files.readString(categoriesFile);
                Type listType = new TypeToken<List<Category>>(){}.getType();
                List<Category> loaded = gson.fromJson(json, listType);
                if (loaded != null) categories.addAll(loaded);
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            Files.writeString(dataDir.resolve("tasks.json"), gson.toJson(tasks));
            Files.writeString(dataDir.resolve("categories.json"), gson.toJson(categories));
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public Task addTask(String title, String description, Priority priority, LocalDate dueDate, String category) {
        Task task = new Task(title, description, priority);
        task.setDueDate(dueDate);
        task.setCategory(category);
        tasks.add(task);
        saveData();
        return task;
    }

    public List<Task> listTasks(TaskStatus status, Priority priority) {
        return tasks.stream()
            .filter(t -> status == null || t.getStatus() == status)
            .filter(t -> priority == null || t.getPriority() == priority)
            .collect(Collectors.toList());
    }

    public boolean completeTask(long id) {
        return tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .map(t -> { t.complete(); saveData(); return true; })
            .orElse(false);
    }

    public boolean deleteTask(long id) {
        return tasks.removeIf(t -> t.getId() == id && saveData());
    }

    public Task getTask(long id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public Category addCategory(String name, String description, String color) {
        Category category = new Category(name, description, color);
        categories.add(category);
        saveData();
        return category;
    }

    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", tasks.size());
        stats.put("pending", (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count());
        stats.put("completed", (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count());
        stats.put("highPriority", (int) tasks.stream().filter(t -> t.getPriority() == Priority.HIGH).count());
        return stats;
    }
}