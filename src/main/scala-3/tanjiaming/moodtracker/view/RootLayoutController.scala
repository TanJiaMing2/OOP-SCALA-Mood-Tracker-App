package tanjiaming.moodtracker

import scalafx.scene.control.Button
import javafx.scene.control.{Button => JFXButton}
import javafx.fxml.FXML

class RootLayoutController:

  @FXML var btnLogin: JFXButton = null
  @FXML var btnManage: JFXButton = null
  @FXML var btnLogout: JFXButton = null
  @FXML var btnSetting: JFXButton = null
  @FXML var btnExit: JFXButton = null

  var mainApp: MainApp.type = _

  def initialize(): Unit =
    // convert to ScalaFX Button for ScalaFX syntax
    val sfxLogin = new Button(btnLogin)
    val sfxManage = new Button(btnManage)
    val sfxLogout = new Button(btnLogout)
    val sfxSetting = new Button(btnSetting)
    val sfxExit = new Button(btnExit)

    sfxLogin.onAction = _ => mainApp.showLogin()
    sfxManage.onAction = _ => mainApp.showMainMenu()
    sfxLogout.onAction = _ => mainApp.handleLogout()
    sfxSetting.onAction = _ => mainApp.showSettings()
    sfxExit.onAction = _ => mainApp.handleExit()