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
    requires org.jetbrains.annotations;

    exports rfinder.client.view;
    opens rfinder.client.view to javafx.fxml;
    opens rfinder.client.view.map to javafx.fxml;

    exports rfinder.structures.common;
    exports rfinder.dao;
    exports rfinder.query;
    exports rfinder.query.result;
    exports rfinder.service;
    opens rfinder.service to javafx.fxml;
}