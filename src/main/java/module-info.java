module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.todolist to javafx.fxml;
    opens com.example.todolist.models;
    exports com.example.todolist;
}