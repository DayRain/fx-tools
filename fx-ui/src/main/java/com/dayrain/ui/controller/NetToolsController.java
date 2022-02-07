package com.dayrain.ui.controller;

import com.dayrain.net.tools.PortCheckService;
import com.dayrain.net.tools.port.PortCheckServiceImpl;
import com.dayrain.net.tools.port.PortInfo;
import com.dayrain.ui.DialogBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NetToolsController {

    private final PortCheckService portCheckService = new PortCheckServiceImpl();
    @FXML
    public TableColumn<PortInfo, Button> kill;

    private List<PortInfo> portInfos;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public TableView<PortInfo> processTable;

    @FXML
    public TableColumn<PortInfo, String> processName;

    @FXML
    public TableColumn<PortInfo, Integer> pid;

    @FXML
    public TableColumn<PortInfo, Integer> port;

    @FXML
    public MenuBar menuBar;

    @FXML
    public TextField textField;

    @FXML
    private void initialize() {
        update();
        processName.setCellValueFactory(new PropertyValueFactory<>("processName"));
        pid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        port.setCellValueFactory(new PropertyValueFactory<>("port"));
        processTable.setItems(FXCollections.observableArrayList(portInfos));

        textField.setOnKeyPressed(event -> {
            if(KeyCode.ENTER.equals(event.getCode())) {
                update();
                search();
            }
        });

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || "".equals(newValue.trim())) {
                    System.out.println("empty");
                    update();
                    search();
                }
            }
        });

        processName.setCellFactory(tc ->{
            TableCell<PortInfo, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(processName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });


        kill.setCellFactory(tc ->{
            TableCell<PortInfo, Button> cell = new TableCell<PortInfo, Button>() {
                @Override
                protected void updateItem(Button item, boolean empty) {
                    super.updateItem(item, empty);
                    TableView<PortInfo> tableView = this.getTableView();
                    TableRow tableRow = this.getTableRow();

                    if(empty || tableRow == null) {
                        setGraphic(null);
                        return;
                    }

                    Button button = new Button("终止");
                    button.setPrefWidth(tc.getPrefWidth());
                    button.setStyle("-jfx-button-type: FLAT;" +
                            "-fx-background-color: white;" +
                            "-fx-text-fill: black;");
                    this.setGraphic(button);

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ObservableList<PortInfo> items = tableView.getItems();
                            PortInfo portInfo = items.get(tableRow.getIndex());

                            new DialogBuilder(processTable).setTitle("终止进程").setMessage("确定要终止程序 [" + portInfo.getProcessName() + "] 吗?")
                                    .setPositiveBtn("确定", "#d71345", () -> {
                                        kill(portInfo);
                                        update();
                                    }).setNegativeBtn("取消").create();

                        }
                    });

                    button.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            button.setStyle("-jfx-button-type: FLAT;" +
                                    "-fx-background-color: #555593;" +
                                    "-fx-text-fill: white;");
                        }
                    });

                    button.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            button.setStyle("-jfx-button-type: FLAT;" +
                                    "-fx-background-color: white;" +
                                    "-fx-text-fill: black;");
                        }
                    });
                }
            };

            return cell;
        });


    }

    private void search() {
        String port = textField.getText();
        if(port == null || "".equals(port.trim())) {
            processTable.setItems(FXCollections.observableArrayList(portInfos));
            return;
        }
        List<PortInfo>curList = new ArrayList<>();
        for (PortInfo portInfo : portInfos) {
            if(String.valueOf(portInfo.getPort()).contains(port)) {
                curList.add(portInfo);
            }
        }
        processTable.setItems(FXCollections.observableArrayList(curList));
    }

    private void update() {
        portInfos = portCheckService.listPort();
    }

    private void kill(PortInfo portInfo) {
        System.out.println("结束进程" + portInfo.getPid());
        portCheckService.kill(portInfo.getPid());
    }
}
