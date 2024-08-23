package controller;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


public class ClientView1Controller extends Thread {
    public TextField txtMessage;
    public VBox messageArea;
    public ImageView imgSendImages;
    public Label lblUser;
    public FileChooser chooser;
    public File path;
    public Pane emojiPane;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String username;

    private PrintWriter printWriter;

    public void initialize() throws IOException {
        emojiPane.setVisible(false);

        lblUser.setText("Client1");

        try {
            socket = new Socket("localhost", 10002);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendOnAction(MouseEvent actionEvent) {
        String message = txtMessage.getText();
        printWriter.println(lblUser.getText() + ": " + message);
        txtMessage.clear();
        printWriter.flush();
        if (message.equalsIgnoreCase("exit")) {
            Stage stage = (Stage) txtMessage.getScene().getWindow();
            stage.close();
        }
    }

    public void sendImageOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        chooser = new FileChooser();
        chooser.setTitle("Open Image");
        this.path = chooser.showOpenDialog(stage);
        printWriter.println(lblUser.getText() + " " + "img" + path.getPath());
        printWriter.flush();

    }

    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                String[] tokens = message.split(" ");
                String command = tokens[0];

                StringBuilder clientMassage = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    clientMassage.append(tokens[i] + " ");
                }

                String[] massageAr = message.split(" ");
                String string = "";
                for (int i = 0; i < massageAr.length - 1; i++) {
                    string += massageAr[i + 1] + " ";
                }

                Text text = new Text(string);
                String fChar = "";

                if (string.length() > 3) {
                    fChar = string.substring(0, 3);
                }

                if (fChar.equalsIgnoreCase("img")) {
                    string = string.substring(3, string.length() - 1);

                    File file = new File(string);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(300);
                    imageView.setPreserveRatio(true);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!command.equalsIgnoreCase(lblUser.getText())) {
                        messageArea.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.setStyle("-fx-background-color: #3C3C3E;");

                        HBox nameTag = new HBox();
                        nameTag.setAlignment(Pos.TOP_LEFT);
                        Text text1 = new Text(command);
                        nameTag.getChildren().add(text1);
                        HBox msgBox = new HBox();
                        msgBox.setAlignment(Pos.BOTTOM_LEFT);
                        msgBox.getChildren().add(imageView);
                        hBox.getChildren().add(msgBox);
                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.setStyle("-fx-background-color: #056162;");
                        HBox nameTag = new HBox();
                        nameTag.setAlignment(Pos.TOP_LEFT);
                        Text text1 = new Text("You");
                        nameTag.getChildren().add(text1);
                        nameTag.setStyle("-fx-background-color: #3C3C3E;");
                        HBox msgBox = new HBox();
                        msgBox.setAlignment(Pos.BOTTOM_LEFT);
                        msgBox.getChildren().add(imageView);
                        hBox.getChildren().add(msgBox);
                    }

                    Platform.runLater(() -> messageArea.getChildren().addAll(hBox));

                } else {
                    TextFlow tempTextFlow = new TextFlow();

                    if (!command.equalsIgnoreCase(lblUser.getText() + ":")) {
                        Text name = new Text(command + " ");
                        name.getStyleClass().add("name");
                        tempTextFlow.getChildren().add(name);
                    }

                    tempTextFlow.getChildren().add(text);
                    tempTextFlow.setMaxWidth(400);

                    TextFlow textFlow = new TextFlow(tempTextFlow);
                    HBox hBox = new HBox(10);

                    if (!command.equalsIgnoreCase(lblUser.getText() + ":")) {
                        messageArea.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(textFlow);
                    } else {
                        Text text1 = new Text("You : " + clientMassage);
                        TextFlow textFlow1 = new TextFlow(text1);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(textFlow1);
                    }
                    Platform.runLater(() -> messageArea.getChildren().addAll(hBox));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openEmojisOnAction(MouseEvent mouseEvent) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    public void emoji1OnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE42");
    }

    public void emoji2OnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE01");
    }
}

