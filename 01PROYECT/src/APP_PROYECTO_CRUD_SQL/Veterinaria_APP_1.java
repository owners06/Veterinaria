package APP_PROYECTO_CRUD_SQL;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;
import javafx.scene.layout.HBox;

public class Veterinaria_APP_1 extends Application {

    private final ObservableList<Propietario> propietarioList = FXCollections.observableArrayList();
    private final ObservableList<Mascota> mascotaList = FXCollections.observableArrayList();
    private final ObservableList<Historial_clinico> historial_clinicoList = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bd_veterinaria";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection dbConnection;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        loadPropietario();
        loadMascota();
        loadHistorial_clinico();

        primaryStage.setTitle("Veterinary Project App");

        Label propietarioLabel = new Label("Propietario");
        ListView<Propietario> propietarioListView = new ListView<>(propietarioList);
        propietarioListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Propietario item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre() + ", Direccion: " + item.getDireccion() + ", Telefono: " + item.getTelefono() + ", Email: " + item.getEmail());
                }
            }
        });
        Label mascotaLabel = new Label("Mascota");
        ListView<Mascota> mascotaListView = new ListView<>(mascotaList);
        mascotaListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Mascota item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre() + ", Edad: " + item.getEdad() + ", Tipo: " + item.getTipo() + ", ID PROPIETARIO: " + item.getIdPropietario());
                }
            }
        });
        Label historial_clinicoLabel = new Label("Historial Clinico");
        ListView<Historial_clinico> historial_clinicoListView = new ListView<>(historial_clinicoList);
        historial_clinicoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Historial_clinico item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", ID MASCOTA: " + item.getIdMascota() + ", Diagnostico: " + item.getDiagnostico() + ", Fecha: " + item.getFecha() + ", Tratamiento: " + item.getTratamiento());
                }
            }
        });
//BOTONES:
        Button agregarPropietarioButton = new Button("Agregar Propietario");
        agregarPropietarioButton.setOnAction(e -> agregarPropietario());

        Button agregarMascotaButton = new Button("Agregar Mascota");
        agregarMascotaButton.setOnAction(e -> agregarMascota());

        Button agregarHistorial_clinicoButton = new Button("Agregar Historial Clinico");
        agregarHistorial_clinicoButton.setOnAction(e -> agregarHistorial_clinico());
///// botones eliminar:
        Button eliminarPropietarioButton = new Button("Eliminar Propietario");
        eliminarPropietarioButton.setOnAction(e -> {
            Propietario propietarioSeleccionado = propietarioListView.getSelectionModel().getSelectedItem();
            eliminarPropietario(propietarioSeleccionado);
        });
        Button eliminarMascotaButton = new Button("Eliminar Mascota");
        eliminarMascotaButton.setOnAction(e -> {
            Mascota mascotaSeleccionado = mascotaListView.getSelectionModel().getSelectedItem();
            eliminarMascota(mascotaSeleccionado);
        });
        Button eliminarHistorial_clinicoButton = new Button("Eliminar Historial Clinico");
        eliminarHistorial_clinicoButton.setOnAction(e -> {
            Historial_clinico historial_clinicoSeleccionado = historial_clinicoListView.getSelectionModel().getSelectedItem();
            eliminarHistorial_clinico(historial_clinicoSeleccionado);
        });
