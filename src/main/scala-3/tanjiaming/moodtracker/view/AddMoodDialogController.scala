package tanjiaming.moodtracker.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import javafx.scene.control.{Button, TextField, DatePicker}
import scalafx.scene.control.Alert
import scalafx.stage.Stage
import java.time.LocalDate
import tanjiaming.moodtracker.model.MoodRecord

@FXML
class AddMoodDialogController:

  @FXML private var txtMood: javafx.scene.control.TextField = null
  @FXML private var txtNote: javafx.scene.control.TextField = null
  @FXML private var datePicker: javafx.scene.control.DatePicker = null   // new
  @FXML private var btnOk: javafx.scene.control.Button = null
  @FXML private var btnCancel: javafx.scene.control.Button = null

  var dialogStage: Stage = null
  var mood: MoodRecord = null

  def setMood(m: MoodRecord): Unit =
    txtMood.setText(m.moodLevel.value.toString)
    txtNote.setText(m.noteProperty.value)
    datePicker.setValue(m.dateProperty.value)
    mood = m

  @FXML
  def handleOk(event: ActionEvent): Unit =
    try
      val level = txtMood.getText.toInt
      if level < 1 || level > 5 then
        throw new NumberFormatException("Level must be 1-5")

      val note = txtNote.getText
      val date = Option(datePicker.getValue).getOrElse(LocalDate.now())

      if mood == null then
        // Adding new mood
        mood = new MoodRecord(level, note, date)
      else
        // Editing existing mood
        mood.moodLevel.value = level
        mood.noteProperty.value = note
        mood.dateProperty.value = date

      dialogStage.close()
    catch
      case _: NumberFormatException =>
        val alert = new scalafx.scene.control.Alert(scalafx.scene.control.Alert.AlertType.Error):
          initOwner(dialogStage)
          title = "Invalid Input"
          headerText = "Mood level must be a number between 1 and 5."
          contentText = "Please enter a valid number."
        alert.showAndWait()



  @FXML
  def handleCancel(event: ActionEvent): Unit =
    mood = null
    dialogStage.close()
