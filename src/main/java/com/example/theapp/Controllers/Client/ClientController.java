package com.example.theapp.Controllers.Client;

import com.example.theapp.Models.Model;
import com.example.theapp.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case ClientMenuOptions.TRANSACTIONS:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
                    break;
                case ClientMenuOptions.ACCOUNTS:client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView());
                    break;
                default:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    break;
            }
        });
    }
}
