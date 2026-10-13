package tanjiaming.moodtracker.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import scalafx.stage.Stage

@FXML
class GuideDialogController:

  var dialogStage: Stage = null

  @FXML
  def handleClose(event: ActionEvent): Unit =
    dialogStage.close()
