package ku.cs.services;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;
import com.github.saacsos.FXRouter;
import java.io.IOException;

public class Transitional {
    private Node node;

    public Transitional(Node node){
        this.node=node;
    }

    public void fadeIn(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5),node);
        fadeTransition.setFromValue(0.45);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

   /* public <T> void fadeOut(String fxml,T value){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1),node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo(fxml,value);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า"+fxml+" ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            }
        });
        fadeTransition.play();
    }
    public <T> void fadeOut(String fxml){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1),node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo(fxml);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า"+fxml+" ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            }
        });
        fadeTransition.play();
    }*/

}
