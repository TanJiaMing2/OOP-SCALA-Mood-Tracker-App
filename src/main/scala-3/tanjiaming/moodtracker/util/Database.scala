package tanjiaming.moodtracker.util
import tanjiaming.moodtracker.model.{MoodRecord, Users}
import scalikejdbc.*

trait Database:
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:moodDB;create=true;";

  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")

  given AutoSession = AutoSession

object Database extends Database:
  def setupDB(): Unit =
    if (!hasDBInitialize) then
      MoodRecord.initializeTable()
      Users.initializeTable()

    DB autoCommit { implicit session =>
      sql"delete from Users".update.apply()}

  def hasDBInitialize: Boolean =
    val moodExists = DB getTable "Mood" match
      case Some(_) => true
      case None => false

    val userExists = DB getTable "Users" match
      case Some(_) => true
      case None => false

    moodExists && userExists