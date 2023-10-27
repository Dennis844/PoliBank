package com.example.theapp.Controllers.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
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

    }
}
