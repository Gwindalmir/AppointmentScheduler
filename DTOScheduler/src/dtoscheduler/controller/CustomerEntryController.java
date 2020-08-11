/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import dtoscheduler.model.Address;
import dtoscheduler.model.Customer;
import dtoscheduler.model.City;
import dtoscheduler.model.Country;
import dtoscheduler.singleton.SQLHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class CustomerEntryController implements Initializable
{
    Customer customer;

    @FXML
    private TextField textName;

    @FXML
    private TextField textPhone;

    @FXML
    private TextField textStreet;

    @FXML
    private TextField textStreet2;

    @FXML
    private ComboBox<Country> comboCountry;

    @FXML
    private ComboBox<City> comboCity;

    @FXML
    private TextField textPostal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        comboCity.setCellFactory((ListView<City> param) -> new ComboBoxListCell<City>()
        {
            @Override
            public void updateItem(City item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null)
                    setText(item.getCity());
                else
                    setText(null);
            }
        });

        comboCity.setConverter(new StringConverter<City>()
        {
            @Override
            public String toString(City object)
            {
                return object != null ? object.getCity() : null;
            }

            @Override
            public City fromString(String string)
            {
                City ret = new City();
                ret.setCity(string);

                for (City city : comboCity.getItems())
                {
                    if (city.getCity().equalsIgnoreCase(string))
                        ret = city;
                }
                if (ret.getCityId() == 0)
                {
                    if (!ret.lookup())
                    {
                        ret.setCountryId(comboCountry.getSelectionModel().getSelectedItem());
                    }
                }
                return ret;
            }
        });

        comboCountry.setCellFactory((ListView<Country> param) -> new ComboBoxListCell<Country>()
        {
            @Override
            public void updateItem(Country item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null)
                    setText(item.getCountry());
                else
                    setText(null);
            }
        });

        comboCountry.setConverter(new StringConverter<Country>()
        {
            @Override
            public String toString(Country object)
            {
                return object != null ? object.getCountry() : null;
            }

            @Override
            public Country fromString(String string)
            {
                Country ret = new Country();
                ret.setCountry(string);

                for (Country country : comboCountry.getItems())
                {
                    if (country.getCountry().equalsIgnoreCase(string))
                        ret = country;
                }
                if (ret.getCountryId() == 0)
                {
                    ret.lookup();
                }
                return ret;
            }
        });

        try
        {
            // Fill combo boxes
            ObservableList<City> cities = SQLHelper.getInstance().getCities();
            comboCity.setItems(cities);

            ObservableList<Country> countries = SQLHelper.getInstance().getCountries();
            comboCountry.setItems(countries);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void onCancel(ActionEvent event)
    {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onSave(ActionEvent event)
    {
        // Validate
        // Satisfies 1/4 of Rubric task F
        if(textName.getText() == null || textName.getText().isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "A customer name is required.", ButtonType.OK).showAndWait();
            return;
        }
        
        // Allow Address2 to be blank
        if(textStreet.getText() == null || textStreet.getText().isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "An address is required.", ButtonType.OK).showAndWait();
            return;
        }
        
        if(textPhone.getText() == null || textPhone.getText().isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "A phone number is required.", ButtonType.OK).showAndWait();
            return;
        }

        if(textPostal.getText() == null || textPostal.getText().isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "A postal/zip code is required.", ButtonType.OK).showAndWait();
            return;
        }

        if(comboCity.getEditor().getText() == null || comboCity.getEditor().getText().isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "A city is required.", ButtonType.OK).showAndWait();
            return;
        }

        if(comboCountry.getSelectionModel().getSelectedItem() == null)
        {
            new Alert(Alert.AlertType.ERROR, "A country is required.", ButtonType.OK).showAndWait();
            return;
        }

        // Build up customer class with the validated data
        if (customer == null)
            customer = new Customer();

        if (customer.getAddressId() == null)
            customer.setAddressId(new Address());

        if (customer.getAddressId().getCityId() == null)
            customer.getAddressId().setCityId(comboCity.getSelectionModel().getSelectedItem());

        if (customer.getAddressId().getCityId().getCountryId() == null)
            customer.getAddressId().getCityId().setCountryId(comboCountry.getSelectionModel().getSelectedItem());

        City newCity = customer.getAddressId().getCityId();
        newCity.setCity(comboCity.getEditor().getText());
        newCity.setCountryId(comboCountry.getSelectionModel().getSelectedItem());

        Address newAddress = customer.getAddressId();
        newAddress.setAddress(textStreet.getText());
        newAddress.setAddress2(textStreet2.getText());
        newAddress.setPhone(textPhone.getText());
        newAddress.setPostalCode(textPostal.getText());
        newAddress.setCityId(newCity);

        customer.setCustomerName(textName.getText());
        customer.setAddressId(newAddress);

        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    public void setCustomer(Customer customer)
    {
        this.customer = new Customer(customer);

        textName.setText(this.customer.getCustomerName());
        if(this.customer.getAddressId() != null)
        {
            textPhone.setText(this.customer.getAddressId().getPhone());
            textPostal.setText(this.customer.getAddressId().getPostalCode());
            textStreet.setText(this.customer.getAddressId().getAddress());
            textStreet2.setText(this.customer.getAddressId().getAddress2());
            comboCity.setValue(this.customer.getAddressId().getCityId());
            
            if(this.customer.getAddressId().getCityId() != null)
                comboCountry.setValue(this.customer.getAddressId().getCityId().getCountryId());
        }
    }

    public Customer getCustomer()
    {
        return customer;
    }
}
