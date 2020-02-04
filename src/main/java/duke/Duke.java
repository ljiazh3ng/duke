package duke;

import duke.command.Command;
import duke.command.CommandResult;
import duke.exception.DukeException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

public class Duke {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private Image user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    /**
     * Empty constructor so that Launcher works.
     */
    public Duke() {
        String home = System.getProperty("user.home");
        Path filePath = java.nio.file.Paths.get(home, "Desktop", "GitHub", "duke","data","data.txt");
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }

    }

    /**
     * Initiate the Duke bot.
     * create UI, duke.storage.Storage, duke.task.TaskList.
     * @param filePath filePath stores the txt file used to load data.
     */
    public Duke(Path filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }


    /**
     * Chat bot function, run infinitely until user types "bye".
     */
    public void runChatBot() {
        this.ui.welcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parseMessage(fullCommand);
                CommandResult commandResult = c.execute(tasks, ui, storage);
                isExit = c.getIsExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            Command c = Parser.parseMessage(input);
            CommandResult commandResult = c.execute(tasks, ui, storage);
            System.out.flush();
            System.setOut(old);
        } catch (DukeException e) {
            ui.showError(e.getMessage());
        }
        return baos.toString();
    }



    /**
     * Iteration 1:
     * Creates a label with the specified text and adds it to the dialog container.
     * @param text String containing text to add
     * @return a label with the specified text that has word wrap enabled.
     */
    private Label getDialogLabel(String text) {
        // You will need to import `javafx.scene.control.Label`.
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);

        return textToAdd;
    }

    /**
     * Prints a logo of the name Duke.
     * @param args Set up main function
     */
    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        Path path = java.nio.file.Paths.get(home, "Desktop", "GitHub", "duke","data","data.txt");
        new Duke(path).runChatBot();
    }

}
