package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Username and Passsword Fields for Login Page
public class LoginContent extends VBox {

    private TextField username;
    private PasswordField password;
    private Button createAccountButton, loginButton, autoLoginButton;
    private HBox autoLoginContainer;

    /**
     * Contains fields to enter crendials and automatic login switch
     */
    LoginContent() {
        
        // Sets the size of LoginContent
        this.setPrefWidth(10);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        // Initializes the TextField Object
        username = new TextField();
        // Initializes the PasswordField Object
        password = new PasswordField();
    
        // Sets the size of the username and password
        username.setMaxSize(400, 60);
        username.setMinSize(200, 40);
        password.setMaxSize(400, 60);
        password.setMinSize(200, 40);

        // Sets the prompt in the username and password fields
        username.setPromptText("Username");
        password.setPromptText("Password");

        // Sets the style of the username and password fields
        username.setStyle("-fx-font: 18 monaco;");
        password.setStyle("-fx-font: 18 monaco;");

        // Initialize the createAccountButton and loginButton
        createAccountButton = new Button("Create Account");
        loginButton = new Button("Login");

        // Initializes the HBox that will hold the Buttons and adds the two buttons
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(loginButton, createAccountButton);

        // Initializes the HBox that will act as a buffer space between two containers
        HBox spaceContainer = new HBox();
        Label spaceLabel = new Label("");
        spaceContainer.getChildren().addAll(spaceLabel);

        Label autoLoginLabel = new Label("Automatic Login: ");
        autoLoginButton = new Button("Loading");

        // Initializes the HBox that will hold the login label
        autoLoginContainer = new HBox();
        autoLoginContainer.setAlignment(Pos.CENTER);
        autoLoginContainer.getChildren().addAll(autoLoginLabel, autoLoginButton);

        this.getChildren().addAll(username, password, buttonContainer, spaceContainer, autoLoginContainer);
        
    }
    
    // Get methods that allow access to the contents of LoginContent
    public TextField getUsername() { return username; }

    public PasswordField getPassword() { return password; }

    public Button getLoginButton() { return loginButton; }

    public Button getCreateAccountButton() { return createAccountButton; }

    public Button getAutoLoginButton() { return autoLoginButton; }

    public HBox getAutoLoginContainer() { return autoLoginContainer; }

}