//botones modificar update:
        Button modificarPropietarioButton = new Button("Modificar Propietario");
        modificarPropietarioButton.setOnAction(e -> {
            Propietario propietarioSeleccionado = propietarioListView.getSelectionModel().getSelectedItem();
            modificarPropietario(propietarioSeleccionado);
        });
        Button modificarMascotaButton = new Button("Modificar Mascota");
        modificarMascotaButton.setOnAction(e -> {
            Mascota mascotaSeleccionado = mascotaListView.getSelectionModel().getSelectedItem();
            modificarMascota(mascotaSeleccionado);
        });
        Button modificarHistorial_clinicoButton = new Button("Modificar Historial Clinico");
        modificarHistorial_clinicoButton.setOnAction(e -> {
            Historial_clinico historial_clinicoSeleccionada = historial_clinicoListView.getSelectionModel().getSelectedItem();
            modificarHistorial_clinico(historial_clinicoSeleccionada);
        });

        HBox buttonBox_propietario = new HBox(agregarPropietarioButton, modificarPropietarioButton, eliminarPropietarioButton);
        buttonBox_propietario.setSpacing(10);

        HBox buttonBox_mascota = new HBox(agregarMascotaButton, modificarMascotaButton, eliminarMascotaButton);
        buttonBox_mascota.setSpacing(10);

        HBox buttonBox_historial_clinico = new HBox(agregarHistorial_clinicoButton, modificarHistorial_clinicoButton, eliminarHistorial_clinicoButton);
        buttonBox_historial_clinico.setSpacing(10);

        VBox vbox = new VBox(
                propietarioLabel,
                propietarioListView,
                buttonBox_propietario,
                mascotaLabel,
                mascotaListView,
                buttonBox_mascota,
                historial_clinicoLabel,
                historial_clinicoListView,
                buttonBox_historial_clinico
        );
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Método para agregar un nuevo propietario 
    private void agregarPropietario() {
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("Agregar Propietario");
        dialogNombre.setHeaderText(null);
        dialogNombre.setContentText("Ingrese el nombre del propietario:");

        Optional<String> nombreOpt = dialogNombre.showAndWait();
        if (!nombreOpt.isPresent() || nombreOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El nombre del propietario no puede estar vacío.");
            return;
        }
        String nombre = nombreOpt.get().trim();
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El nombre del propietario debe ser formato texto.");
            return ;
        }

        TextInputDialog dialogDireccion = new TextInputDialog();
        dialogDireccion.setTitle("Agregar Propietario");
        dialogDireccion.setHeaderText(null);
        dialogDireccion.setContentText("Ingrese la dirección del propietario:");

        Optional<String> direccionOpt = dialogDireccion.showAndWait();
        if (!direccionOpt.isPresent() || direccionOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La dirección del propietario no puede estar vacía.");
            return;
        }

        TextInputDialog dialogTelefono = new TextInputDialog();
        dialogTelefono.setTitle("Agregar Propietario");
        dialogTelefono.setHeaderText(null);
        dialogTelefono.setContentText("Ingrese el teléfono del propietario:");

        Optional<String> telefonoOpt = dialogTelefono.showAndWait();
        if (!telefonoOpt.isPresent() || telefonoOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El teléfono del propietario no puede estar vacío.");
            return;
        }
        

        TextInputDialog dialogEmail = new TextInputDialog();
        dialogEmail.setTitle("Agregar Propietario");
        dialogEmail.setHeaderText(null);
        dialogEmail.setContentText("Ingrese el email del propietario:");

        Optional<String> emailOpt = dialogEmail.showAndWait();
        if (!emailOpt.isPresent() || emailOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El email del propietario no puede estar vacío.");
            return;
        }

        // Insertar nuevo propietario en la base de datos
        agregarPropietarioToDatabase(nombreOpt.get(), direccionOpt.get(), telefonoOpt.get(), emailOpt.get());

        // Actualizar la lista observable
        loadPropietario();
        showAlert("Propietario Agregado", "El propietario ha sido agregado correctamente.");
    }

    //Método para insertar un nuevo propietario en la base de datos
    private void agregarPropietarioToDatabase(String nombre, String direccion, String telefono, String email) {
        nombre = nombre.toUpperCase();
        direccion = direccion.toUpperCase();
        telefono = telefono.toUpperCase();
        email = email.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO propietario (nombre, direccion, telefono, email) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, nombre);
            statement.setString(2, direccion);
            statement.setString(3, telefono);
            statement.setString(4, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para agregar un nuevo propietario 
    private void agregarMascota() {
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("Agregar Mascota");
        dialogNombre.setHeaderText(null);
        dialogNombre.setContentText("Ingrese el nombre de la mascota:");

        Optional<String> nombreOpt = dialogNombre.showAndWait();
        if (!nombreOpt.isPresent() || nombreOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El nombre de la mascota no puede estar vacío.");
            return;
        }   
        String nombre = nombreOpt.get().trim();
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El nombre de la mascota debe ser formato texto.");
            return ;
        }

        TextInputDialog dialogEdad = new TextInputDialog();
        dialogEdad.setTitle("Agregar Mascota");
        dialogEdad.setHeaderText(null);
        dialogEdad.setContentText("Ingrese la edad de la mascota:");

        Optional<String> edadOpt = dialogEdad.showAndWait();
        if (!edadOpt.isPresent() || edadOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La edad de la mascota no puede estar vacía.");
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadOpt.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "La edad de la mascota debe ser un número.");
            return;
        }

        TextInputDialog dialogTipo = new TextInputDialog();
        dialogTipo.setTitle("Agregar Mascota");
        dialogTipo.setHeaderText(null);
        dialogTipo.setContentText("Ingrese el tipo de la mascota:");

        Optional<String> tipoOpt = dialogTipo.showAndWait();
        if (!tipoOpt.isPresent() || tipoOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El tipo de la mascota no puede estar vacío.");
            return;
        }
        String tipo = tipoOpt.get().trim();
        if (!tipo.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El tipo de la mascota debe ser formato texto.");
            return;
        }

        TextInputDialog dialogidPropietario = new TextInputDialog();
        dialogidPropietario.setTitle("Agregar Mascota");
        dialogidPropietario.setHeaderText(null);
        dialogidPropietario.setContentText("Ingrese id del propietario de la mascota:");

        Optional<String> idPropietarioOpt = dialogidPropietario.showAndWait();
        if (!idPropietarioOpt.isPresent() || idPropietarioOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El id del propietario no puede estar vacía.");
            return;
        }
        int idPropietario;
        try {
            idPropietario = Integer.parseInt(idPropietarioOpt.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "El id del propietario debe ser un número.");
            return;
        }
        if (!propietarioExists(idPropietario)) {
            showAlert("Propietario No Existe", "El propietario con el ID especificado no existe.");
            return;
        }

        // Insertar nueva mascota en la base de datos
        agregarMascotaToDatabase(nombreOpt.get(), edad, tipoOpt.get(), idPropietario);

        // Actualizar la lista observable
        loadMascota();
        showAlert("Mascota Modificada", "La mascota ha sido agregada correctamente.");
    }
    
    // Método para insertar una nueva mascota en la base de datos
    private void agregarMascotaToDatabase(String nombre, int edad, String tipo, int idPropietario) {
        nombre = nombre.toUpperCase();
        tipo = tipo.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO mascota (nombre, edad, tipo, idPropietario) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, nombre);
            statement.setInt(2, edad);
            statement.setString(3, tipo);
            statement.setInt(4, idPropietario);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para agregar un nuevo historial clínico
    private void agregarHistorial_clinico() {
        TextInputDialog dialogIdMascota = new TextInputDialog();
        dialogIdMascota.setTitle("Agregar Historial Clínico");
        dialogIdMascota.setHeaderText(null);
        dialogIdMascota.setContentText("Ingrese el ID de la mascota:");

        Optional<String> idMascotaOpt = dialogIdMascota.showAndWait();
        if (!idMascotaOpt.isPresent() || idMascotaOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El ID de la mascota no puede estar vacío.");
            return;
        }
        int idMascota;
        try {
            idMascota = Integer.parseInt(idMascotaOpt.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "El ID de la mascota debe ser un número.");
            return;
        }
        if (!mascotaExists(idMascota)) {
            showAlert("Mascota No Existe", "La mascota con el ID especificado no existe.");
            return;
        }

        TextInputDialog dialogDiagnostico = new TextInputDialog();
        dialogDiagnostico.setTitle("Agregar Historial Clínico");
        dialogDiagnostico.setHeaderText(null);
        dialogDiagnostico.setContentText("Ingrese el diagnóstico:");

        Optional<String> diagnosticoOpt = dialogDiagnostico.showAndWait();
        if (!diagnosticoOpt.isPresent() || diagnosticoOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El diagnóstico no puede estar vacío.");
            return;
        }
        String diagnostico = diagnosticoOpt.get().trim();
        if (!diagnostico.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El diagnostico de la mascota debe ser formato texto.");
            return ;
        }

        TextInputDialog dialogFecha = new TextInputDialog();
        dialogFecha.setTitle("Agregar Historial Clínico");
        dialogFecha.setHeaderText(null);
        dialogFecha.setContentText("Ingrese la fecha (YYYY-MM-DD):");

        Optional<String> fechaOpt = dialogFecha.showAndWait();
        if (!fechaOpt.isPresent() || fechaOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La fecha no puede estar vacío.");
            return;
        }
        int fecha;
        try {
            fecha = Integer.parseInt(fechaOpt.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "La fecha debe ser un número.");
            return;
        }

        TextInputDialog dialogTratamiento = new TextInputDialog();
        dialogTratamiento.setTitle("Agregar Historial Clínico");
        dialogTratamiento.setHeaderText(null);
        dialogTratamiento.setContentText("Ingrese el tratamiento:");

        Optional<String> tratamientoOpt = dialogTratamiento.showAndWait();
        if (!tratamientoOpt.isPresent() || tratamientoOpt.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El tratamiento no puede estar vacío.");
            return;
        } 
        String tratamiento = tratamientoOpt.get().trim();
        if (!tratamiento.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El tratamiento de la mascota debe ser formato texto.");
            return ;
        }

        // Insertar nuevo historial clínico en la base de datos
        agregarHistorial_clinicoToDatabase(idMascota, diagnosticoOpt.get(), fecha, tratamientoOpt.get());

        // Actualizar la lista observable
        loadHistorial_clinico();
        showAlert("Historial Clinico Agregado", "El historial clinico ha sido agregado correctamente.");
    }

    // Método para insertar un nuevo historial clínico en la base de datos
    private void agregarHistorial_clinicoToDatabase(int idMascota, String diagnostico, int fecha, String tratamiento) {
        diagnostico = diagnostico.toUpperCase();
        tratamiento = tratamiento.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO historial_clinico (idMascota, diagnostico, fecha, tratamiento) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, idMascota);
            statement.setString(2, diagnostico);
            statement.setInt(3, fecha);
            statement.setString(4, tratamiento);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void modificarPropietario(Propietario propietario) {
        if (propietario == null) {
            showAlert("No se ha seleccionado un propietario", "Por favor, seleccione una propietario para modificar.");
            return;
        }

        // Cuadro de diálogo para ingresar el nuevo nombre del propietario
        TextInputDialog nombreDialog = new TextInputDialog();
        nombreDialog.setTitle("Modificar Propietario");
        nombreDialog.setHeaderText(null);
        nombreDialog.setContentText("Ingrese el nuevo nombre para el propietario:");

        Optional<String> nuevoNombreResult = nombreDialog.showAndWait();
        if (!nuevoNombreResult.isPresent() || nuevoNombreResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El nombre del prpietario no puede estar vacío.");
            return;
        }
        String nuevonombre = nuevoNombreResult.get().trim();
        if (!nuevonombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El nombre del propietario debe ser formato texto.");
            return ;
        }
        
        TextInputDialog direccionDialog = new TextInputDialog();
        direccionDialog.setTitle("Modificar Propietario");
        direccionDialog.setHeaderText(null);
        direccionDialog.setContentText("Ingrese la nueva direccion para el propietario:");

        Optional<String> nuevaDireccionResult = direccionDialog.showAndWait();
        // Verificar si se ingresó una nueva direccion
        if (!nuevaDireccionResult.isPresent() || nuevaDireccionResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La direccion del propietario no puede estar vacía.");
            return;
        }
        
        TextInputDialog telefonoDialog = new TextInputDialog();
        telefonoDialog.setTitle("Modificar Propietario");
        telefonoDialog.setHeaderText(null);
        telefonoDialog.setContentText("Ingrese el nuevo telefono para el propietario:");

        Optional<String> nuevoTelefonoResult = telefonoDialog.showAndWait();
        // Verificar si se ingresó un nuevo telefono
        if (!nuevoTelefonoResult.isPresent() || nuevoTelefonoResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El telefono del propietario no puede estar vacío.");
            return;
        }
        
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Modificar Propietario");
        emailDialog.setHeaderText(null);
        emailDialog.setContentText("Ingrese el nuevo email para el propietario:");

        Optional<String> nuevaEmailResult = emailDialog.showAndWait();
        // Verificar si se ingresó un nuevo email
        if (!nuevaEmailResult.isPresent() || nuevaEmailResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El email del propietario no puede estar vacío.");
            return;
        }
        
        // Modificar la mascota en la base de datos
        modificarPropietarioEnBaseDeDatos(propietario.getId(), nuevoNombreResult.get(), nuevoTelefonoResult.get(), nuevaDireccionResult.get(), nuevaEmailResult.get());

        // Actualizar la lista observable
        loadPropietario();
        showAlert("Propietario Modificado", "El propietario ha sido modificado exitosamente.");
    }

    private void modificarPropietarioEnBaseDeDatos(int propietarioId, String nombre, String telefono, String direccion, String email) {
        // Convertir los valores a mayúsculas si es necesario
        nombre = nombre.toUpperCase();
        telefono = telefono.toUpperCase();
        direccion = direccion.toUpperCase();
        email = email.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE propietario SET nombre = ?, telefono = ?, direccion = ?, email = ? WHERE idPropietario = ?")) {
            statement.setString(1, nombre);
            statement.setString(2, telefono);
            statement.setString(3, direccion);
            statement.setString(4, email);
            statement.setInt(5, propietarioId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarMascota(Mascota mascota) {
        if (mascota == null) {
            showAlert("No se ha seleccionado una mascota", "Por favor, seleccione una mascota para modificar.");
            return;
        }

        // Cuadro de diálogo para ingresar el nuevo nombre de la mascota
        TextInputDialog nombreDialog = new TextInputDialog();
        nombreDialog.setTitle("Modificar Mascota");
        nombreDialog.setHeaderText(null);
        nombreDialog.setContentText("Ingrese el nuevo nombre para la mascota:");

        Optional<String> nuevoNombreResult = nombreDialog.showAndWait();
        if (!nuevoNombreResult.isPresent() || nuevoNombreResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El nombre de la mascota no puede estar vacío.");
            return;
        }
        String nuevonombre = nuevoNombreResult.get().trim();
        if (!nuevonombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El nombre de la mascota debe ser formato texto.");
            return ;
        }

        // Cuadro de diálogo para ingresar la nueva edad de la mascota
        TextInputDialog edadDialog = new TextInputDialog();
        edadDialog.setTitle("Modificar Mascota");
        edadDialog.setHeaderText(null);
        edadDialog.setContentText("Ingrese la nueva edad para la mascota:");

        Optional<String> nuevaEdadResult = edadDialog.showAndWait();
        if (!nuevaEdadResult.isPresent() || nuevaEdadResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La edad de la mascota no puede estar vacía.");
            return;
        }
        int nuevaEdad;
        try {
            nuevaEdad = Integer.parseInt(nuevaEdadResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "La edad de la mascota debe ser un número.");
            return;
        }

        // Cuadro de diálogo para ingresar el nuevo tipo de la mascota
        TextInputDialog tipoDialog = new TextInputDialog();
        tipoDialog.setTitle("Modificar Mascota");
        tipoDialog.setHeaderText(null);
        tipoDialog.setContentText("Ingrese el nuevo tipo de mascota:");

        Optional<String> nuevoTipoResult = tipoDialog.showAndWait();
        if (!nuevoTipoResult.isPresent() || nuevoTipoResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El tipo de la mascota no puede estar vacío.");
            return;
        }
        String nuevotipo = nuevoTipoResult.get().trim();
        if (!nuevotipo.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El tipo de la mascota debe ser formato texto.");
            return;
        }

        // Cuadro de diálogo para ingresar el nuevo id del propietario
        TextInputDialog idPropietarioDialog = new TextInputDialog();
        idPropietarioDialog.setTitle("Modificar Mascota");
        idPropietarioDialog.setHeaderText(null);
        idPropietarioDialog.setContentText("Ingrese el nuevo id del propietario para la mascota:");

        Optional<String> nuevoIdPropietarioResult = idPropietarioDialog.showAndWait();
        if (!nuevoIdPropietarioResult.isPresent() || nuevoIdPropietarioResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El id del propietario no puede estar vacío.");
            return;
        }
        int nuevoIdPropietario;
        try {
            nuevoIdPropietario = Integer.parseInt(nuevoIdPropietarioResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "El id del propietario debe ser un número.");
            return;
        }
        if (!propietarioExists(nuevoIdPropietario)) {
            showAlert("Propietario No Existe", "El propietario con el ID especificado no existe.");
            return;
        }

        // Modificar la mascota en la base de datos
        modificarMascotaEnBaseDeDatos(mascota.getId(), nuevoNombreResult.get(), nuevaEdad, nuevoTipoResult.get(), nuevoIdPropietario);

        // Actualizar la lista observable
        loadMascota();
        showAlert("Mascota Modificada", "La mascota ha sido modificada exitosamente.");
    }


    // Método para modificar una mascota en la base de datos
    private void modificarMascotaEnBaseDeDatos(int idMascota, String nombre, int edad, String tipo, int idPropietario) {
        nombre = nombre.toUpperCase();
        tipo = tipo.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE mascota SET nombre = ?, edad = ?, tipo = ?, idPropietario = ? WHERE idMascota = ?")) {
            statement.setString(1, nombre);
            statement.setInt(2, edad);
            statement.setString(3, tipo);
            statement.setInt(4, idPropietario);
            statement.setInt(5, idMascota);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarHistorial_clinico(Historial_clinico historial_clinico) {
        if (historial_clinico == null) {
            showAlert("No se ha seleccionado un historial clinico", "Por favor, seleccione un historial clinico para modificar.");
            return;
        }

        // Cuadro de diálogo para ingresar el nuevo historial_clinico de la mascota
        TextInputDialog  idMascotaDialog = new TextInputDialog();
        idMascotaDialog.setTitle("Modificar Id Mascota");
        idMascotaDialog.setHeaderText(null);
        idMascotaDialog.setContentText("Ingrese el nuevo id para la mascota:");

        Optional<String> nuevaIdMascotaResult = idMascotaDialog.showAndWait();
        // Verificar si se ingresó un nuevo id mascota
            if (!nuevaIdMascotaResult.isPresent() || nuevaIdMascotaResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El id de la mascota no puede estar vacío.");
            return;
        }
        int nuevaIdMascota;
        try {
            nuevaIdMascota = Integer.parseInt(nuevaIdMascotaResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "El id de la mascota debe ser un número.");
            return;
        }
        if (!mascotaExists(nuevaIdMascota)) {
            showAlert("Mascota No Existe", "La mascota con el ID especificado no existe.");
            return;
        }

        TextInputDialog diagnosticoDialog = new TextInputDialog();
        diagnosticoDialog.setTitle("Modificar Diagnostico");
        diagnosticoDialog.setHeaderText(null);
        diagnosticoDialog.setContentText("Ingrese el nuevo diagnostico para la mascota:");

        Optional<String> nuevoDiagnosticoResult = diagnosticoDialog.showAndWait();
         // Verificar si se ingresó un nuevo diagnostico
        if (!nuevoDiagnosticoResult.isPresent() || nuevoDiagnosticoResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El diagnostico de la mascota no puede estar vacío.");
            return;
        }
        String nuevodiagnostico = nuevoDiagnosticoResult.get().trim();
        if (!nuevodiagnostico.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El diagnostico de la mascota debe ser formato texto.");
            return ;
        }
        
        TextInputDialog fechaDialog = new TextInputDialog();
        fechaDialog.setTitle("Modificar Fecha");
        fechaDialog.setHeaderText(null);
        fechaDialog.setContentText("Ingrese la nueva fecha para la mascota:");

        Optional<String> nuevaFechaResult = fechaDialog.showAndWait();
        // Verificar si se ingresó una nueva fecha
        if (!nuevaFechaResult.isPresent() || nuevaFechaResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "La fecha de la mascota no puede estar vacía.");
            return;
        }
        int nuevaFecha;
        try {
            nuevaFecha = Integer.parseInt(nuevaFechaResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada Inválida", "El id de la mascota debe ser un número.");
            return;
        }

        TextInputDialog TratamientoDialog = new TextInputDialog();
        TratamientoDialog.setTitle("Modificar Tratamiento");
        TratamientoDialog.setHeaderText(null);
        TratamientoDialog.setContentText("Ingrese el nuevo Tratamiento para la mascota:");

        Optional<String> nuevoTratamientoResult = TratamientoDialog.showAndWait();
        // Verificar si se ingresó un nuevo tratamiento
        if (!nuevoTratamientoResult.isPresent() || nuevoTratamientoResult.get().trim().isEmpty()) {
            showAlert("Entrada Inválida", "El tratamiento de la mascota no puede estar vacío.");
            return;
        }
        String nuevotratamiento = nuevoTratamientoResult.get().trim();
        if (!nuevotratamiento.matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            showAlert("Entrada Inválida", "El tratamiento de la mascota debe ser formato texto.");
            return ;
        }
        // Modificar la mascota en la base de datos
        modificarHistorial_clinicoEnBaseDeDatos(historial_clinico.getId(), nuevaIdMascota , nuevoDiagnosticoResult.get(), nuevaFecha, nuevoTratamientoResult.get()); 
        // Actualizar la lista observable
        loadHistorial_clinico();
        showAlert("Mascota Modificada", "La mascota ha sido modificada exitosamente.");
    }

    private void modificarHistorial_clinicoEnBaseDeDatos(int idHistorial_clinico, int nuevaMascota_id, String nuevoDiagnostico, int nuevaFecha, String nuevoTratamiento) {
        nuevoDiagnostico = nuevoDiagnostico.toUpperCase();
        nuevoTratamiento = nuevoTratamiento.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE historial_clinico SET idMascota = ?, diagnostico = ?, fecha = ?, tratamiento = ? WHERE idHistorial_clinico = ?")) {
            statement.setInt(1, nuevaMascota_id);
            statement.setString(2, nuevoDiagnostico);
            statement.setInt(3, nuevaFecha);
            statement.setString(4, nuevoTratamiento);
            statement.setInt(5, idHistorial_clinico);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void eliminarPropietario(Propietario propietario) {
        if (propietario == null) {
            showAlert("No se ha seleccionado una propietario", "Por favor, seleccione una propietario para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Propietario");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el propietario?: " + propietario.getId());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar propietario de la base de datos
            eliminarPropietarioDeBaseDeDatos(propietario.getId());

            // Actualizar la lista observable
            loadPropietario();
            showAlert("Propietario Eliminado", "Propietario eliminado exitosamente.");
        }
    }
    
    private void eliminarPropietarioDeBaseDeDatos(int idPropietario) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM propietario WHERE idPropietario = ?")) {
            statement.setInt(1, idPropietario);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarMascota(Mascota mascota) {
        if (mascota == null) {
            showAlert("No se ha seleccionado una mascota", "Por favor, seleccione una mascota para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Mascota");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar la mascota': " + mascota.getId());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar mascota de la base de datos
            eliminarMascotaDeBaseDeDatos(mascota.getId());

            // Actualizar la lista observable
            loadMascota();
            showAlert("Mascota Eliminada", "Mascota eliminada exitosamente.");
        }
    }
    
    private void eliminarMascotaDeBaseDeDatos(int idMascota) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM mascota WHERE idMascota = ?")) {
            statement.setInt(1, idMascota);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void eliminarHistorial_clinico(Historial_clinico historial_clinico) {
        if (historial_clinico == null) {
            showAlert("No se ha seleccionado un historial clinico", "Por favor, seleccione un historial clinico para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Historial Clinico");
        confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de que desea eliminar el historial clinico': " + historial_clinico.getId());
        
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar mascota de la base de datos
            eliminarHistorial_clinicoDeBaseDeDatos(historial_clinico.getId());

            // Actualizar la lista observable
            loadHistorial_clinico();
            showAlert("Mascota Eliminada", "Mascota eliminada exitosamente.");
        }
    }
    
    private void eliminarHistorial_clinicoDeBaseDeDatos(int idHistorial_clinico) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM historial_clinico WHERE idHistorial_clinico = ?")) {
            statement.setInt(1, idHistorial_clinico);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // Obtener el propietario por ID
    private boolean propietarioExists(int idPropietario) {
    boolean exists = false;
    try (PreparedStatement statement = dbConnection.prepareStatement("SELECT COUNT(*) FROM propietario WHERE idPropietario = ?")) {
        statement.setInt(1, idPropietario);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
    }

    
    // Obtener la mascota por ID
    private boolean mascotaExists(int idMascota) {
    boolean exists = false;
    try (PreparedStatement statement = dbConnection.prepareStatement("SELECT COUNT(*) FROM mascota WHERE idMascota = ?")) {
        statement.setInt(1, idMascota);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////    
    private void loadPropietario() {
        propietarioList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM propietario")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idPropietario");
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");

                Propietario propietario = new Propietario(id, nombre, direccion, telefono, email);
                propietarioList.add(propietario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadMascota() {
        mascotaList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM mascota")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idMascota");
                String nombre = resultSet.getString("nombre");
                int edad = resultSet.getInt("edad");
                String tipo = resultSet.getString("tipo");                
                int idPropietario = resultSet.getInt("idPropietario");

                Mascota mascota = new Mascota(id, nombre, edad, tipo, idPropietario);
                mascotaList.add(mascota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadHistorial_clinico() {
        historial_clinicoList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM historial_clinico")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idHistorial_clinico");
                int idMascota = resultSet.getInt("idMascota");
                String diagnostico = resultSet.getString("diagnostico");
                int fecha = resultSet.getInt("fecha");
                String tratamiento = resultSet.getString("tratamiento");

                Historial_clinico historial_clinico = new Historial_clinico(id, idMascota, diagnostico, fecha, tratamiento);
                historial_clinicoList.add(historial_clinico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////// 
    public static class Propietario {

        private final int id;
        private final String nombre;
        private final String direccion;
        private final String telefono;
        private final String email;

        public Propietario(int id, String nombre, String direccion, String telefono, String email) {
            this.id = id;
            this.nombre = nombre;
            this.direccion = direccion;
            this.telefono = telefono;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
        
        public String getDireccion() {
            return direccion;
        }
        
        public String getTelefono() {
            return telefono;
        }
        
        public String getEmail() {
            return email;
        }
    }

    public static class Mascota {

        private final int id;
        private final String nombre;
        private final int edad;
        private final String tipo;
        private final int idPropietario;

        public Mascota(int id, String nombre, int edad, String tipo, int idPropietario) {
            this.id = id;
            this.nombre = nombre;
            this.edad = edad;
            this.tipo = tipo;
            this.idPropietario = idPropietario;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
        
        public int getEdad() {
            return edad;
        }
        
        public String getTipo() {
            return tipo;
        }

        public int getIdPropietario() {
            return idPropietario;
        }
    }

    public static class Historial_clinico {

        private final int id;
        private final int idMascota;
        private final String diagnostico;
        private final int fecha;
        private final String tratamiento;

        public Historial_clinico(int id, int idMascota, String diagnostico, int fecha, String tratamiento) {
            this.id = id;
            this.idMascota = idMascota;
            this.diagnostico = diagnostico;
            this.fecha = fecha;
            this.tratamiento = tratamiento;
        }

        public int getId() {
            return id;
        }

        public int getIdMascota() {
            return idMascota;
        }

        public String getDiagnostico() {
            return diagnostico;
        }
        
        public int getFecha() {
            return fecha;
        }
        
        public String getTratamiento() {
            return tratamiento;
        }
    }
}
