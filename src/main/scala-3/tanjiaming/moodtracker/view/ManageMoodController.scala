package tanjiaming.moodtracker.view

import javafx.event.ActionEvent
import tanjiaming.moodtracker.model.MoodRecord
import scalafx.collections.ObservableBuffer
import javafx.scene.control.{TableColumn, TableView}
import scalafx.Includes.*
import javafx.fxml.{FXML, FXMLLoader}

import java.time.LocalDate
import scalafx.stage.{Modality, Stage}
import scalafx.scene.Scene
import javafx.scene as jfxs
import scalafx.scene.control.Alert
import tanjiaming.moodtracker.MainApp

import scala.util.{Failure, Success}

val moodData = ObservableBuffer[MoodRecord]()

@FXML
class ManageMoodController:

  @FXML
  private var moodTable: TableView[MoodRecord] = null
  @FXML
  private var moodColumn: TableColumn[MoodRecord, Int] = null
  @FXML
  private var noteColumn: TableColumn[MoodRecord, String] = null

  @FXML
  private var dateColumn: TableColumn[MoodRecord, java.time.LocalDate] = null
  // Temporary data
//  private val moodData = ObservableBuffer(
//    new MoodRecord(3, "Okay day", LocalDate.now()),
//    new MoodRecord(5, "Very happy", LocalDate.now())
//  )

  @FXML
  private var btnAddMood: javafx.scene.control.Button = null
  @FXML
  private var btnEditMood: javafx.scene.control.Button = null
  @FXML
  private var btnDelMood: javafx.scene.control.Button = null
  @FXML
  private var btnGuidesMood: javafx.scene.control.Button = null
  @FXML
  private var guideText: javafx.scene.control.TextArea = null

  @FXML
  def initialize(): Unit =


    moodTable.items = moodData

    // IMPORTANT: ObjectProperty[Int] → .value
    moodColumn.cellValueFactory = { _.value.moodLevel }  // Int → ObjectProperty[Int]
    noteColumn.cellValueFactory = { _.value.noteProperty }  // String → StringProperty
    dateColumn.cellValueFactory = { _.value.dateProperty }  // LocalDate → ObjectProperty[LocalDate]
    moodData.onChange { (_, _) =>
      updateGuide()
    }


  def handleAddMood(event: ActionEvent): Unit =
    val loader = new javafx.fxml.FXMLLoader(getClass.getResource("AddMoodDialog.fxml"))
    val root = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[AddMoodDialogController]

    val stage = new scalafx.stage.Stage():
      title = "Add Mood"
      initModality(scalafx.stage.Modality.ApplicationModal)
      scene = new scalafx.scene.Scene(root)

    controller.dialogStage = stage
    stage.showAndWait() // wait until the user closes the dialog

    // After dialog closes
    if controller.mood != null then
      controller.mood.save() match
        case Success(_) =>
          moodData += controller.mood // update the table
        case Failure(_) =>
          val alert = new Alert(Alert.AlertType.Warning):
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem failed to save mood"
          alert.showAndWait()


  def handleEditMood(event: ActionEvent): Unit =
    val selected = moodTable.selectionModel().selectedItem.value
    if selected != null then
      val loader = new javafx.fxml.FXMLLoader(getClass.getResource("AddMoodDialog.fxml"))
      val root = loader.load[javafx.scene.Parent]()
      val controller = loader.getController[AddMoodDialogController]

      // Pass the selected mood to the dialog
      controller.setMood(selected)

      val stage = new scalafx.stage.Stage():
        title = "Edit Mood"
        initModality(scalafx.stage.Modality.ApplicationModal)
        scene = new scalafx.scene.Scene(root)

      controller.dialogStage = stage
      stage.showAndWait() // wait for dialog

      // After dialog closes
      if controller.mood != null then
        controller.mood.save() match
          case Success(_) =>
            // Update table view (refresh to show changes)
            val index = moodData.indexOf(selected)
            if index >= 0 then
              moodData.update(index, controller.mood)
          case Failure(_) =>
            val alert = new Alert(Alert.AlertType.Warning):
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem failed to update mood"
            alert.showAndWait()
    else
      // Nothing selected
      val alert = new Alert(Alert.AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Mood Selected"
        contentText = "Please select a mood in the table."
      alert.showAndWait()


    moodTable.refresh() // Refresh table to show updated values



  def handleDeleteMood(event: ActionEvent): Unit =
    val selectedIndex = moodTable.selectionModel().selectedIndex.value
    val selectedMood = moodTable.selectionModel().selectedItem.value

    if (selectedIndex >= 0 && selectedMood != null) then
      selectedMood.delete() match
        case Success(_) =>
          moodTable.items().remove(selectedIndex)
        case Failure(_) =>
          val alert = new Alert(Alert.AlertType.Warning):
            initOwner(MainApp.stage)
            title = "Failed to Delete"
            headerText = "Database Error"
            contentText = "Database problem failed to delete mood"
          alert.showAndWait()
    else
      val alert = new Alert(Alert.AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Mood Selected"
        contentText = "Please select a mood in the table."
      alert.showAndWait()



  def handleGuides(event: ActionEvent): Unit =
    val loader = new FXMLLoader(getClass.getResource("GuideDialog.fxml"))
    val root = loader.load[jfxs.Parent]()

    val controller = loader.getController[GuideDialogController]

    val stage = new Stage():
      title = "Mood Improvement Guide"
      initModality(Modality.ApplicationModal)
      scene = new Scene(root)

    controller.dialogStage = stage
    stage.showAndWait()

  def averageMood(): Double =
    if moodData.isEmpty then 0.0
    else moodData.map(_.moodLevel.value).sum.toDouble / moodData.size

  def updateGuide(): Unit =
    val avg = averageMood()

    val message =
      if avg == 0 then
        "Start adding mood records to receive guidance."
      else if avg < 2.5 then
        "Your mood seems low recently. Consider resting more, talking to someone you trust, or doing small activities you enjoy."
      else if avg < 4 then
        "Your mood is moderate. Try maintaining a balanced routine and keep track of what makes you feel better."
      else
        "Great job! Your mood is generally positive. Keep up the healthy habits!"

    guideText.setText(message)