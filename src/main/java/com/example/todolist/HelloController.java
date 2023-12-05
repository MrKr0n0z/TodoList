package com.example.todolist;

import com.example.todolist.db.MySQLConnection;
import com.example.todolist.db.dao.TagsDao;
import com.example.todolist.db.dao.TaskDao;
import com.example.todolist.models.Tags;
import com.example.todolist.models.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class HelloController implements Initializable {


    //-----------------------------------------------------------Atributos----------------------------------------------------------------------------------------

    @FXML
    private Button btnAdd, btnReset, btnAddTag, btnSendTag;
    @FXML
    private TextField txtName, txtDescription;




    @FXML
    TableView<Task> tblTasks;

    @FXML
    private TableColumn<Task, Integer> id;

    @FXML
    private TableColumn<Task, String> nameColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, String> estatusColumn;

    @FXML
    private TableColumn<Task, LocalDate> dateColumn;

    @FXML
    private TableColumn<Task, Integer> idTagsColumn;


    @FXML
    private ChoiceBox cbTags;
    @FXML
    private TextField txtTag;
    @FXML
    DatePicker dpDueDate;
    @FXML
    Label lblName, lblDescription, lblDueDate;
    ObservableList<Task> tasksList = FXCollections.observableArrayList();

    Boolean editMode = false;
    Task taskSelected = null;
    Random random = new Random();

    TaskDao taskDao = new TaskDao();
    TagsDao tagsDao = new TagsDao();

    private List<Tags> tagsList; // Mantén la lista de Tags


    //---------------------------------------------------------Métodos----------------------------------------------------------------------------------------


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    id.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    estatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    idTagsColumn.setCellValueFactory(new PropertyValueFactory<>("idTags"));

   cargarTagsEnChoiceBox();

    loadTasksData();
       // initTableInfo();
    }

    private void cargarTagsEnChoiceBox() {
        // Crear un elemento "Seleccionar"
        Tags seleccionarTag = new Tags();
        seleccionarTag.setIdTags(-1); // Puedes usar un ID especial o no asignado en tu base de datos
        seleccionarTag.setDescription("Seleccionar");

        List<Tags> tags = tagsDao.findAll();

        // Limpiar el ChoiceBox antes de agregar nuevos elementos
        cbTags.getItems().clear();

        // Agregar el elemento "Seleccionar" al principio de la lista
        tags.add(0, seleccionarTag);

        // Agregar los tags obtenidos desde el DAO al ChoiceBox
        cbTags.getItems().addAll(tags);

        // Opcional: Si quieres mostrar solo la descripción del tag en el ChoiceBox, puedes configurar el CellFactory
        cbTags.setConverter(new StringConverter<Tags>() {
            @Override
            public String toString(Tags tag) {
                return tag != null ? tag.getDescription() : "";
            }

            @Override
            public Tags fromString(String string) {
                // Si necesitas convertir de nuevo el string a un Tag, puedes hacerlo aquí
                return null;
            }
        });
        // Establecer el valor predeterminado a "Seleccionar"
        cbTags.setValue(seleccionarTag);

    }

    private void loadTasksData() {
        List<Task> servicioList = taskDao.findAll();
        ObservableList<Task> tasks = FXCollections.observableArrayList(servicioList);
        tblTasks.setItems(tasks);
    }



    @FXML
    protected void onAddButtonClick() {

    String name = txtName.getText();
    String description = txtDescription.getText();
    LocalDate dueDate = dpDueDate.getValue();

    int selectedTagIndex = cbTags.getSelectionModel().getSelectedIndex(); // Obtiene el índice seleccionado

    boolean statusDefault = false;

    if (!name.isEmpty() && !description.isEmpty() && dueDate != null && selectedTagIndex != -1) {
        int selectedTagId = tagsList.get(selectedTagIndex).getIdTags(); // Obtiene el idTags correspondiente al índice

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setStatus(statusDefault);
        newTask.setDueDate(Date.valueOf(dueDate));
        newTask.setIdTags(selectedTagId);

        boolean saved = taskDao.save(newTask);

        if (saved) {
                        // Limpiar los campos después de guardar exitosamente
                        txtName.clear();
                        txtDescription.clear();
                        dpDueDate.setValue(null);
                        cbTags.getSelectionModel().clearSelection();

                        // Volver a cargar los datos en la tabla para reflejar los cambios
                        loadTasksData();
                    } else {
                        showMessage("Error al guardar la tarea en la base de datos.");
                    }

            } else {
                showMessage("Por favor, completa todos los campos para agregar una tarea.");
            }
    }


