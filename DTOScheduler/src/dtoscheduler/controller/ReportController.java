/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.controller;

import dtoscheduler.interfaces.IDatabaseReportEntity;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class ReportController implements Initializable
{
    ObservableList<IDatabaseReportEntity> reportItems;

    @FXML
    private TableView<IDatabaseReportEntity> tableReport;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    public void setReportList(ObservableList<IDatabaseReportEntity> list)
    {
        buildTableColumns(list);
        this.reportItems = list;
        tableReport.setItems(reportItems);
    }

    // The report can be of any type, to just work off the interface
    // and build columns dynamically.
    private void buildTableColumns(ObservableList<IDatabaseReportEntity> items)
    {
        if (items.size() > 0)
        {
            IDatabaseReportEntity referenceColumn = items.get(0);

            for (int idx = 0; idx < referenceColumn.getColumnCount(); idx++)
            {
                Class<?> type = referenceColumn.getColumnType(idx);
                String name = referenceColumn.getColumnName(idx);

                TableColumn<IDatabaseReportEntity, Object> column = new TableColumn<>();
                column.setText(name);
                column.setCellValueFactory((param) ->
                {
                    TableColumn<IDatabaseReportEntity, Object> col = param.getTableColumn();
                    IDatabaseReportEntity item = param.getValue();
                    int index = param.getTableView().getColumns().indexOf(col);

                    Object field = item.getColumnValue(index);

                    if (field instanceof Integer || field instanceof Long)
                        col.setStyle("-fx-alignment: CENTER-RIGHT;");

                    return new SimpleObjectProperty<>(field);
                });

                if (type.equals(ZonedDateTime.class))
                {
                    column.setCellFactory((param) -> new TableCell<IDatabaseReportEntity, Object>()
                    {
                        private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);

                        @Override
                        protected void updateItem(Object item, boolean empty)
                        {
                            super.updateItem(item, empty);
                            if (empty)
                                setText(null);
                            else
                                setText(((ZonedDateTime) item).format(formatter));
                        }
                    });
                }

                if (type.equals(Duration.class))
                {
                    column.setCellFactory((param) -> new TableCell<IDatabaseReportEntity, Object>()
                    {
                        @Override
                        protected void updateItem(Object value, boolean empty)
                        {
                            Duration item = (Duration) value;
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
                }

                tableReport.getColumns().add(column);
            }
        }
    }
}
