package app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.*;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LoginContent extends VBox {

    private TextField username;
    private PasswordField password;
    private Label userLabel, passLabel;
    private Button createAccountButton, loginButton;

    public LoginContent() {

        this.setPrefWidth(10);

        username = new TextField();
        password = new PasswordField();

        userLabel = new Label("Username");
        userLabel.setTextAlignment(TextAlignment.CENTER);

        passLabel = new Label("Password");
        passLabel.setTextAlignment(TextAlignment.CENTER);

        createAccountButton = new Button("Create Account");
        loginButton = new Button("Login");

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(loginButton, createAccountButton);

        this.getChildren().addAll(userLabel, username, passLabel,password, buttonContainer);

    }
    
    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

}