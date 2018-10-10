package ar.edu.unq.o3.documentos {

  /**
   * Type aliases para nombrar tipos de funciones genéricas
   */
  object Types {
    type AggregateStep = List[Document] => List[Document]
    type DocumentFilter = (Document) => Boolean
  }

  /**
    * Pasos en el aggregate
    */
  object AggregateSteps {

    def project(fields: List[String])(docs:List[Document])=
      docs.map(Operations.projectDocument(fields))

    def matching(filter: Types.DocumentFilter)(docs:List[Document]) =
      docs.filter(filter)

    // podríamos agregar un $groupBy, $count, $limit, etc
    // https://docs.mongodb.com/manual/reference/operator/aggregation/count/
  }

  //
  // Modelo de objetos de documentos
  //

  trait Value

  case class Number(number: Int) extends Value
  case class Text(text: String) extends Value

  case class Field(name: String, value: Value)
  case class Document(fields: List[Field]) extends Value {

    def get(fieldName: String) = fields
      .find(_.name == fieldName)
      .map(_.value)

    def filter(_fields: List[String]) = {
      copy(fields = fields.filter(f => _fields.contains(f.name)))
    }
  }

  case class DBCollection(documents: List[Document]) {
    import Types._

    def aggregate(steps: List[AggregateStep]) : List[Document] = steps.foldLeft(documents) {
      (accDocs, step) => step(accDocs)
    }

  }

  /**
   * Utils para construir documentos con una sintaxis más corta
   */
  object ValueImplicits {
    implicit def stringToText = Text(_)
    implicit def intToNumber = Number(_)
  }

  /**
   * Operaciones sobre documentos
   */
  object Operations {
    def projectDocument(fields: List[String]) = (doc: Document) => doc.filter(fields)
  }


}

