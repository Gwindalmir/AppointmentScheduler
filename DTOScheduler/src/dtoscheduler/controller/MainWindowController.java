/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import dtoscheduler.interfaces.IDatabaseReportEntity;
import dtoscheduler.model.Appointment;
import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class MainWindowController implements Initializable
{
    // This is used to hold all the appointments,
    // which will be used for adding, editing, and deleting appointments.
    // Based on code below, this satisifies Rubric Task C.
    ObservableList<Appointment> appointments;

    boolean monthView = true;

    @FXML
    private RadioButton radioMonth;

    @FXML
    private TableView<Appointment> tableCalendar;

    @FXML
    private TableColumn<Appointment, String> columnCustomer;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> columnStartTime;

    @FXML
    private TableColumn<Appointment, Duration> columnDuration;

    @FXML
    private TableColumn<Appointment, String> columnConsultant;

    @FXML
    private DatePicker dateStart;

    @FXML
    void onManageCustomers(ActionEvent event) throws IOException
    {
        // Show window, wait for user
        FXMLLoader loader = GlobalInformation.loadWindow("view/CustomerList.fxml", (Stage) ((Button) event.getSource()).getScene().getWindow());
        Stage stage = (Stage) ((Parent) loader.getRoot()).getScene().getWindow();
        stage.showAndWait();

        // Once the window closes, refresh the appointments
        for (Appointment appointment : appointments)
        {
            appointment.getCustomerId().lookup();
        }
        // Refresh the visuals
        tableCalendar.refresh();
    }

    @FXML
    void onSwitchCalender(ActionEvent event)
    {
        monthView = radioMonth.isSelected();

        if (monthView)
            dateStart.setValue(LocalDate.now().withDayOfMonth(1));
        else
            dateStart.setValue(LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1));
    }

    @FXML
    void onAddAppointment(ActionEvent event) throws IOException, SQLException
    {
        // Show window, wait for user
        FXMLLoader loader = GlobalInformation.loadWindow("view/AppointmentEntry.fxml", (Stage) ((Button) event.getSource()).getScene().getWindow());
        Stage stage = (Stage) ((Parent) loader.getRoot()).getScene().getWindow();

        AppointmentEntryController controller = loader.getController();
        stage.showAndWait();

        Appointment appointment = controller.getAppointment();
        if (appointment != null)
        {
            Savepoint savepoint = null;
            try
            {
                savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
                appointment.add();
                appointments.add(appointment);
            }
            catch (SQLException ex)
            {
                SQLHelper.getInstance().getConnection().rollback(savepoint);
                Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
                showError(ex);
            }
        }
    }

    @FXML
    void onDeleteAppointment(ActionEvent event)
    {
        Appointment appointment = tableCalendar.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the appointment: " + appointment.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.YES)
        {
            Savepoint savepoint = null;
            try
            {
                savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
                // Remove the appointment from the database
                appointment.delete();
                appointments.remove(appointment);
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
                Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void onEditAppointment(ActionEvent event) throws IOException
    {
        Appointment original = tableCalendar.getSelectionModel().getSelectedItem();

        // Show window, wait for user
        FXMLLoader loader = GlobalInformation.loadWindow("view/AppointmentEntry.fxml", (Stage) ((Button) event.getSource()).getScene().getWindow());
        Stage stage = (Stage) ((Parent) loader.getRoot()).getScene().getWindow();

        AppointmentEntryController controller = loader.getController();
        controller.setAppointment(original);
        stage.showAndWait();

        Appointment appointment = controller.getAppointment();
        if (!appointment.equals(original))
        {
            Savepoint savepoint = null;
            try
            {
                savepoint = SQLHelper.getInstance().getConnection().setSavepoint();
                appointment.update();
                appointments.set(appointments.indexOf(original), appointment);
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
                Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void onSave(ActionEvent event)
    {
        SQLHelper.getInstance().saveChanges();
    }

    @FXML
    void onDiscard(ActionEvent event)
    {
        SQLHelper.getInstance().undoChanges();
    }

    @FXML
    void onReportSchedule(ActionEvent event) throws IOException
    {
        try
        {
            showReport(event, SQLHelper.getInstance().getReportConsultantSchedule(), "Schedule for Consultants");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void onReportTotalTime(ActionEvent event) throws IOException
    {
        try
        {
            showReport(event, SQLHelper.getInstance().getReportTotalTimeByConsultant(), "Total Time Spent Per Consultant");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void onReportTypes(ActionEvent event) throws IOException
    {
        try
        {
            showReport(event, SQLHelper.getInstance().getReportTypePerMonth(), "Appointment Types By Month");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Set up table columns
        // Lambdas are used here to simplify the code, and make debugging more efficient.
        // Satisfies Rubric Task G.
        columnCustomer.setCellValueFactory((TableColumn.CellDataFeatures<Appointment, String> param) ->
                new SimpleStringProperty(param.getValue().getCustomerId().getCustomerName()));
        columnStartTime.setCellFactory((param) -> new TableCell<Appointment, ZonedDateTime>()
        {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);

            @Override
            protected void updateItem(ZonedDateTime item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(item.format(formatter));
            }
        });

        columnDuration.setCellValueFactory((TableColumn.CellDataFeatures<Appointment, Duration> param) ->
                new SimpleObjectProperty(Duration.between(param.getValue().getStart(), param.getValue().getEnd())));
        columnDuration.setCellFactory((param) -> new TableCell<Appointment, Duration>()
        {
            @Override
            protected void updateItem(Duration item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setText(null);
                }
                else
                {
                    long totalSeconds = item.getSeconds();
                    long hours = totalSeconds / 3600;
                    long minutes = (totalSeconds % 3600) / 60;

                    if (hours > 0)
                        setText(String.format("%d h %d m", hours, minutes));
                    else
                        setText(String.format("%d m", minutes));
                }
            }
        });

        columnConsultant.setCellValueFactory((TableColumn.CellDataFeatures<Appointment, String> param) ->
                new SimpleStringProperty(param.getValue().getUserId().getUserName()));

        try
        {
            appointments = SQLHelper.getInstance().getAppointments();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }

        FilteredList<Appointment> filtered = new FilteredList<>(appointments, (t) -> true);

        // Lambda listener to support filtering by Month or by Week.
        // This lambda is much easier to read, and more efficent due to how the runtime creates objects.
        // Satisfies Rubric task D and G.
        dateStart.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if (oldValue == null || !oldValue.equals(newValue))
            {
                if (monthView)
                    dateStart.valueProperty().setValue(newValue.with(ChronoField.DAY_OF_MONTH, 1));
                else
                    dateStart.valueProperty().setValue(newValue.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1));
            }

            filtered.setPredicate((t) ->
            {
                // convert stored date in UTC to local time zone
                ZonedDateTime local = t.getStart().withZoneSameInstant(ZoneId.systemDefault());

                // If month view, filter appointments within that month
                if (monthView)
                    return local.toLocalDate().isAfter(newValue.withDayOfMonth(1).minusDays(1)) &&
                            local.toLocalDate().isBefore(newValue.withDayOfMonth(1).plusMonths(1));
                // If week view, only show appointments within that week (start of week locale specific).
                else
                    return local.toLocalDate().isAfter(newValue.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).minusDays(1)) &&
                            local.toLocalDate().isBefore(newValue.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).plusDays(7));
            });
        });

        SortedList<Appointment> sorted = new SortedList<>(filtered);
        tableCalendar.setItems(sorted);

        // Set initial start
        dateStart.setValue(LocalDate.now().withDayOfMonth(1));

        checkForUpcomingAppointments();
    }

    private void checkForUpcomingAppointments()
    {
        // This checks for appointments upcoming within 15 minutes of this method call
        // This satisfies Rubric Task H.
        List<Appointment> upcoming = new ArrayList<>();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime future = now.plusMinutes(15);

        appointments.forEach((a) ->
        {
            // We only care about appointments for the current user, unless they are the 'test' user.
            if (GlobalInformation.getInstance().getUser().compareTo(a.getUserId().getUserName()) != 0 ||
                    GlobalInformation.getInstance().getUser().compareTo("test") == 0)
                return;

            ZonedDateTime start = a.getStart();
            ZonedDateTime end = a.getEnd();

            // We need to check if the appointment:
            // 1) Starts within the next 15 minutes -OR-
            // 2) Ends within the next 15 minutes (this could be an upcoming, or already in progress one.
            // 3) And the end is NOT before the current time (to ignore appointments that already ended).
            if ((start.isBefore(future) || end.isBefore(future)) && !end.isBefore(now))
                upcoming.add(a);
        });

        // If the list isn't empty, we have one or more appointments within the next 15 minutes.
        if (upcoming.size() > 0)
        {
            StringBuilder message = new StringBuilder();
            message.append("You have the following appointment");

            if (upcoming.size() > 1)
                message.append("s");
            message.append(" upcoming shortly:");
            message.append(System.lineSeparator());

            upcoming.forEach((a) -> message.append(a.getTitle() + " with " + a.getCustomerId().getCustomerName() + " at " + a.getStart().format(DateTimeFormatter.ISO_LOCAL_TIME) + System.lineSeparator()));

            new Alert(Alert.AlertType.INFORMATION, message.toString()).showAndWait();
        }
    }

    // This shows various types of reports the user asked for.
    // This satisfies Rubric Task I.
    private void showReport(ActionEvent event, ObservableList<IDatabaseReportEntity> list, String windowTitle) throws IOException
    {
        // Show window, wait for user
        FXMLLoader loader = GlobalInformation.loadWindow("view/Report.fxml", (Stage) ((Button) event.getSource()).getScene().getWindow());
        Stage stage = (Stage) ((Parent) loader.getRoot()).getScene().getWindow();
        stage.setTitle(windowTitle);

        ReportController controller = loader.getController();
        controller.setReportList(list);
        stage.showAndWait();
    }

    private void showError(Exception ex)
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, ex.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
        alert.showAndWait();
    }
}
