package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ModalDialog extends ModalBox {
    private final Card body = new Card();
    private final Tile header = new Tile();

    public ModalDialog(String title, Node content, Node footer, int width, int height) {
        super();

        header.setTitle(title);
        body.setBody(content);
        body.setFooter(footer);
        setMinSize(width, height);
        setMaxSize(width, height);
        super.closeButton.setOnMouseClicked(evt -> ViewHandler.overlay().hide());
        createView();
    }

    public ModalDialog(String title, Node node, int width, int height) {
        super();

        header.setTitle(title);
        body.setBody(node);
        body.setFooter(defaultFooter());
        setMinSize(width, height);
        setMaxSize(width, height);
        super.closeButton.setOnMouseClicked(evt -> ViewHandler.overlay().hide());
        createView();
    }

    private Node defaultFooter() {
        var closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("form-action");
        closeBtn.setCancelButton(true);
        closeBtn.setOnAction(e -> ViewHandler.hideOverlay());

        var footer = new HBox(10, new Spacer(), closeBtn);
        footer.getStyleClass().add("footer");
        footer.setAlignment(Pos.CENTER_RIGHT);
        VBox.setVgrow(footer, Priority.NEVER);
        return footer;
    }

    private void createView() {
        body.setHeader(header);
        body.getStyleClass().add(Tweaks.EDGE_TO_EDGE);

        AnchorPane.setTopAnchor(body, 0d);
        AnchorPane.setRightAnchor(body, 0d);
        AnchorPane.setBottomAnchor(body, 0d);
        AnchorPane.setLeftAnchor(body, 0d);

        addContent(body);
        getStyleClass().add("modal-dialog");
    }
}