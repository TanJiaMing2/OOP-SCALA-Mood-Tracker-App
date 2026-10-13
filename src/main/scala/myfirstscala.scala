import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

object MyApp extends JFXApp3: // must use jfxapp3

  override def start(): Unit =
    stage = new PrimaryStage()


end MyApp
