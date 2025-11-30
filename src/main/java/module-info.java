module fr.cda.findboat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires org.jsoup;
    requires java.sql;
    requires mysql.connector.j;

    exports fr.cda.findboat;
    exports fr.cda.findboat.controller;
    exports fr.cda.findboat.view;
    exports fr.cda.findboat.factory;
    exports fr.cda.findboat.model;
    opens fr.cda.findboat.view to  javafx.fxml;

}