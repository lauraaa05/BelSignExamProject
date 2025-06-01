package gui.controller;

import be.Picture;
import gui.controllers.QCUDoneReportController;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

    class QCUDoneReportControllerTest {

        @Test
        void testCreateImageCard() {
            QCUDoneReportController controller = new QCUDoneReportController();

            Picture pic = new Picture();
            pic.setImage(new byte[] { /* image bytes here */ });
            pic.setSide("Left");
            pic.setTimestamp(LocalDateTime.of(2025, 6, 1, 12, 0));

            VBox imageCard = controller.createImageCard(pic);

            assertNotNull(imageCard);
            assertTrue(imageCard.getChildren().stream().anyMatch(node -> node instanceof ImageView));
            assertTrue(imageCard.getChildren().stream().anyMatch(node -> node instanceof Label));
        }
    }

