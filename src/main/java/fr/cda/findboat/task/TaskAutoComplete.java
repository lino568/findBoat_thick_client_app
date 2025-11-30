package fr.cda.findboat.task;

import fr.cda.findboat.api.AutoCompleteAPI;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskAutoComplete implements Callable<List<String>> {

    private static Logger log = LoggerFactory.getLogger(TaskAutoComplete.class);
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Future<List<String>> currentTask = null;
    private String cityValue;

    public TaskAutoComplete(String cityValue) {
        this.cityValue = cityValue;
    }

    @Override
    public List<String> call() throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            return List.of();
        }
        return AutoCompleteAPI.getCities(this.cityValue);
    }

    public static void executeSearch(String cityValue, Consumer<List<String>> onResult) {
        // Annuler la tâche précédente si elle existe
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }

        // Lancer la nouvelle tâche
        TaskAutoComplete task = new TaskAutoComplete(cityValue);
        currentTask = executor.submit(task);
        new Thread(() -> {
            try {
                List<String> cities = currentTask.get();
                if (!currentTask.isCancelled() && cities != null) {
                    Platform.runLater(() -> onResult.accept(cities));
                }
            } catch (CancellationException e) {

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.info(e.getCause().toString());
                Platform.runLater(() -> onResult.accept(List.of()));
            }
        }).start();
    }

    public static void shutdown() {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }


}
