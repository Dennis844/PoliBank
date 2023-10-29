package com.example.theapp.Models;

import com.example.theapp.Views.AccountType;
import com.example.theapp.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Model {
    private static  Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    // Client Data Section
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    //Admin Data Section
    private boolean adminLoginSuccessFlag=false;
    private final ObservableList<Client> clients;

    private Model() {

        this.viewFactory=new ViewFactory();
        this.databaseDriver=new DatabaseDriver();
        //Client Data Section
        this.clientLoginSuccessFlag=false;
        this.client=new Client("", "", "", null, null, null);
        this.latestTransactions=FXCollections.observableArrayList();
        this.allTransactions=FXCollections.observableArrayList();
        //Admin data section
        this.adminLoginSuccessFlag=false;
        this.clients= FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance() {
        if(model == null) {
            model=new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}


    /*
    * Client Method Section
    * */
    public boolean getClientLoginSuccessFlag() {return this.clientLoginSuccessFlag;}
    public void setClientLoginSuccessFlag(boolean flag) {this.clientLoginSuccessFlag=flag;}

    public Client getClient() {
        return client;
    }

    public void evaluateClientCred(String pAdress, String password) {
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet= databaseDriver.getClientData(pAdress, password);
        try {
            if(resultSet.isBeforeFirst()) {
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.pAdressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dateParts=resultSet.getString("Date").split("-");
                LocalDate date=LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateProperty().set(date);
                checkingAccount=getCheckingAccount(pAdress);
                savingAccount=getSavingsAccount(pAdress);
                this.client.cAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingAccount);
                this.clientLoginSuccessFlag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit) {
        ResultSet resultSet=databaseDriver.getTransactions(this.client.pAdressProperty().get(), limit);
        try {
            while (resultSet.next()) {
                String sender=resultSet.getString("Sender");
                String receiver=resultSet.getString("Receiver");
                double amount=resultSet.getDouble("Amount");
                String[] dateParts=resultSet.getString("Date").split("-");
                LocalDate date=LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String message=resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLatestTransactions() {
        prepareTransactions(this.latestTransactions, 4);
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    public void setAllTransactions() {
        //limita de -1 returneaza toate tranzactiile din baza de date
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    /*
    * Admin method Section
    * */

    public boolean getAdminLoginSuccessFlag() {return this.adminLoginSuccessFlag;}

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public void evaluateAdminCredentials(String username, String password) {
        ResultSet resultSet= getDatabaseDriver().getAdminData(username, password);
        try {
            if(resultSet.isBeforeFirst()) {
                this.adminLoginSuccessFlag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients() {
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet= databaseDriver.getAllClientsData();
        try {
            while (resultSet.next()) {
                String fName=resultSet.getString("FirstName");
                String lName=resultSet.getString("LastName");
                String pAddress=resultSet.getString("PayeeAddress");
                String[] dateParts=resultSet.getString("Date").split("-");
                LocalDate date=LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                checkingAccount=getCheckingAccount(pAddress);
                savingAccount=getSavingsAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingAccount, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> searchClient(String pAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            CheckingAccount checkingAccount=getCheckingAccount(pAddress);
            SavingAccount savingAccount=getSavingsAccount(pAddress);
            String fName=resultSet.getString("FirstName");
            String lName=resultSet.getString("LastName");
            String[] dateParts=resultSet.getString("Date").split("-");
            LocalDate date=LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingAccount, date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    /*
    * Utility method section
    * */
    public CheckingAccount getCheckingAccount(String pAddress) {
        CheckingAccount account=null;
        ResultSet resultSet= databaseDriver.getCheckingAccountData(pAddress);
        try {
            String num=resultSet.getString("AccountNumber");
            int tLimit=(int) resultSet.getDouble("TransactionLimit");
            double balance=resultSet.getDouble("Balance");
            account=new CheckingAccount(pAddress, num, balance, tLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public SavingAccount getSavingsAccount(String pAddress) {
        SavingAccount account=null;
        ResultSet resultSet= databaseDriver.getSavingsAccountData(pAddress);
        try {
            String num=resultSet.getString("AccountNumber");
            double wLimit=resultSet.getDouble("WithdrawalLimit");
            double balance=resultSet.getDouble("Balance");
            account=new SavingAccount(pAddress, num, balance, wLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }


}
