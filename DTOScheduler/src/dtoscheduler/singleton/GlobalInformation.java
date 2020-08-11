/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.singleton;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class GlobalInformation
{
    private static final GlobalInformation _instance;
    private String user;
    private boolean dirty = false;
    
    static
    {
        _instance = new GlobalInformation();
    }

    // Ensure singleton
    private GlobalInformation()
    {
    }

    public static GlobalInformation getInstance()
    {
        return _instance;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public static FXMLLoader loadWindow(String resource, Stage owner) throws IOException
    {
        // Create window
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(dtoscheduler.DTOScheduler.class.getResource(resource));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        // caller should show the window
        return loader;
    }

    public boolean getDirty()
    {
        return dirty;
    }
    
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }
}
