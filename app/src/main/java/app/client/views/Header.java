package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

// App Header that goes on every Window to tell what Page a User is on
public class Header extends HBox {

    Text titleText;

    public Header(String text) {

        this.setPrefSize(500, 70); // Size of the header
        this.setStyle("-fx-background-color: #9EB8D9;");

        titleText = new Text(text); // Text of the Header
        titleText.setStyle("-fx-font: 24 moncao; -fx-text-fill: #FFFFFF;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center

    }

}