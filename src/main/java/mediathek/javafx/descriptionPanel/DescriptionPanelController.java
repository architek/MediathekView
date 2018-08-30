package mediathek.javafx.descriptionPanel;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mSearch.daten.DatenFilm;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;
import java.util.Optional;

/**
 * The controller for the film description panel
 */
public class DescriptionPanelController {
    @FXML
    private Hyperlink websiteLink;

    @FXML
    private WebView webView;

    @FXML
    private Tab descriptionTab;

    private WebEngine webEngine;
    private EventHandler<Event> closeHandler;

    public void showFilmDescription(@NotNull Optional<DatenFilm> optFilm) {
        optFilm.ifPresentOrElse(film -> {
            websiteLink.setVisible(true);
            websiteLink.setVisited(false);
            websiteLink.setTooltip(new Tooltip(film.getWebsiteLink()));
            websiteLink.setOnAction(e -> {
                try {
                    final Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URI(film.getWebsiteLink()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            webEngine.setUserStyleSheetLocation(getClass().getResource("/mediathek/res/css/description.css").toString());
            webEngine.loadContent(
                    "<html>"
                            + "<span class=\'headline\'>" + (film.getSender().isEmpty() ? "" : film.getSender() + "  -  ")
                            + film.getTitle() + "</span><br/><br/>"
                            + "<span class=\'description\'>" + film.getDescription() + "</span>"
                            + "</html>");
        }, () -> {
            webEngine.loadContent("");
            websiteLink.setTooltip(null);
            websiteLink.setVisible(false);
        });
    }

    public void setOnCloseRequest(EventHandler<Event> e) {
        closeHandler = e;
        descriptionTab.setOnCloseRequest(evt -> closeHandler.handle(evt));
    }

    public void initialize() {
        websiteLink.setVisible(false);
        webView.setContextMenuEnabled(false);

        webEngine = webView.getEngine();
        webEngine.loadContent("<html></html>");
    }
}