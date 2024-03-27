package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.CardSkin;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;

public class Images {
    public static ImageView topRoundImage(double fitWidth, double fitHeight, double radius) {
        return round(fitWidth, fitHeight, radius, radius, 0, 0);
    }

    // https://stackoverflow.com/a/72951931
    public static ImageView round(double width,
                                  double height,
                                  double topLeft,
                                  double topRight,
                                  double bottomRight,
                                  double bottomLeft) {
        topLeft *= 2;
        topRight *= 2;
        bottomRight *= 2;
        bottomLeft *= 2;

        double topEdgeHalf = (width - topLeft - topRight) / 2;
        double rightEdgeHalf = (height - topRight - bottomRight) / 2;
        double bottomEdgeHalf = (width - bottomLeft - bottomRight) / 2;
        double leftEdgeHalf = (height - topLeft - bottomLeft) / 2;

        double topLeftWidth = topEdgeHalf + 2 * topLeft;
        double topLeftHeight = leftEdgeHalf + 2 * topLeft;
        double topLeftX = 0;
        double topLeftY = 0;

        double topRightWidth = topEdgeHalf + 2 * topRight;
        double topRightHeight = rightEdgeHalf + 2 * topRight;
        double topRightX = width - topRightWidth;
        double topRightY = 0;

        double bottomRightWidth = bottomEdgeHalf + 2 * bottomRight;
        double bottomRightHeight = rightEdgeHalf + 2 * bottomRight;
        double bottomRightX = width - bottomRightWidth;
        double bottomRightY = height - bottomRightHeight;

        double bottomLeftWidth = bottomEdgeHalf + 2 * bottomLeft;
        double bottomLeftHeight = leftEdgeHalf + 2 * bottomLeft;
        double bottomLeftX = 0;
        double bottomLeftY = height - bottomLeftHeight;

        Rectangle topLeftRect = new Rectangle(topLeftX, topLeftY, topLeftWidth, topLeftHeight);
        Rectangle topRightRect = new Rectangle(topRightX, topRightY, topRightWidth, topRightHeight);
        Rectangle bottomRightRect = new Rectangle(bottomRightX, bottomRightY, bottomRightWidth, bottomRightHeight);
        Rectangle bottomLeftRect = new Rectangle(bottomLeftX, bottomLeftY, bottomLeftWidth, bottomLeftHeight);

        topLeftRect.setArcWidth(topLeft);
        topLeftRect.setArcHeight(topLeft);
        topRightRect.setArcWidth(topRight);
        topRightRect.setArcHeight(topRight);
        bottomRightRect.setArcWidth(bottomRight);
        bottomRightRect.setArcHeight(bottomRight);
        bottomLeftRect.setArcWidth(bottomLeft);
        bottomLeftRect.setArcHeight(bottomLeft);

        Group clipGroup = new Group(
                topLeftRect,
                topRightRect,
                bottomRightRect,
                bottomLeftRect
        );

        var results = new ImageView();
        results.setClip(clipGroup);
        return results;
    }
}
