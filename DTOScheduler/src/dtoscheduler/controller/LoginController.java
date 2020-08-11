/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class LoginController implements Initializable
{

    private ResourceBundle rb;
    private Connection connection;
    private boolean loginSuccessful = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.rb = rb;
        System.out.println(Locale.getDefault());

        loginTitle.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        buttonExit.setText(rb.getString("exit"));
        buttonLogin.setText(rb.getString("login"));
    }

    @FXML
    private Label loginTitle;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonLogin;

    @FXML
    void onExit(ActionEvent event)
    {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onLogin(ActionEvent event)
    {
        // Logger satisfies Rubic Task J.
        Logger logger = Logger.getLogger("logins");
        try
        {
            FileHandler fh = new FileHandler("logins.txt", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        }
        catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Error opening log file: {0}", ex.getMessage());
        }

        // Use try resources to ensure the database objects are cleaned up immediately.
        try (PreparedStatement statement = connection.prepareStatement("SELECT userId FROM user WHERE userName = ? AND password = ?"))
        {
            // Use bind variables to avoid SQL injections
            statement.setString(1, textUsername.getText());
            statement.setString(2, textPassword.getText());

            try (ResultSet rs = statement.executeQuery())
            {
                // Any row in the result set means the user exists, and the login was correct.
                // This satisfies Rubric Task F
                if (rs.first())
                {
                    logger.log(Level.INFO, "Successful login for user {0}", new Object[]
                    {
                        textUsername.getText()
                    });
                    loginSuccessful = true;
                    ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
                    return;
                }
            }
        }
        catch (SQLException ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            error.showAndWait();

            return;
        }

        // Log a securty warning
        logger.log(Level.WARNING, "Unsuccessful login for user {0}", new Object[]
        {
            textUsername.getText()
        });

        Alert error = new Alert(Alert.AlertType.ERROR, rb.getString("login_error"), ButtonType.OK);
        error.showAndWait();
    }

    public void setConnection(Connection conn)
    {
        connection = conn;
    }

    public boolean getLoginSuccessful()
    {
        return loginSuccessful;
    }

    public String getLoginUser()
    {
        return textUsername.getText();
    }
}
