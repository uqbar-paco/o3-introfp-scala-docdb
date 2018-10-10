import org.scalatest.{FunSpec, Matchers}
import ar.edu.unq.o3.documentos._
import ar.edu.unq.o3.documentos.ValueImplicits._

class DocumentosSpec extends FunSpec with Matchers {

  // Ejemplo de documentos de mongo
  // { nombre: "Morty", apellido: "Sanchez", edad: 13 }
  // { nombre: "Rick", apellido: "Sanchez", edad: 33 }


  // ejemplo de consulta aggregate en mongo con 2 pasos: filtrado (match), y luego proyección
  //db.collection('personas').aggregate([
  //  { $match: { edad: { $gte: 18 } } },
  //  { $project: { quien: "$nombre", edad: "$edad } }
  //])
  // produce
  //  [{ quien: "Rick", edad: 13 }]

  describe("DocDB") {

    describe("project()") {

      import ar.edu.unq.o3.documentos.Operations.{ projectDocument }

      it("debe proyectar 1 unico field") {

        val juan = Document(List(
          Field("nombre", Text("Juan")),
          Field("apellido", Text("Perez"))
        ))

        val proyectarNombre = projectDocument(List("nombre"))

        (proyectarNombre(juan)) should equal(Document(List(
          Field("nombre", "Juan") // los implicits nos permiten escribir "Juan" en lugar de Text("Juan")
        )))
      }

      it("debe proyectar 2 fields") {

        val juan = Document(List(
          Field("nombre", "Juan"),
          Field("apellido", "Perez"),
          Field("direccion", "Av siempre viva 1234")
        ))

        projectDocument(List("nombre", "apellido"))(juan) should equal(Document(List(
          Field("nombre", "Juan"),
          Field("apellido", "Perez"),
        )))
      }

      it("debe proyectar 1 campo number") {

        val juan = Document(List(
          Field("nombre", "Juan"),
          Field("apellido", "Perez"),
          Field("edad", 33) // implicit acá nos permite escribir 33 en lugar de Number(33)
        ))

        projectDocument(List("edad"))(juan) should equal(Document(List(
          Field("edad", 33),
        )))
      }

    }

    describe("aggregate()") {
      import ar.edu.unq.o3.documentos.AggregateSteps._

      implicit def tupleToField(t : (String, Int)) = Field(t._1, t._2)

      val juan = Document(List(
        Field("nombre", Text("Juan")),
        Field("apellido", Text("Perez")),
        "edad" -> 17   // implicits acá nos permite escribir una Tupla(String, Int) en lugar de Field("edad", 17)
      ))

      val morty = Document(List(
        Field("nombre", Text("Morty")),
        Field("apellido", Text("Sanchez")),
        "edad" -> 67
      ))

      it("should perform a single project") {
        DBCollection(List(
          juan,
          morty
        )).aggregate(List(
          // el match está un poco "crudo" recibe una funcion (Document) => Boolean
          // para ver si pasa. Podríamos trabajarlo para parecerse a mongo
          matching(d => d.get("edad") match {
            case Some(Number(edad)) if (edad >= 18) => true
            case _ => false
          }),
          project(List("nombre")),
        )) should equal(List(
          Document(List(Field("nombre", "Morty"))),
        ))
      }
    }

  }

}