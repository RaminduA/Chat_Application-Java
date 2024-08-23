package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {
    public TextField txtUsername;
    public TextField txtPassword;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUsername.getText().equals("Client1")){
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ClientView1.fxml"))));
            stage1.setTitle("Let's Chat ");
            stage1.setResizable(false);
            stage1.centerOnScreen();
            stage1.show();
        }

        else if (txtUsername.getText().equals("Client2")) {
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ClientView2.fxml"))));
            stage2.setTitle("Let's Chat");
            stage2.setResizable(false);
            stage2.centerOnScreen();
            stage2.show();
        }
    }
}
