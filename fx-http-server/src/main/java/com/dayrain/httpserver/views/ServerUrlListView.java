package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ServerConfig;
import com.dayrain.httpserver.component.ServerUrl;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * server 列表
 * @author peng
 * @date 2021/11/11
 */
public class ServerUrlListView extends ListView<ServerUrl> {

    private ServerConfig serverConfig;

    public ServerUrlListView(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        createView();
    }

    private void createView() {
        ObservableList<ServerUrl> urlList = FXCollections.observableArrayList(serverConfig.getServerUrls());
        setItems(urlList);
        this.setCellFactory(new Callback<ListView<ServerUrl>, ListCell<ServerUrl>>() {
            @Override
            public ListCell<ServerUrl> call(ListView<ServerUrl> param) {
                return new ListCell<ServerUrl>() {
                    @Override
                    protected void updateItem(ServerUrl item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            ServerUrlPaneView urlPane = new ServerUrlPaneView(item, serverConfig, ServerUrlListView.this);
                            this.setGraphic(urlPane);
                        }
                    }
                };
            }
        });

        this.prefHeightProperty().bind(Bindings.size(urlList).multiply(60));
        this.setFocusTraversable(false);
    }

}
