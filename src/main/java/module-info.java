module rfinder {
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires mapsforge.core;
    requires java.prefs;
    requires mapsforge.map.awt;
    requires mapsforge.map.reader;
    requires mapsforge.mapthemes.bridge;
    requires javafx.swing;

    requires jlmap;
    requires org.apache.logging.log4j;
    requires javafx.controls;
    requires org.postgresql.jdbc;
    requires net.postgis.jdbc.geometry;
    requires net.postgis.jdbc;
    requires org.hibernate.orm.core;
    requires spring.context;

    exports view;
    opens view to javafx.fxml;
    opens view.map to javafx.fxml;

    exports rfinder.structures.common;
    exports rfinder.dao;
    exports rfinder.query;
    exports rfinder.query.result;
}