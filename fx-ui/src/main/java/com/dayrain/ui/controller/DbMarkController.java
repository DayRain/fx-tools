package com.dayrain.ui.controller;

import com.dayrain.common.StringUtils;
import com.dayrain.dbmark.DbMarkService;
import com.dayrain.dbmark.DbMarkServiceImpl;
import com.dayrain.dbmark.entity.ColumnInfo;
import com.dayrain.ui.config.DbMarkConfig;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;

import java.util.List;

public class DbMarkController {

    private DbMarkService dbMarkService = new DbMarkServiceImpl();

    @FXML
    public TableView<ColumnInfo> columnTable;

    @FXML
    public JFXComboBox<String> tableComboBox;

    @FXML
    public TableColumn<ColumnInfo, String> columnName;

    @FXML
    public TableColumn<ColumnInfo, String> comment;

    @FXML
    public Label pageLabel;

    @FXML
    public JFXTextField pageField;

    private String curTable;

    private ColumnPage<ColumnInfo> columnPage;

    @FXML
    private void initialize() {
        List<String> tables = dbMarkService.listTables();
        tableComboBox.setItems(FXCollections.observableArrayList(tables));
        tableComboBox.setEditable(false);
        tableComboBox.getSelectionModel().selectFirst();
        curTable = tableComboBox.getSelectionModel().getSelectedItem();
        //设置下拉框监听
        tableComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!StringUtils.equals(oldValue, newValue)) {
                curTable = newValue;
                columnPage.updateItems(dbMarkService.listColumns(curTable));
                refreshTable();
            }
        });

        columnPage = new ColumnPage<>(dbMarkService.listColumns(curTable));
        //绑定表格数据
        columnTable.setEditable(true);
        columnTable.setFixedCellSize(61.45);
        //!!!
        columnTable.getSelectionModel().setCellSelectionEnabled(true);

        refreshTable();

        //设置输入框监听
        pageField.setOnKeyPressed(event -> {
            if (KeyCode.ENTER.equals(event.getCode())) {
                doJump();
            }
        });

        columnName.setCellValueFactory(new PropertyValueFactory<>("identify"));
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment.setCellFactory(TextFieldTableCell.forTableColumn());

        comment.setOnEditCommit(event -> {
            ColumnInfo columnInfo = event.getRowValue();
            //1、更新信息
            String newValue = event.getNewValue();
            if (StringUtils.isBlank(newValue)) {
                String defaultComment = DbMarkConfig.getDefaultComment(columnInfo.getColumnName());
                if (defaultComment != null) {
                    newValue = defaultComment;
                }
            }
            columnInfo.setComment(newValue);
            dbMarkService.alterColumn(columnInfo);

            //2、切换焦点
            int curPosition = columnTable.getSelectionModel().getSelectedIndex() + 1;
            if (curPosition < columnPage.getSize()) {
                columnTable.getSelectionModel().selectBelowCell();
            } else {
                columnPage.switchNextPage();
                refreshTable();
            }
        });
    }

    @FXML
    public void prePage(ActionEvent actionEvent) {
        columnPage.switchPrePage();
        refreshTable();
    }

    @FXML
    public void nextPage(ActionEvent actionEvent) {
        columnPage.switchNextPage();
        refreshTable();
    }

    @FXML
    public void jumpPage(ActionEvent actionEvent) {
        doJump();
    }

    void doJump() {
        columnPage.jump(Integer.parseInt(pageField.getText()));
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<ColumnInfo> columnInfos = FXCollections.observableArrayList(columnPage.getCurrentPage());
        columnTable.setItems(columnInfos);
        pageLabel.setText(String.format("(%s/%s)", columnPage.getCurrent(), columnPage.getTotalPage()));
        columnTable.getSelectionModel().select(0, comment);
    }
}

class ColumnPage<T> {
    private int current;
    private final int size;
    private int totalPage;
    private List<T> items;
    private int totalCount;

    public ColumnPage(List<T> items) {
        this.current = 1;
        this.size = 10;
        updateItems(items);
    }

    public List<T> getCurrentPage() {
        if (current <= 1) {
            current = 1;
        }

        if (current >= totalPage) {
            current = totalPage;
        }

        return items.subList((current - 1) * size, Math.min((current - 1) * size + size, totalCount));
    }

    public void updateItems(List<T> items) {
        this.totalCount = items.size();
        this.items = items;
        this.totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
    }

    public boolean hasPrePage() {
        return current > 0;
    }

    public boolean hasNextPage() {
        return current < totalPage;
    }

    public void switchPrePage() {
        if (hasPrePage()) {
            current--;
        }
    }

    public void switchNextPage() {
        if (hasNextPage()) {
            current++;
        }
    }

    public void jump(int page) {
        if (page > 0 && page <= totalPage) {
            current = page;
        }
    }

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
