package tanjiaming.moodtracker.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import javafx.scene.control.{PasswordField, TextField}
import scalafx.scene.control.Alert
import scalafx.stage.Stage
import tanjiaming.moodtracker.MainApp
import tanjiaming.moodtracker.model.Users
import scala.util.{Success, Failure}
import scala.util.CommandLineParser.showError

class RegisterDialogController:

    @FXML private var usernameField: TextField = null
    @FXML private var passwordField: PasswordField = null
    @FXML private var confirmPasswordField: PasswordField = null

    var dialogStage: Stage = _

    def showErrorAlert(
                        titleText: String,
                        header: String,
                        content: String
                      ): Unit =
      val alert = new Alert(Alert.AlertType.Error):
        initOwner(dialogStage)
        title = titleText
        headerText = header
        contentText = content
      alert.showAndWait()

    @FXML
    def handleOk(event: ActionEvent): Unit =
      val username = usernameField.getText
      val password = passwordField.getText
      val confirm = confirmPasswordField.getText

      if username.isEmpty || password.isEmpty || confirm.isEmpty then
        showErrorAlert("Invalid Input", "Missing Fields", "Please fill in all fields.")
        return

      if password != confirm then
        showErrorAlert("Invalid Input", "Password Mismatch", "Passwords do not match.")
        return

      val newUser = new Users(username, password)

      if newUser.isExist then
        showErrorAlert("Invalid Input", "User Exists", "Username already taken.")
        return

      newUser.save() match
        case Success(_) =>
          dialogStage.close()
        case Failure(_) =>
          showErrorAlert(
            "Database Error",
            "Failed to Save",
            "Could not save user."
      )
    @FXML
    def handleCancel(event: ActionEvent): Unit =
      dialogStage.close()
