package oracle.ui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import oracle.Oracle;

/**
 * Controller class for the main chat window of the Oracle application.
 * Handles GUI interactions, including message display, user input, and chat UI layout.
 */
public class MainWindow {
    @FXML
    private VBox dialogContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Oracle oracle;
    private final Image userImage = new Image(getClass().getResourceAsStream("/view/ip-user.jpg"));
    private final Image botImage = new Image(getClass().getResourceAsStream("/view/ip-bot.jpg"));

    /**
     * Initializes the UI components and ensures automatic scrolling for chat.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        showWelcomeMessage();
    }

    /**
     * Sets the Oracle instance for processing user commands and generating responses.
     *
     * @param oracle The Oracle instance to use for command processing.
     */
    public void setOracle(Oracle oracle) {
        this.oracle = oracle;
    }

    /**
     * Displays the welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        String welcomeMessage = "🚀 Greetings, traveler! I am Oracle, your cosmic guide.\n"
                                + "How may I chart your course today?";
        addMessage(welcomeMessage, botImage, "bot");
    }


    /**
     * Handles user input when the "Send" button is clicked or Enter is pressed.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (!input.isBlank()) {
            String response = oracle.getResponse(input);
            addMessage(input, userImage, "user");
            addMessage(response, botImage, "bot");

            userInput.clear();
            if (input.equalsIgnoreCase("bye")) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.exit();
                    }
                }, 5000);
            }
        }
    }

    /**
     * Adds a chat message to the UI with proper text wrapping.
     *
     * @param text          The message text.
     * @param profileImage  The profile image of the sender.
     * @param sender        The sender type ("user" or "bot").
     */
    private void addMessage(String text, Image profileImage, String sender) {
        HBox messageBox = new HBox(10);
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(250);
        messageLabel.setMinHeight(Region.USE_PREF_SIZE);
        messageLabel.setStyle(sender.equals("user")
                ? "-fx-background-color: #007AFF; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 15;"
                : "-fx-background-color: #34C759; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 15;");

        ImageView profileView = new ImageView(profileImage);
        profileView.setFitHeight(40);
        profileView.setFitWidth(40);

        profileView.setClip(new Circle(20, 20, 20));

        if (sender.equals("user")) {
            messageBox.getChildren().addAll(messageLabel, profileView);
            messageBox.setStyle("-fx-alignment: CENTER_RIGHT;");
        } else {
            messageBox.getChildren().addAll(profileView, messageLabel);
            messageBox.setStyle("-fx-alignment: CENTER_LEFT;");
        }

        dialogContainer.getChildren().add(messageBox);
    }
}
