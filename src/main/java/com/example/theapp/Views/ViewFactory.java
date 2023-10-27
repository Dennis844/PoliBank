package com.example.theapp.Views;

import com.example.theapp.Controllers.Admin.AdminController;
import com.example.theapp.Controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private AccountType loginAccountType;
    //Client Views
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;

    //Admin Views
    private AnchorPane createClientView;

    private AnchorPane clientView;
    private AnchorPane depositView;
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;

    public ViewFactory() {
        this.loginAccountType=AccountType.CLIENT;
        this.clientSelectedMenuItem=new SimpleObjectProperty<>();
        this.adminSelectedMenuItem=new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /* Client views section*/
    public ObjectProperty getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if(dashboardView==null) {
            try {
                dashboardView=new FXMLLoader(getClass().getResource("/FXML/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getTransactionsView() {
        if(transactionsView==null) {
            try {
                transactionsView=new FXMLLoader(getClass().getResource("/FXML/Client/Transactions.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transactionsView;
    }

    public AnchorPane getAccountsView() {
        if(accountsView==null) {
            try {
                accountsView=new FXMLLoader(getClass().getResource("/FXML/Client/Accounts.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accountsView;
    }

    public void showLoginWindow() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        createStage(loader);
    }

    public void showClientWindow() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Client/Client.fxml"));
        ClientController clientController=new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }

    /*Admin */
    public void showAdminWindow() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Admin/Admin.fxml"));
        AdminController controller=new AdminController();
        loader.setController(controller);
        createStage(loader);
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getCreateClientView() {
        if(createClientView==null) try {
            createClientView=new FXMLLoader(getClass().getResource("/FXML/Admin/CreateClient.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createClientView;
    }

    public AnchorPane getClientView() {
        if(clientView==null) try {
            clientView=new FXMLLoader(getClass().getResource("/FXML/Admin/Clients.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientView;
    }

    public AnchorPane getDepositView() {
        if(depositView==null) try {
            depositView=new FXMLLoader(getClass().getResource("/FXML/Admin/Deposit.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return depositView;
    }

    private void createStage(FXMLLoader loader) {
        Scene scene =null;
        try {
            scene=new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icons8-bank-32.png"))));
        stage.setResizable(false);
        stage.setTitle("Poli Bank");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
