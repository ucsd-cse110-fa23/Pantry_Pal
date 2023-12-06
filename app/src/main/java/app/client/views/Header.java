package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

// App Header that goes on every Window to tell what Page a User is on
public class Header extends HBox {

    Text titleText;

    /**
     * Sets header across all pages based on tite specified
     * 
     * @param text
     */
    public Header(String text) {

        // Sets the size of the Header
        this.setPrefSize(500, 70); // Size of the header
        this.setStyle("-fx-background-color: #9EB8D9;");

        // Initializes the text that is displayed in the header
        titleText = new Text(text); // Text of the Header
        titleText.setStyle("-fx-font: 24 moncao; -fx-text-fill: #FFFFFF;");
        // Adds the text to the Header
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center

    }

}