package duke.task;

import duke.exception.DukeException;
import duke.storage.Storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TaskList collections for Task.
 * @author Lua Jia Zheng.
 */
public class TaskList {
    private TreeMap<Integer, Task> tasks;
    public static int index;

    /**
     * Constructor for duke.task.Task object initializing with 2 attribute.
     * Tracks Tasks and completed Tasks.
     */
    public TaskList(TreeMap<Integer, Task> tasks) {
        this.tasks = tasks;
        this.index = tasks.size() + 1;
    }

    /**
     * Create new taskList if there are no saved progress.
     */
    public TaskList() {
        this.tasks = new TreeMap<>();
        this.index = 1;

    }


    /**
     * Mark the tasks that are fulfilled.
     * @param num used to locate the specific tasks.
     */
    public Task doneTask(int num) {
        Task temp = this.tasks.get(num);
        temp.done();
        return temp;
    }

    /**
     * Updates list with the latest stored details in the data.txt file.
     * @param storage storage used to access the data.txt file.
     * @throws DukeException throws exception when there is a error in laoding the file.
     */
    public void updateList(Storage storage) throws DukeException {
        this.tasks = storage.load();
    }

    /**
     * Deletes task from the task List.
     * @param taskIndex taskIndex use to retrieve task.
     * @return returns the deleted duke.task.Task.
     * @throws NullPointerException if taskIndex not available.
     */
    public Task deleteTask(int taskIndex) throws NullPointerException {
        try {
            final Task temp = this.tasks.remove(taskIndex);
            return temp;
        } catch (NullPointerException ex) {
            throw new NullPointerException("Number provided does not exist in the list, please try again.");
        }

    }

    public void addTask(Task task) {
        this.tasks.put(this.index, task);
        this.index++;
    }

    public TreeMap<Integer, Task> getList() {
        return this.tasks;
    }

    /**
     * Find tasks that has the keyword.
     * @param find find is the keyword used to find the tasks.
     * @return returns a Treemap of the tasks found.
     */
    public TreeMap<Integer, Task> findTask(String find) {
        int newIndex = 1;
        TreeMap<Integer, Task> newTasks = new TreeMap<>();
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Task task = entry.getValue();
            String message = task.getTitle();
            if (message.contains(find)) {
                newTasks.put(newIndex, task);
                newIndex++;
            }
        }
        return newTasks;
    }

    /**
     * Sorts the existing taskList by type of event followed by title.
     */
    public void sortTask() {
        List<Task> sortTask = new ArrayList<>();
        TreeMap<Integer, Task> newTasks = new TreeMap<>();
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Task task = entry.getValue();
            sortTask.add(task);
        }
        Collections.sort(sortTask);
        int counter = 1;
        for (Task taskObject: sortTask) {
            newTasks.put(counter, taskObject);
            counter++;
        }

        this.tasks = newTasks;
    }

}
