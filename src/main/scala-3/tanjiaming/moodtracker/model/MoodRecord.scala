package tanjiaming.moodtracker.model

import scalafx.beans.property.{StringProperty, ObjectProperty}
import java.time.LocalDate
import scalikejdbc._
import scala.util.{Try, Success, Failure}
import tanjiaming.moodtracker.util.Database

class MoodRecord(level: Int, noteText: String, dateValue: LocalDate) extends Database:

  def this() = this(0, "", LocalDate.now())

  private val _moodLevel = ObjectProperty[Int](level)
  private val _note      = StringProperty(noteText)
  private val _date      = ObjectProperty[LocalDate](dateValue)

  
  def moodLevel     = _moodLevel
  def noteProperty  = _note
  def dateProperty  = _date

  def save(): Try[Int] =
    if (!isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
        insert into mood (mood, note, date)
        values (${_moodLevel.value}, ${_note.value}, ${_date.value.toString})
      """.update.apply()
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
      update mood
      set mood = ${_moodLevel.value},
          note = ${_note.value},
          date = ${_date.value.toString}
      where date = ${_date.value.toString}
    """.update.apply()
      })

  def delete(): Try[Int] = {
    if (isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
      delete from mood
      where mood = ${_moodLevel.value}
        and date = ${_date.value.toString}
    """.update.apply()
      })
    else
      throw new Exception("Mood not Exists in Database")
  }


  def isExist: Boolean =
    DB readOnly { implicit session =>
      sql"""
        select * from mood
        where mood = ${_moodLevel.value}
          and note = ${_note.value}
          and date = ${_date.value.toString}
      """.map(_.int("mood")).single.apply()
    } match
      case Some(_) => true
      case None => false


object MoodRecord extends Database:

  def initializeTable() =
    DB autoCommit { implicit session =>
      sql"""
        create table mood (
          id int not null GENERATED ALWAYS AS IDENTITY,
          mood int,
          note varchar(255),
          date varchar(64)
        )
      """.execute.apply()
    }

  def getAllMoods: List[MoodRecord] =
    DB readOnly { implicit session =>
      sql"select * from mood"
        .map(rs =>
          new MoodRecord(
            rs.int("mood"),
            rs.string("note"),
            LocalDate.parse(rs.string("date"))
          )
        ).list.apply()
    }