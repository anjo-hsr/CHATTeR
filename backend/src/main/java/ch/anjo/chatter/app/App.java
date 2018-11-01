package ch.anjo.chatter.app;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    showInputTextDialog(primaryStage, "Enter your E-Mail");
  }

  private void showInputTextDialog(Stage primaryStage, String text) {
    TextInputDialog dialog = new TextInputDialog("Tran");

    dialog.setTitle("E-Mail Address");
    dialog.setHeaderText(text);
    dialog.setContentText("E-Mail:");

    Optional<String> result = dialog.showAndWait();
    System.out.println(result);

    result.ifPresent(
        name -> {
          if (validEmail(name)) {
            Parent root = null;
            try {
              root =
                  FXMLLoader.load(
                      Objects.requireNonNull(
                          getClass().getClassLoader().getResource("sample.fxml")));
            } catch (IOException e) {
              e.printStackTrace();
            }

            GraphicsDevice gd =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();
            primaryStage.setTitle("Block Chat");
            primaryStage.setScene(new Scene(root, width - 50, height - 50));
            primaryStage.show();

            primaryStage.show();
          } else {
            showInputTextDialog(primaryStage, "E-Mail is not valid. Try again!");
          }
        });
  }

  private static boolean validEmail(String email) {
    String emailRegex =
        "^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);
    if (email == null) return false;
    return pat.matcher(email).matches();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
