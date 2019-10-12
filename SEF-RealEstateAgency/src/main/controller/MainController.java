package main.controller;

import javafx.fxml.FXML;
import main.model.DBConnector;

public class MainController {
    //    private ObservableList<Property> data;
//    private FilteredList<Property> filteredList;
//    private ContextMenu cm;

    private DBConnector dbConnector;

    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public DBConnector getDbConnector() {
        return dbConnector;
    }

    @FXML
    void initialize() {
        //TODO DIFFERENT MAIN VIEW FOR DIFFERENT TYPES OF USERS?
//        conn = new DBConnector();
//
//        try {
//            Connection newConn = conn.getConnection();
//            data = FXCollections.observableArrayList();
//
//            ResultSet rs = newConn.createStatement().executeQuery("SELECT * FROM STOCK");
//            while (rs.next()) {
//                data.add(new Stock(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6)));
//            }
//        } catch (SQLException e) {
//            System.err.println(e);
//        }
    }
}
