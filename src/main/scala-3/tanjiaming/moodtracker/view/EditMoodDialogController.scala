package tanjiaming.moodtracker.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, DatePicker, TextField}
import scalafx.scene.control.Alert
import scalafx.stage.Stage
import tanjiaming.moodtracker.model.MoodRecord

import java.time.LocalDate

@FXML
class EditMoodDialogController:

  @FXML private var txtMood: javafx.scene.control.TextField = null
  @FXML private var txtNote: javafx.scene.control.TextField = null
  @FXML private var datePicker: javafx.scene.control.DatePicker = null   // new
  @FXML private var btnOk: javafx.scene.control.Button = null
  @FXML private var btnCancel: javafx.scene.control.Button = null

  var dialogStage: Stage = null
  var mood: MoodRecord = null

  @FXML
  def handleOk(event: ActionEvent): Unit =
    try
      // Validate mood level
      val level = txtMood.getText.toInt
      if level < 1 || level > 5 then
        throw new NumberFormatException("Level must be 1-5")

      // Validate note
      val note = txtNote.getText
      
      // Validate date
      val date: LocalDate = Option(datePicker.getValue).getOrElse {
        val alert = new Alert(Alert.AlertType.Error):
          initOwner(dialogStage)
          title = "Invalid Input"
          headerText = "Date not selected"
          contentText = "Please select a valid date."
        alert.showAndWait()
        return
      }


      mood.noteProperty.value = note
      mood.dateProperty.value = date
      mood.moodLevel.value = level
      dialogStage.close()
      

    catch
      case _: NumberFormatException =>
        val alert = new Alert(Alert.AlertType.Error):
          initOwner(dialogStage)
          title = "Invalid Input"
          headerText = "Mood level must be a number between 1 and 5."
          contentText = "Please enter a valid number."
        alert.showAndWait()


  @FXML
  def handleCancel(event: ActionEvent): Unit =
    mood = null
    dialogStage.close()

  def setMood(m: MoodRecord): Unit =
    txtMood.setText(m.moodLevel.value.toString)
    txtNote.setText(m.noteProperty.value)
    datePicker.setValue(m.dateProperty.value)
    mood = m
