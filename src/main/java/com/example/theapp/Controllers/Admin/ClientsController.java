package com.example.theapp.Controllers.Admin;

import com.example.theapp.Models.Client;
import com.example.theapp.Models.Model;
import com.example.theapp.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initClientsList();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(e -> new ClientCellFactory());
    }

    //if you refresh the case and the getInstance is called, the list wil be appended over and over again with the same data
    private void initClientsList() {
        if(Model.getInstance().getClients().isEmpty()) {
            Model.getInstance().setClients();
        }
    }
}
