package tanjiaming.moodtracker.model

import scalikejdbc.*
import scala.util.{Try, Success, Failure}
import tanjiaming.moodtracker.util.Database

class Users(val username: String, val password: String) extends Database:

  def save(): Try[Int] =
    if (!isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
            insert into Users (username, password)
            values ($username, $password)
          """.update.apply()
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
            update Users
            set password = $password
            where username = $username
          """.update.apply()
      })

  def delete(): Try[Int] =
    if (isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
            delete from Users
            where username = $username
          """.update.apply()
      })
    else
      throw new Exception("User not Exists in Database")

  def isExist: Boolean =
    DB readOnly { implicit session =>
      sql"""
          select * from Users
          where username = $username
        """.map(_.string("username")).single.apply()
    } match
      case Some(x) => true
      case None => false

  def validateLogin(inputPassword: String): Boolean =
    DB readOnly { implicit session =>
      sql"""
          select * from Users
          where username = $username
            and password = $inputPassword
        """
        .map(_.string("username"))
        .single
        .apply()
    } match
      case Some(_) => true
      case None => false
object Users extends Database:
  def initializeTable() =
    DB autoCommit { implicit session =>
      sql"""
        create table Users (
          id int not null GENERATED ALWAYS AS IDENTITY,
          username varchar(64),
          password varchar(64)
        )
      """.execute.apply()
    }

  def getAllUsers: List[Users] =
    DB readOnly { implicit session =>
      sql"select * from Users"
        .map(rs => new Users(
          rs.string("username"),
          rs.string("password")
        ))
        .list
        .apply()
    }