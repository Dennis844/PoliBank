package com.example.theapp.Controllers.Client;

import com.example.theapp.Models.Model;
import com.example.theapp.Models.Transaction;
import com.example.theapp.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.controlsfx.control.PropertySheet;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label checking_balance;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
    public TextField payee_flb;
    public TextField amount_lfb;
    public TextArea message_flb;
    public Button send_btn;
    public Text user_name;
    public Label login_date;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        initLastesTransactionsList();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e->new TransactionCellFactory());
        send_btn.setOnAction(actionEvent -> onSendMoney());
        accountSummary();
    }

    private void bindData() {
        user_name.textProperty().bind(Bindings.concat("Buna ").concat(Model.getInstance().getClient().firstNameProperty()));
        login_date.setText("Astazi, "+ LocalDate.now());
        checking_balance.textProperty().bind(Model.getInstance().getClient().cAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().cAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        savings_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());

    }


    //Nu vrem ca lista sa fie repetata de mai multe ori, si de aceea folosim acel if
    private void initLastesTransactionsList() {
        if(Model.getInstance().getLatestTransactions().isEmpty()) {
            Model.getInstance().setLatestTransactions();
        }
    }

    private void onSendMoney() {
        String receiver = payee_flb.getText();
        String amountText = amount_lfb.getText().trim(); // Trim any extra spaces
        if (!amountText.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountText);
                String message = message_flb.getText();
                String sender = Model.getInstance().getClient().pAdressProperty().get();
                ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
                if (resultSet.isBeforeFirst()) {
                    Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
                }
                Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
                Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
                Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);
                // Clear the fields
                payee_flb.setText("");
                amount_lfb.setText("");
                message_flb.setText("");
            } catch (NumberFormatException e) {
                // Handle exception if the entered amount is not a valid number
                // For example: display a message to the user indicating an invalid input
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case when the amount field is empty
            // For example: display a message asking the user to input an amount
        }
    }

    //Metoda calculeaza toate cheltuielile si incasarile
    private void accountSummary() {
        double income=0;
        double expenses=0;
        if(Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }
        for(Transaction transaction :Model.getInstance().getAllTransactions()) {
            if(transaction.senderProperty().get().equals(Model.getInstance().getClient().pAdressProperty().get())) {
                expenses=expenses+transaction.amountProperty().get();
            } else {
                income=income+transaction.amountProperty().get();
            }
        }
        income_lbl.setText("+ $"+ income);
        expense_lbl.setText("- $"+expenses);
    }

}
