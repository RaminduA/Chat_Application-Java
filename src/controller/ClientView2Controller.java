package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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

public class ClientView2Controller extends Thread {
    public Label lblUser;
    public TextField txtMessage;
    public VBox messageArea;
    public ImageView imgSendImages;
    public Pane emojiPane;
    public FileChooser chooser;
    private Socket accept;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private PrintWriter printWriter;
    private File path;

    public void initialize() throws IOException {
        emojiPane.setVisible(false);

        lblUser.setText("Client2");
        try {
            accept = new Socket("localhost", 10002);
            bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            printWriter = new PrintWriter(accept.getOutputStream(), true);
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendOnAction(ActionEvent actionEvent) {
        String massage = txtMessage.getText();
        printWriter.println(lblUser.getText() + ": " + massage);
        txtMessage.clear();
        printWriter.flush();
        if (massage.equalsIgnoreCase("exit")) {
            Stage stage = (Stage) txtMessage.getScene().getWindow();
            stage.close();
        }

    }

    public void run() {
        try {
            while (true) {
                String massage = bufferedReader.readLine();
                String[] tokens = massage.split(" ");
                String command = tokens[0];

                StringBuilder clientMassage = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    clientMassage.append(tokens[i] + " ");
                }

                String[] massageAr = massage.split(" ");
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
                    imageView.setFitHeight(300);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!command.equalsIgnoreCase(lblUser.getText())) {
                        messageArea.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text("  " + command + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);
                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        Text text1 = new Text("You : ");
                        hBox.getChildren().add(imageView);
                        hBox.getChildren().add(text1);
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

    public void sendImageOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        chooser = new FileChooser();
        chooser.setTitle("Open Image");
        this.path = chooser.showOpenDialog(stage);
        printWriter.println(lblUser.getText() + " " + "img" + path.getPath());
        printWriter.flush();
    }

    public void openEmojisOnAction(MouseEvent mouseEvent) {
        if (!emojiPane.isVisible()) {
            emojiPane.setVisible(true);
        } else {
            emojiPane.setVisible(false);
        }
    }

    public void emoji1OnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE42");
    }

    public void emoji2OnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE01");
    }
}

