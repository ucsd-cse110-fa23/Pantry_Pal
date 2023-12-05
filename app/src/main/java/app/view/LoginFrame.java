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

public class LoginFrame extends BorderPane {

    private Header header;
    private LoginContent loginContent;
    private Button loginButton, createAccountButton;

   public LoginFrame() {
        
        this.setPrefSize(370, 120);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            
        header = new Header("PantryPal Login");
        loginContent = new LoginContent();

        this.setTop(header);
        this.setCenter(loginContent);

        loginButton = loginContent.getLoginButton();
        createAccountButton = loginContent.getCreateAccountButton();

    }

    public LoginContent getLoginContent() {
        return loginContent;
    }

    public void setLoginButtonAction(EventHandler<ActionEvent> event) {
        loginButton.setOnAction(event);
    }

    public void setCreateAccountButtonAction(EventHandler<ActionEvent> event) {
        createAccountButton.setOnAction(event);
    }

}