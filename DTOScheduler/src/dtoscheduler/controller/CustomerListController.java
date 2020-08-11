/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import dtoscheduler.model.Customer;
import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class CustomerListController implements Initializable
{
    ObservableList<Customer> customers;

    @FXML
    private TableView<Customer> tableCustomers;

    @FXML
    private TableColumn<Customer, String> columnPhone;

    @FXML
    private TableColumn<Customer, String> columnAddress;

    @FXML
    void onAddCustomer(ActionEvent event) throws IOException
    {
        Customer customer = openEditor(event, null);
        Savepoint savepoint = null;
        try
        {
            savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
            customer.add();
            customers.add(customer);
        }
        catch (SQLException ex)
        {
            try
            {
                SQLHelper.getInstance().getConnection().rollback(savepoint);
            }
            catch (SQLException inner)
            {
                Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, inner.getMessage());
            }
            showError(ex);
        }
    }

    @FXML
    void onDeleteCustomer(ActionEvent event)
    {
        Customer customer = tableCustomers.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the customer: " + customer.getCustomerName() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.YES)
        {
            Savepoint savepoint = null;
            try
            {
                savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
                // Delete the customer
                customer.delete();
                customers.remove(customer);
            }
            catch (SQLException ex)
            {
                try
                {
                    SQLHelper.getInstance().getConnection().rollback(savepoint);
                }
                catch (SQLException inner)
                {
                    Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, inner.getMessage());
                }
                showError(ex);
            }
        }
    }

    @FXML
    void onModifyCustomer(ActionEvent event) throws IOException
    {
        Customer original = tableCustomers.getSelectionModel().getSelectedItem();
        Customer customer = openEditor(event, original);

        if (!customer.equals(original))
        {
            Savepoint savepoint = null;
            try
            {
                savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
                customer.update();
                customers.remove(original);
                customers.add(customer);
            }
            catch (SQLException ex)
            {
                try
                {
                    SQLHelper.getInstance().getConnection().rollback(savepoint);
                }
                catch (SQLException inner)
                {
                    Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, inner.getMessage());
                }
                showError(ex);
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Since this data is nested inside another object,
        // and not directly accessible at the Customer level,
        // the cell factory must be set manually
        columnPhone.setCellValueFactory((CellDataFeatures<Customer, String> param) ->
                new SimpleStringProperty(param.getValue().getAddressId().getPhone()));

        columnAddress.setCellValueFactory((CellDataFeatures<Customer, String> param) ->
                new SimpleStringProperty(param.getValue().getAddressId().getAddress()));

        // Read the customers from the database
        try
        {
            customers = SQLHelper.getInstance().getCustomers();
            tableCustomers.setItems(customers);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Customer openEditor(ActionEvent event, Customer customer) throws IOException
    {
        // Show window, wait for user
        FXMLLoader loader = GlobalInformation.loadWindow("view/CustomerEntry.fxml", (Stage) ((Button) event.getSource()).getScene().getWindow());
        Stage stage = (Stage) ((Parent) loader.getRoot()).getScene().getWindow();

        // Get product from window, and add to list
        CustomerEntryController controller = loader.getController();
        controller.setCustomer(customer);
        stage.showAndWait();
        return controller.getCustomer();
    }

    private void showError(Exception ex)
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, ex.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
        alert.showAndWait();
    }
}
