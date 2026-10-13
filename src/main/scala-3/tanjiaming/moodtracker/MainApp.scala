package tanjiaming.moodtracker

import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import javafx.scene as jfxs
import scalafx.scene.control.Alert
import scalafx.scene.image.Image
import tanjiaming.moodtracker.util.Database
import tanjiaming.moodtracker.view.{LoginController, RegisterDialogController, SettingsDialogController}
import tanjiaming.moodtracker.model.Users

object MainApp extends JFXApp3:
  Database.setupDB()
  // Root layout
  var roots: Option[scalafx.scene.layout.BorderPane] = None
  var currentUsername: Option[String] = None
  var cssResource = getClass.getResource("view/DarkTheme.css")
  var lightMode: Boolean = false

  private def requireLogin(showPage: => Unit): Unit =
    currentUsername match
      case Some(username) => showPage
      case None =>
        val alert = new Alert(Alert.AlertType.Warning):
          initOwner(stage)
          title = "Access Denied"
          headerText = "Please log in first"
          contentText = "You must log in before accessing this page."
        alert.showAndWait()

  override def start(): Unit =
    // Load RootLayout.fxml
//    User.initializeTable()
    val loader = new FXMLLoader(getClass.getResource("view/RootLayout.fxml"))
    loader.load()
    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    // Pass MainApp reference to controller
    val controller = loader.getController[RootLayoutController]
    controller.mainApp = this

    // Set stage
    stage = new PrimaryStage:
      title = "Mood Tracker"
      icons += new Image(getClass.getResource("/images/smily.png").toExternalForm)
      scene = new Scene():
        root = roots.get
        stylesheets = Seq(cssResource.toExternalForm)

    // Start with login page
    this.showLogin()

  var loginController: LoginController = null

  // Page switching
  def showLogin(): Unit =
    val loader = new FXMLLoader(getClass.getResource("view/Login.fxml"))
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots

    loginController = loader.getController[LoginController]

    loginController.updateLoginScreen()


  def showMainMenu(): Unit =
    requireLogin {
      val loader = new FXMLLoader(getClass.getResource("view/ManageMood.fxml"))
      loader.load()
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.get.center = roots
    }
  def showRegister(): Unit =
    // Load FXML
    val loader = new javafx.fxml.FXMLLoader(getClass.getResource("view/RegisterDialog.fxml"))
    val root = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[RegisterDialogController]

    // Create a new stage for the dialog
    val stage = new scalafx.stage.Stage():
      title = "Register"
      initModality(scalafx.stage.Modality.ApplicationModal) // blocks main window
      scene = new scalafx.scene.Scene(root)

    // Assign stage to controller so it can close itself
    controller.dialogStage = stage

    stage.showAndWait()
  def handleLogout(): Unit =
    currentUsername = None

    loginController.updateLoginScreen()  // this will hide welcome, show login
    showLogin()

  def showSettings(): Unit =
    val loader = new javafx.fxml.FXMLLoader(getClass.getResource("view/SettingsDialog.fxml"))
    val root = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[SettingsDialogController]
    controller.initDialog(MainApp.lightMode)

    val stage = new scalafx.stage.Stage():
      title = "Settings"
      initModality(scalafx.stage.Modality.ApplicationModal)
      scene = new scalafx.scene.Scene(root)

    controller.dialogStage = stage
    stage.showAndWait()

  def setLightMode(enabled: Boolean): Unit =
    val sceneObj = roots.get.scene()
    sceneObj.stylesheets.clear() // always clear first
    if !enabled then
      // Dark mode
      val darkCss = getClass.getResource("view/DarkTheme.css").toExternalForm
      sceneObj.stylesheets.add(darkCss)

  @FXML
  def handleExit(): Unit =
    stage.close()
