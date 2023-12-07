package com.example.todolist;

import com.example.todolist.db.dao.TagsDao;
import com.example.todolist.db.dao.TaskDao;
import com.example.todolist.db.dao.tasks_tagsDao;
import com.example.todolist.models.Tags;
import com.example.todolist.models.Task;
import com.example.todolist.models.tasks_tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.net.URL;
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
    private Button btnAdd, btnReset, btnAddTag, btnSendTag, btnRegresar;
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
    private GridPane gpTarea;

    @FXML
    private AnchorPane anPaneInicio;



    @FXML
    private Label lblDueDate;

    @FXML
    private TextField txtTag;

    @FXML
    private Label lblName;



    @FXML
    private ComboBox<String> cboTarea;

    @FXML
    private AnchorPane anPaneTask_Tags;

    @FXML
    private DatePicker dpDueDate;

    @FXML
    private Label lblDescription;

    @FXML
    private AnchorPane anPaneTags;

    @FXML
    private ChoiceBox cboTags;

    @FXML
    private ComboBox<String> cboEtiqueta;



    ObservableList<Task> tasksList = FXCollections.observableArrayList();

    Boolean editMode = false;
    Task taskSelected = null;
    Random random = new Random();

    TaskDao taskDao = new TaskDao();
    TagsDao tagsDao = new TagsDao();

    tasks_tagsDao tasks_tagsDao = new tasks_tagsDao();
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
        
    cargarNombresTareasEnComboBox();    
     cargarEtiquetasEnComboBox();
    }

    private void cargarEtiquetasEnComboBox() {
        List<Tags> etiquetas = tagsDao.findAll(); // Obtener todas las etiquetas desde la base de datos

        // Limpiar el ComboBox antes de agregar nuevos elementos
        cboEtiqueta.getItems().clear();

        // Obtener solo las descripciones de las etiquetas
        List<String> descripcionesEtiquetas = etiquetas.stream()
                .map(Tags::getDescription)
                .collect(Collectors.toList());

        // Agregar las descripciones de las etiquetas al ComboBox
        cboEtiqueta.getItems().addAll(descripcionesEtiquetas);

        // Opcional: Si deseas manipular más que solo las descripciones de las etiquetas en el ComboBox, puedes usar objetos Tags directamente
        // cboEtiqueta.getItems().addAll(etiquetas);
        // cboEtiqueta.setCellFactory(tagListView -> new ListCell<Tags>() {
        //     @Override
        //     protected void updateItem(Tags tag, boolean empty) {
        //         super.updateItem(tag, empty);
        //         if (empty || tag == null) {
        //             setText(null);
        //         } else {
        //             setText(tag.getDescription());
        //         }
        //     }
        // });
    }

    private void cargarNombresTareasEnComboBox() {

        List<Task> tareas = taskDao.findAll(); // Obtener todas las tareas desde la base de datos

        // Limpiar el ComboBox antes de agregar nuevos elementos
        cboTarea.getItems().clear();

        // Obtener solo los nombres de las tareas
        List<String> nombresTareas = tareas.stream()
                .map(Task::getName)
                .collect(Collectors.toList());

        // Agregar los nombres de las tareas al ComboBox
        cboTarea.getItems().addAll(nombresTareas);

        // Opcional: Si deseas manipular más que solo los nombres de las tareas en el ComboBox, puedes usar objetos Task directamente
        // cboTarea.getItems().addAll(tareas);
        // cboTarea.setCellFactory(taskListView -> new ListCell<Task>() {
        //     @Override
        //     protected void updateItem(Task task, boolean empty) {
        //         super.updateItem(task, empty);
        //         if (empty || task == null) {
        //             setText(null);
        //         } else {
        //             setText(task.getName());
        //         }
        //     }
        // });
    }

    private void cargarTagsEnChoiceBox() {
        // Crear un elemento "Seleccionar"
        Tags seleccionarTag = new Tags();
        seleccionarTag.setIdTags(-1); // Puedes usar un ID especial o no asignado en tu base de datos
        seleccionarTag.setDescription("Disponibles");

        List<Tags> tags = tagsDao.findAll();

        // Limpiar el ChoiceBox antes de agregar nuevos elementos
        cboTags.getItems().clear();

        // Agregar el elemento "Seleccionar" al principio de la lista
        tags.add(0, seleccionarTag);

        // Agregar los tags obtenidos desde el DAO al ChoiceBox
        cboTags.getItems().addAll(tags);

        // Opcional: Si quieres mostrar solo la descripción del tag en el ChoiceBox, puedes configurar el CellFactory
        cboTags.setConverter(new StringConverter<Tags>() {
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
        cboTags.setValue(seleccionarTag);

    }

    private void loadTasksData() {
        List<Task> servicioList = taskDao.findAll();
        ObservableList<Task> tasks = FXCollections.observableArrayList(servicioList);
        tblTasks.setItems(tasks);
    }




    //Método para agregar una tarea en la base de datos-----------------------------------------------------------------------------------

    @FXML
    protected void onAddButtonClick() {

    String name = txtName.getText();
    String description = txtDescription.getText();
    LocalDate dueDate = dpDueDate.getValue();

    boolean statusDefault = false;

    if (!name.isEmpty() && !description.isEmpty() && dueDate != null) {

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setStatus(statusDefault);
        newTask.setDueDate(Date.valueOf(dueDate));

        boolean saved = taskDao.save(newTask);

        if (saved) {
                        // Limpiar los campos después de guardar exitosamente
                        txtName.clear();
                        txtDescription.clear();
                        dpDueDate.setValue(null);
                        cboTags.getSelectionModel().clearSelection();
                        cargarNombresTareasEnComboBox();
                        // Volver a cargar los datos en la tabla para reflejar los cambios
                        loadTasksData();
                    } else {
                        showMessage("Error al guardar la tarea en la base de datos.");
                    }

            } else {
                showMessage("Por favor, completa todos los campos para agregar una tarea.");
            }
    }

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
        txtTag.setVisible(true);
        cboTags.setVisible(false);
        btnAddTag.setVisible(false);
        btnSendTag.setVisible(true);
    }

    //Método para mandar una etiqueta a la base de datos-------------------------------------------------------------------------------
    public void onSendTag(ActionEvent actionEvent) {
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
                cargarEtiquetasEnComboBox();
                showMessage("Se agregó la etiqueta con exito");

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

    public void onAñadirTarea(ActionEvent actionEvent) {
        cerrarVentanas();
        gpTarea.setVisible(true);
        btnRegresar.setVisible(true);
    }

    public void onAddEtiqueta(ActionEvent actionEvent) {
        cerrarVentanas();
        anPaneTags.setVisible(true);
        btnRegresar.setVisible(true);
    }

    public void onActualizar(ActionEvent actionEvent) {
        cerrarVentanas();
        anPaneTask_Tags.setVisible(true);
        btnRegresar.setVisible(true);

    }

    public void cerrarVentanas(){
        anPaneInicio.setVisible(false);
        anPaneTags.setVisible(false);
        anPaneTask_Tags.setVisible(false);
        gpTarea.setVisible(false);
    }

    // Método para obtener la ID de la tarea seleccionada basada en su nombre
private int obtenerIdTareaSeleccionada() {
    String nombreTareaSeleccionada = cboTarea.getValue();
    if (nombreTareaSeleccionada != null) {
        Task tarea = taskDao.findByName(nombreTareaSeleccionada);
        if (tarea != null) {
            return tarea.getId();
        }
    }
    return -1; // o un valor que tenga sentido en tu aplicación
}

private int obtenerIdEtiquetaSeleccionada() {
    String descripcionEtiquetaSeleccionada = cboEtiqueta.getValue();
    if (descripcionEtiquetaSeleccionada != null) {
        Tags etiqueta = tagsDao.findByDescription(descripcionEtiquetaSeleccionada);
        if (etiqueta != null) {
            return etiqueta.getIdTags();
        }
    }
    return -1; // o un valor que tenga sentido en tu aplicación
}

    public void onMezclarClick(ActionEvent actionEvent) {
        //Metodo para añadir etiquetas con las tareas

        int idTag = obtenerIdEtiquetaSeleccionada();
        int idTask = obtenerIdTareaSeleccionada();

    boolean statusDefault = false;

    if (idTag != 0 && idTask != 0) {

        tasks_tags newTask = new tasks_tags();

        newTask.setIdTasks(idTask);
        newTask.setIdTag(idTag);

        boolean saved = tasks_tagsDao.save(newTask);

        if (saved) {
                        // Limpiar los campos después de guardar exitosamente
                        cboEtiqueta.getSelectionModel().clearSelection();
                        cboTarea.getSelectionModel().clearSelection();

                        // Volver a cargar los datos en la tabla para reflejar los cambios

                    } else {
                        showMessage("Error al guardar la tarea en la base de datos.");
                    }

            } else {
                showMessage("Por favor, completa todos los campos para agregar una tarea.");
            }

    }

    public void onRegresar(ActionEvent actionEvent) {
        cerrarVentanas();
        anPaneInicio.setVisible(true);
        btnRegresar.setVisible(false);
    }
}