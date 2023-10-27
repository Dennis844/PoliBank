module com.example.theapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.theapp to javafx.fxml;
    exports com.example.theapp;
    exports com.example.theapp.Controllers;
    exports com.example.theapp.Controllers.Admin;
    exports com.example.theapp.Controllers.Client;
    exports com.example.theapp.Models;
    exports com.example.theapp.Views;
}