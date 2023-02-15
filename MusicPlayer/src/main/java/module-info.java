module project.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.mediaEmpty;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;

    opens project.musicplayer to javafx.fxml;
    exports project.musicplayer;
}