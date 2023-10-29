package com.example.theapp.Controllers.Client;

import com.example.theapp.Models.Model;
import com.example.theapp.Models.Transaction;
import com.example.theapp.Views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.controlsfx.control.PropertySheet;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {
    public ListView<Transaction> transactions_listview;
    //public ListView transactions_listview;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllTransactionsList();
        transactions_listview.setItems(Model.getInstance().getAllTransactions());
        transactions_listview.setCellFactory(e ->new TransactionCellFactory());
    }

    private void initAllTransactionsList() {
        if(Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }
    }
}
