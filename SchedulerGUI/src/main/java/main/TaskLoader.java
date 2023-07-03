package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilites.Priority;
import utilites.Task;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TaskLoader {

    public static ObservableList<Task> loadTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        Task task1 = new Task("oop labos", Priority.HIGH, LocalDate.now(), LocalDate.of(2023, 7, 2), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        Task task2 = new Task("project presentation", Priority.HIGH, LocalDate.now(), LocalDate.of(2023, 7, 5));
        Task task3 = new Task("prepare for exam", Priority.HIGH, LocalDate.now(), LocalDate.of(2023, 8, 15));
        Task task4 = new Task("finish report", Priority.MEDIUM, LocalDate.now(), LocalDate.of(2023, 7, 10));
        Task task5 = new Task("meeting with team", Priority.MEDIUM, LocalDate.now(), LocalDate.of(2023, 7, 7));
        Task task6 = new Task("submit proposal", Priority.MEDIUM, LocalDate.now(), LocalDate.of(2023, 7, 12));
        Task task7 = new Task("complete coding task", Priority.LOW, LocalDate.now(), LocalDate.of(2023, 7, 20));
        tasks.add(task5);
        tasks.add(task7);
        tasks.add(task2);
        tasks.add(task6);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task1);
        Collections.sort(tasks);
        return tasks;
    }
}
