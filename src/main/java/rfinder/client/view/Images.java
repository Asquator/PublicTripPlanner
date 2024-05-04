package rfinder.client.view;

import javafx.scene.image.Image;

import java.util.Objects;

public class Images {

    public static int ICON_SIZE = 30;

    public static Image loadImage(String path) {
        return new Image(Objects.requireNonNull(SimplifiedSolutionExtractor.class.getResourceAsStream(path)), ICON_SIZE, ICON_SIZE, true, false);
    }

    public static Image loadImage(Class classp, String path) {
        return new Image(Objects.requireNonNull(classp.getResourceAsStream(path)), ICON_SIZE, ICON_SIZE, true, false);
    }

    public static Image loadImage(Class classp, String path, int width, int height) {
        return new Image(Objects.requireNonNull(classp.getResourceAsStream(path)), width, height, true, false);
    }
}
