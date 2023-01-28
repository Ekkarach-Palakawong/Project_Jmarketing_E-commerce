package ku.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import com.github.saacsos.FXRouter;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "The Layer", 940, 600);
        stage.getIcons().add(new Image("file:images/logo/top-left-logo.png"));
        configRoute();
        FXRouter.goTo("home");
    }

    private static void configRoute() {
        String packageStr = "ku/cs/";
        FXRouter.when("home", packageStr+"home.fxml");
        FXRouter.when("creators", packageStr+"creators.fxml");
        FXRouter.when("register", packageStr+"register.fxml");
        FXRouter.when("login", packageStr+"login.fxml");
        FXRouter.when("introduction", packageStr+"introduction.fxml");
        FXRouter.when("store", packageStr+"store.fxml");
        FXRouter.when("checkout", packageStr+"checkout.fxml");
        FXRouter.when("pay", packageStr+"pay.fxml");
        FXRouter.when("admin", packageStr+"admin.fxml");
        FXRouter.when("usermanage", packageStr+"usermanage.fxml");
        FXRouter.when("profile", packageStr+"profile.fxml");
        FXRouter.when("addproduct",packageStr+"addproduct.fxml");
        FXRouter.when("editproduct",packageStr+"editproduct.fxml");
        FXRouter.when("openshop",packageStr+"openshop.fxml");
        FXRouter.when("reported",packageStr+"reporteduser.fxml");
        FXRouter.when("producttype",packageStr+"producttype.fxml");
        FXRouter.when("resetpassword",packageStr+"resetpassword.fxml");
        FXRouter.when("userstore",packageStr+"userstore.fxml");

    }
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}