/*
    private void initTableInfo() {

        tasksList = FXCollections.observableArrayList( taskDao.findAll() );
        tblTasks.setItems(tasksList);
        tblTasks.setColumnResizePolicy((param) -> true );
        Platform.runLater(() -> customResize(tblTasks));
        //implements double click on tableview
        tblTasks.setOnMouseClicked((mouseEvent)->{
            if (mouseEvent.getClickCount() == 2)
            {
                //load task info in the form
                taskSelected = tblTasks.getSelectionModel().getSelectedItem();
                txtName.setText(taskSelected.getName());
                txtDescription.setText(taskSelected.getDescription());
                editMode = true;
                btnAdd.setText("Save");
            }
        });
    }


 */


    public void customResize(TableView<?> view) {
        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size()));
            });
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------

    public void onAddTagClick(ActionEvent actionEvent) {
        txtName.setVisible(false);
        txtDescription.setVisible(false);
        dpDueDate.setVisible(false);
        tblTasks.setVisible(false);
        lblName.setVisible(false);
        lblDescription.setVisible(false);
        lblDueDate.setVisible(false);
        btnAdd.setVisible(false);
        btnReset.setVisible(false);

         cbTags.setVisible(false); // Oculta el ChoiceBox
         txtTag.setVisible(true); // Muestra el TextField
        btnAddTag.setVisible(false);
        btnSendTag.setVisible(true);

    }

    public void onSendTag(ActionEvent actionEvent) {
        txtName.setVisible(true);
        txtDescription.setVisible(true);
        dpDueDate.setVisible(true);
        tblTasks.setVisible(true);
        lblName.setVisible(true);
        lblDescription.setVisible(true);
        lblDueDate.setVisible(true);
        btnAdd.setVisible(true);
        btnReset.setVisible(true);

        cbTags.setVisible(true);
        txtTag.setVisible(false);
        btnAddTag.setVisible(true);
        btnSendTag.setVisible(false);



        String nuevaDescripcion = txtTag.getText();

        if (!nuevaDescripcion.isEmpty()) {
            // Crear un nuevo objeto Tag con la descripción del TextField
            Tags nuevaTag = new Tags();
            nuevaTag.setDescription(nuevaDescripcion);

            // Guardar la nueva tag en la base de datos usando el TagsDao
            boolean exito = tagsDao.save(nuevaTag);

            if (exito) {
                // Recargar las tags en el ChoiceBox después de agregar la nueva
                cargarTagsEnChoiceBox();

                // Limpiar el TextField después de agregar la etiqueta
                txtTag.clear();
            } else {
                // Manejar el caso en que la inserción en la base de datos falle
                System.out.println("Error al agregar la etiqueta a la base de datos.");
            }
        } else {
            // Manejar el caso en que el TextField esté vacío
            System.out.println("El campo de texto está vacío. Por favor, ingresa una etiqueta.");
        }
    }

    //------------------------------------------------------------------------Métodos sin mucha logica----------------------------------------------------------

    @FXML
    protected void onResetButtonClick() {
        txtName.setText("");
        txtDescription.setText("");
        txtName.requestFocus();
        btnAdd.setText("Add");
        dpDueDate.setValue(null);
    }

        private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.show();
    }
}