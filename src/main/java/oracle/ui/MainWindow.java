package oracle.ui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import oracle.Oracle;

/**
 * Controller class for the main chat window of the Oracle application.
 * This class handles the GUI interactions including displaying messages,
 * processing user input, and managing the chat interface.
 */
public class MainWindow {
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Oracle oracle;
    private Image userImage = new Image(getClass().getResourceAsStream("/view/ip-user.jpg"));
    private Image botImage = new Image(getClass().getResourceAsStream("/view/ip-bot.jpg"));

    /**
     * Sets the Oracle instance that will process user commands and generate responses.
     *
     * @param oracle The Oracle instance to be used for processing commands
     */
    public void setOracle(Oracle oracle) {
        this.oracle = oracle;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (!input.isBlank()) {
            String response = oracle.getResponse(input);

            // Add user message
            addMessage(input, userImage, "user");

            // Add bot response
            addMessage(response, botImage, "bot");

            userInput.clear();

            // If command is 'bye', close after 5 seconds
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

    private void addMessage(String text, Image profileImage, String sender) {
        HBox messageBox = new HBox(10);
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
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
