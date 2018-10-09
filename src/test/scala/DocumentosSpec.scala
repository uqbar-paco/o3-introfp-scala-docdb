import org.scalatest.{FunSpec, Matchers}
import ar.edu.unq.o3.documentos._
import ar.edu.unq.o3.documentos.Operaciones._

class DocumentosSpec extends FunSpec with Matchers {

  // { nombre: "Juan", apellido: "Perez", edad: 33 }
  // { nombre: "Juan", apellido: "Perez", edad: 33 }

  //db.collection('personas').aggregate({
  //  { $project: { nombre: "$nombre", apellido: "$apellido" } }
  //})

  describe("DocDB") {

    describe("project()") {

      it("debe permitir filtrar") {
        val juan = Document(List(
          Field("nombre", Text("Juan")),
          Field("apellido", Text("Perez")),
        ))

        val nombre = project("nombre")
        nombre(juan) should equal(Document(List(Field("nombre", Text("Juan")))))
      }

      it("debe permitir fields de tipo numero") {
        val juan = Document(List(
          Field("nombre", Text("Juan")),
          Field("apellido", Text("Perez")),
          Field("edad", Number(33)),
        ))

        val edad = project("edad")
        edad(juan) should equal(Document(List(Field("edad", Number(33)))))
      }

      it("debe permitir multiples fields") {
        val juan = Document(List(
          Field("nombre", Text("Juan")),
          Field("apellido", Text("Perez")),
          Field("edad", Number(33)),
        ))

        val nombreYEdad = project("nombre", "edad")
        nombreYEdad(juan) should equal(Document(List(Field("nombre", Text("Juan")), Field("edad", Number(33)))))
      }

//      it("debe permitir transformar fields") {
//        val juan = Document(List(
//          Field("nombre", Text("Juan")),
//          Field("apellido", Text("Perez")),
//        ))
//
//        val nombre = project(Map(
//          "nombre" -> Copy("nombre"),
//          "apellido" -> Substr(0, 3),
//        ))
//        nombre(juan) should equal(Document(List(
//          Field("nombre", Text("Juan")))),
//          Field("apellido", Text("Per"))
//        )
//      }

    }

    describe("Collection") {

      describe("aggregate()") {

        val juan = Document(List(
          Field("nombre", Text("Juan")),
          Field("apellido", Text("Perez")),
          Field("edad", Number(33)),
        ))

        val maria = Document(List(
          Field("nombre", Text("Maria")),
          Field("apellido", Text("Perez")),
          Field("edad", Number(27)),
        ))

        it("should be able to project()") {
          DbCollection(List(juan, maria)).aggregate(Project("edad")) should equal(List(
            Document(List(Field("edad", Number(33)))),
            Document(List(Field("edad", Number(27))))
          ))
        }

        it("should be able to aggregate two projects in a row") {
          DbCollection(List(juan, maria)).aggregate(
            Project("nombre", "apellido"),
            Project("nombre"),
          ) should equal(List(
            Document(List(Field("nombre", Text("Juan")))),
            Document(List(Field("nombre", Text("Maria"))))
          ))
        }

        it("should support match -> project") {
          DbCollection(List(juan, maria)).aggregate(
            Match(d => d.get("edad").map(_.value) match {
              case Some(Number(n)) if (n > 30) => true
              case _ => false
            }),
            Project("nombre"),
          ) should equal(List(
            Document(List(Field("nombre", Text("Juan")))),
          ))
        }
      }

    }

  }

}