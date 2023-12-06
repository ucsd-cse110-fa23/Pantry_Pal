package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Logic Page Window Upon App Start
public class LoginFrame extends BorderPane {

    private Header header;
    private LoginContent loginContent;
    private Button loginButton, createAccountButton, autoLoginButton;
    private HBox autoLoginContainer;

   public LoginFrame() {
        
        this.setPrefSize(370, 120);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            
        header = new Header("PantryPal");
        loginContent = new LoginContent();

        this.setTop(header);
        this.setCenter(loginContent);

        loginButton = loginContent.getLoginButton();
        createAccountButton = loginContent.getCreateAccountButton();
        autoLoginButton = loginContent.getAutoLoginButton();
        autoLoginContainer = loginContent.getAutoLoginContainer();

    }

    public LoginContent getLoginContent() { return loginContent; }

    public Button getAutoLoginButton() { return autoLoginButton; }

    public HBox getAutoLoginContainer() { return autoLoginContainer; }

    public void setLoginButtonAction(EventHandler<ActionEvent> eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public void setCreateAccountButtonAction(EventHandler<ActionEvent> eventHandler) {
        createAccountButton.setOnAction(eventHandler);
    }

    public void setAutoLoginButtonAction(EventHandler<ActionEvent> eventHandler) {
        autoLoginButton.setOnAction(eventHandler);
    }

}