package com.taskmanager;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.time.LocalDate;
import java.util.*;

@Command(name = "task-manager", description = "Task Manager CLI", version = "1.0.0")
public class App implements Runnable {
    private final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Task Manager v1.0.0");
        System.out.println("Use --help for usage information");
    }

    @Command(name = "add", description = "Add a new task")
    public void addTask(
        @Parameters(index = "0", paramLabel = "TITLE") String title,
        @Option(names = {"-d", "--description"}, description = "Task description") String description = "",
        @Option(names = {"-p", "--priority"}, description = "Priority: low, medium, high") String priority = "medium",
        @Option(names = {"-c", "--category"}, description = "Category") String category = null
    ) {
        Priority p = Priority.valueOf(priority.toUpperCase());
        Task task = taskManager.addTask(title, description, p, null, category);
        System.out.println("✓ Task created: " + task.getTitle());
    }

    @Command(name = "list", description = "List all tasks")
    public void listTasks(
        @Option(names = {"-s", "--status"}, description = "Filter by status") String status = null,
        @Option(names = {"-p", "--priority"}, description = "Filter by priority") String priority = null
    ) {
        TaskStatus s = status != null ? TaskStatus.valueOf(status.toUpperCase()) : null;
        Priority p = priority != null ? Priority.valueOf(priority.toUpperCase()) : null;
        List<Task> tasks = taskManager.listTasks(s, p);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task task : tasks) {
            String mark = task.isCompleted() ? "✓" : "○";
            System.out.println(mark + " [" + task.getPriority().getValue() + "] " + task.getTitle());
        }
    }

    @Command(name = "complete", description = "Complete a task")
    public void completeTask(@Parameters(index = "0") long id) {
        if (taskManager.completeTask(id)) {
            System.out.println("✓ Task " + id + " completed!");
        } else {
            System.out.println("✗ Task " + id + " not found!");
        }
    }

    @Command(name = "delete", description = "Delete a task")
    public void deleteTask(@Parameters(index = "0") long id) {
        if (taskManager.deleteTask(id)) {
            System.out.println("✓ Task " + id + " deleted!");
        } else {
            System.out.println("✗ Task " + id + " not found!");
        }
    }

    @Command(name = "stats", description = "Show statistics")
    public void showStats() {
        Map<String, Integer> stats = taskManager.getStats();
        System.out.println("\nTask Statistics");
        System.out.println("Total:     " + stats.get("total"));
        System.out.println("Pending:   " + stats.get("pending"));
        System.out.println("Completed: " + stats.get("completed"));
        System.out.println("High Pri:  " + stats.get("highPriority") + "\n");
    }
}