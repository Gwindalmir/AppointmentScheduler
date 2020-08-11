/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler;

import dtoscheduler.controller.LoginController;
import com.mysql.cj.jdbc.MysqlDataSource;
import dtoscheduler.controller.MainWindowController;
import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Daniel
 */
public class DTOScheduler extends Application
{

    @Override
    public void start(Stage stage) throws IOException, InterruptedException
    {
        // Uncomment the line below to test french
        //Locale.setDefault(new Locale("fr", "FR"));

        // The locale lookup here, and passed to the Login view below satisfies Rubric Task A
        ResourceBundle rb = ResourceBundle.getBundle("dtoscheduler.resource.rb");

        // Load the data source
        InputStream fis = getClass().getResourceAsStream("resource/db.properties");
        Properties props = new Properties();
        props.load(fis);

        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://" + props.getProperty("MYSQL_HOSTNAME") + ":" + props.getProperty("MYSQL_PORT") + "/" + props.getProperty("MYSQL_CATALOG") + "?" + props.getProperty("MYSQL_ADDITIONAL"));
        ds.setUser(props.getProperty("MYSQL_USER"));
        ds.setPassword(props.getProperty("MYSQL_PASSWORD"));
        try
        {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            SQLHelper.getInstance().setConnection(conn);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Login.fxml"), rb);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(stage);
            Parent root = loader.load();
            ((LoginController) loader.getController()).setConnection(conn);
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait();

            if (((LoginController) loader.getController()).getLoginSuccessful())
            {
                // Save the logged in username, for using in SQL updates
                GlobalInformation.getInstance().setUser(((LoginController) loader.getController()).getLoginUser());

                // Load the main form
                FXMLLoader mainloader = new FXMLLoader(getClass().getResource("view/MainWindow.fxml"), rb);
                Parent mainroot = mainloader.load();
                Scene mainscene = new Scene(mainroot);

                // lambda event handler to save the data and close the connection when the main window is closed
                // This lambda satisfies Rubric Task G
                stage.setOnCloseRequest((WindowEvent l) ->
                {
                    // Check if there are unsaved changes
                    if(GlobalInformation.getInstance().getDirty())
                    {
                        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, 
                                "You have unsaved changes.\nWould you like to save the changes before quitting?",  
                                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL).showAndWait();
                        
                        if(result.isPresent())
                        {
                            if(result.get() == ButtonType.YES)
                            {
                                SQLHelper.getInstance().saveChanges();
                            }
                            else if(result.get() == ButtonType.NO)
                            {
                                SQLHelper.getInstance().undoChanges();
                            }
                            else if(result.get() == ButtonType.CANCEL)
                            {
                                // If they selected cancel, don't close the app, don't save either
                                l.consume();
                            }
                        }
                        else
                        {
                            SQLHelper.getInstance().undoChanges();
                        }
                    }
                    
                    try
                    {
                        conn.close();
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, ex.getMessage());
                    }
                });
                stage.setScene(mainscene);
                stage.show();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.SEVERE, ex.getMessage());
            Alert error = new Alert(Alert.AlertType.ERROR, "There was an error connecting to the MySQL database, please check the connection and try again: " + ex.getMessage(), ButtonType.OK);
            error.showAndWait();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
