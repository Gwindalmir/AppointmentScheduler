/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import dtoscheduler.model.*;
import dtoscheduler.controls.TimeSpinner;
import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class AppointmentEntryController implements Initializable
{
    private Appointment appointment;

    @FXML
    private TextField textTitle;

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private ComboBox<Customer> comboCustomer;

    @FXML
    private DatePicker dateStart;

    @FXML
    private TimeSpinner timeStart;

    @FXML
    private DatePicker dateEnd;

    @FXML
    private TimeSpinner timeEnd;

    @FXML
    private ComboBox<User> comboUser;

    @FXML
    private TextField textURL;

    @FXML
    private TextArea textLocation;

    @FXML
    private TextArea textDescription;

    @FXML
    private TextArea textContact;

    @FXML
    void onCancel(ActionEvent event)
    {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onSave(ActionEvent event)
    {
        ZonedDateTime start = ZonedDateTime.of(dateStart.getValue(), timeStart.getValue(), ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(dateEnd.getValue(), timeEnd.getValue(), ZoneId.systemDefault());

        try
        {
            // Do data validation
            // This satisfies Rubric Task F.
            List<SimpleImmutableEntry<ZonedDateTime, ZonedDateTime>> appointmentTimes = getAppointmentTimes();
            Optional<?> found = appointmentTimes.stream().filter(a ->
                    (start.isAfter(a.getKey()) && start.isBefore(a.getValue())) ||
                    (end.isAfter(a.getKey()) && end.isBefore(a.getValue()))
            ).findAny();

            if (found.isPresent())
            {
                new Alert(Alert.AlertType.ERROR, "There is already an appointment at the scheduled time, please correct.").showAndWait();
                return;
            }

            // Now check for outside business hours
            // Note, for this, business hours are simply defined as 7am-7pm weekdays, for the user's time zone.
            ZonedDateTime dayStart = start.truncatedTo(ChronoUnit.HOURS).withHour(7);
            ZonedDateTime dayEnd = start.truncatedTo(ChronoUnit.HOURS).withHour(19);

            if (start.isBefore(dayStart) || start.isAfter(dayEnd) || end.isBefore(dayStart) || end.isAfter(dayEnd))
            {
                new Alert(Alert.AlertType.ERROR, "Appointments cannot be scheduled outside of business hours (7a-7p) local time.").showAndWait();
                return;
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (appointment == null)
            appointment = new Appointment();

        appointment.setTitle(textTitle.getText());
        appointment.setCustomerId(comboCustomer.getSelectionModel().getSelectedItem());
        appointment.setUserId(comboUser.getSelectionModel().getSelectedItem());
        appointment.setStart(start);
        appointment.setEnd(end);
        appointment.setType(comboType.getSelectionModel().getSelectedItem());
        appointment.setDescription(textDescription.getText());
        appointment.setContact(textContact.getText());
        appointment.setLocation(textLocation.getText());
        appointment.setUrl(textURL.getText());

        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        comboCustomer.setCellFactory((ListView<Customer> param) -> new ComboBoxListCell<Customer>()
        {
            @Override
            public void updateItem(Customer item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null)
                    setText(item.getCustomerName());
                else
                    setText(null);
            }
        });

        comboCustomer.setConverter(new StringConverter<Customer>()
        {
            @Override
            public String toString(Customer object)
            {
                return object.getCustomerName();
            }

            @Override
            public Customer fromString(String string)
            {
                return comboCustomer.getSelectionModel().getSelectedItem();
            }
        });

        comboUser.setCellFactory((ListView<User> param) -> new ComboBoxListCell<User>()
        {
            @Override
            public void updateItem(User item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null)
                    setText(item.getUserName());
                else
                    setText(null);
            }
        });

        comboUser.setConverter(new StringConverter<User>()
        {
            @Override
            public String toString(User object)
            {
                return object.getUserName();
            }

            @Override
            public User fromString(String string)
            {
                return comboUser.getSelectionModel().getSelectedItem();
            }
        });

        try
        {
            // Fill combo boxes
            ObservableList<Customer> customers = SQLHelper.getInstance().getCustomers();
            comboCustomer.setItems(customers);

            ObservableList<User> users = SQLHelper.getInstance().getUsers();
            comboUser.setItems(users);

            ObservableList<String> types = SQLHelper.getInstance().getAppointmentTypes();
            comboType.setItems(types);

            for (User user : users)
            {
                if (user.getUserName().equals(GlobalInformation.getInstance().getUser()))
                    comboUser.setValue(user);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Fill default values
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(1);
        dateStart.setValue(now.toLocalDate());
        timeStart.getValueFactory().setValue(now.toLocalTime());

        dateEnd.setValue(now.plusHours(1).toLocalDate());
        timeEnd.getValueFactory().setValue(now.plusHours(1).toLocalTime());
    }

    public void setAppointment(Appointment appointment)
    {
        if (appointment != null)
        {
            this.appointment = new Appointment(appointment);
            textTitle.setText(appointment.getTitle());
            textDescription.setText(appointment.getDescription());
            comboType.setValue(appointment.getType());
            textLocation.setText(appointment.getLocation());
            textContact.setText(appointment.getContact());
            textURL.setText(appointment.getUrl());
            comboCustomer.setValue(appointment.getCustomerId());
            comboUser.setValue(appointment.getUserId());
            dateStart.setValue(appointment.getStart().toLocalDate());
            timeStart.getValueFactory().setValue(appointment.getStart().toLocalTime());
            dateEnd.setValue(appointment.getEnd().toLocalDate());
            timeEnd.getValueFactory().setValue(appointment.getEnd().toLocalTime());
        }
    }

    public Appointment getAppointment()
    {
        return appointment;
    }

    private List<SimpleImmutableEntry<ZonedDateTime, ZonedDateTime>> getAppointmentTimes() throws SQLException
    {
        return getAppointmentTimes(GlobalInformation.getInstance().getUser());
    }

    private List<SimpleImmutableEntry<ZonedDateTime, ZonedDateTime>> getAppointmentTimes(String user) throws SQLException
    {
        List<SimpleImmutableEntry<ZonedDateTime, ZonedDateTime>> times = new ArrayList<>();

        List<Appointment> appointments = SQLHelper.getInstance().getAppointments();

        for (Appointment appointment : appointments)
        {
            times.add(new SimpleImmutableEntry<>(appointment.getStart(), appointment.getEnd()));
        }

        return times;
    }
}
