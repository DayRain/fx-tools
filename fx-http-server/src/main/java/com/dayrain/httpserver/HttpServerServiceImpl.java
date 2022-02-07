package com.dayrain.httpserver;

import com.dayrain.httpserver.component.ConfigHolder;
import com.dayrain.httpserver.views.ApplicationStarter;
import com.dayrain.httpserver.views.HomeView;
import com.dayrain.httpserver.views.ViewHolder;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerServiceImpl implements HttpServerService{

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

    @Override
    public void start() {
        try {
            Stage stage = new Stage();
            ConfigHolder.init();
            ViewHolder.setPrimaryStage(stage);
            HomeView homeView = new HomeView();
            ViewHolder.setHomePage(homeView);
            homeView.start();
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
