package tanjiaming.moodtracker.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import javafx.scene.control.{Button, Label, PasswordField, TextField}
import javafx.scene.layout.AnchorPane
import scalafx.scene.control.Alert
import scalafx.stage.Stage
import tanjiaming.moodtracker.MainApp
import tanjiaming.moodtracker.model.Users


class LoginController:

  @FXML private var usernameField: TextField = null
  @FXML private var passwordField: PasswordField = null
  @FXML
  private var btnRegister: Button = null
  @FXML private var loginPane: AnchorPane = null
  @FXML private var welcomePane: AnchorPane = null
  @FXML private var welcomeLabel: Label = null
  var loggedInUser: Option[String] = None

  @FXML
  def handleRegister(event: ActionEvent): Unit =
    MainApp.showRegister()


  def updateLoginScreen(): Unit =
    if MainApp.currentUsername.isDefined then
      loginPane.setVisible(false)
      welcomePane.setVisible(true)
      welcomeLabel.setText(s"Welcome, ${MainApp.currentUsername.get}!")
    else
      loginPane.setVisible(true)
      welcomePane.setVisible(false)

  def showWelcomeScreen(username: String): Unit =
    loginPane.setVisible(false)
    welcomePane.setVisible(true)
    welcomeLabel.setText(s"Welcome, $username")

  var dialogStage: Stage = null
  def showErrorAlert(titleText: String, header: String, content: String): Unit =
    val alert = new Alert(Alert.AlertType.Error):
      initOwner(dialogStage)
      title = titleText
      headerText = header
      contentText = content
    alert.showAndWait()

  @FXML
  def handleLogin(event: ActionEvent): Unit =
    val username = usernameField.getText
    val password = passwordField.getText

    val user = new Users(username, password)

    if user.validateLogin(password) then
      MainApp.currentUsername = Some(username) // store the logged-in user
      showWelcomeScreen(username)
    else

      showErrorAlert(
        "Login Failed",
        "Invalid Credentials",
        "Username or password incorrect."
      )