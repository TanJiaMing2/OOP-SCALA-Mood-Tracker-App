package tanjiaming.moodtracker.view

import scalafx.stage.Stage
import javafx.fxml.FXML
import javafx.event.ActionEvent
import javafx.scene.control.{Button, CheckBox}
import tanjiaming.moodtracker.MainApp

class SettingsDialogController:
  @FXML private var lightModeCheck: CheckBox = null
  @FXML private var btnOk: Button = null
  @FXML private var btnCancel: Button = null

  var dialogStage: Stage = _

  @FXML
  def handleOk(event: ActionEvent): Unit =
    // apply settings here, e.g., light mode
//    println(s"Light mode: ${lightModeCheck.isSelected}")
    dialogStage.close()

  @FXML
  def handleCancel(event: ActionEvent): Unit =
    dialogStage.close()

  @FXML
  def handleToggleLightMode(event: ActionEvent): Unit =
    if lightModeCheck.isSelected then
      // Light mode → remove DarkTheme.css
      MainApp.setLightMode(true)
    else
      // Dark mode → re-add DarkTheme.css
      MainApp.setLightMode(false)

  def initDialog(currentLightMode: Boolean): Unit =
    lightModeCheck.setSelected(currentLightMode)