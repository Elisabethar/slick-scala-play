package domain

import com.google.inject.ImplementedBy
import models.CatModel.{Cat, Cats}
//import slick.driver.MySQLDriver.api._
import slick.driver.PostgresDriver.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import java.sql._;

@ImplementedBy(classOf[CatDAO])
trait DAOTrait {
  def getAll: Seq[Cat]
  def findById(id: Int): Cat
  def insert(cat: Cat): Unit
  def update(id: Int, cat: Cat): Unit
  def delete(id: Int): Unit
}

class CatDAO extends DAOTrait{

  val cats : TableQuery[Cats] = TableQuery[Cats]

  val db = Database.forConfig("postregsqldb")
  //val db = Database.forURL("postgres://wnhdwpwbdrgsqf:Nwr9xQfhUDGHRccsVT9tEIuB7o@ec2-50-16-229-91.compute-1.amazonaws.com:5432/dti0r4dk2pvi7", "wnhdwpwbdrgsqf", "Nwr9xQfhUDGHRccsVT9tEIuB7o")

  def filterQuery(id: Int): Query[Cats, Cat, Seq] =
    cats.filter(_.id === id)

  override def getAll : Seq[Cat] =

    Await.result(db.run(cats.result), Duration.Inf)

  override def findById(id: Int): Cat =
    Await.result (db.run (filterQuery (id).result.head), Duration.Inf)

  override def insert(cat: Cat): Unit =
    db.run(cats += cat)

  override def update(id: Int, cat: Cat): Unit =
    db.run(filterQuery(id).update(cat))


  override def delete(id: Int): Unit =
    db.run(filterQuery(id).delete)



}
