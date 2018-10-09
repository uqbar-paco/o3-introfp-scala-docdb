package ar.edu.unq.o3.documentos {

  // general types

  object Types {
    type Filter = Field => Boolean
    type DocumentFilter = (Document) => Boolean
  }

  object Operaciones {
    def project(spec: String*) = (doc: Document) => doc.filter(f => spec.toList.contains(f.name))
  }


  //
  // aggregate
  //

  trait AggregateStep {
    def perform(docs: List[Document]): List[Document]
  }

  case class Project(spec: String*) extends AggregateStep {
    import Operaciones._
    override def perform(docs: List[Document]): List[Document] = docs.map(project(spec:_*))
  }
  case class Match(filter: Types.DocumentFilter) extends AggregateStep {
    override def perform(docs: List[Document]): List[Document] = docs.filter(filter)
  }

  // Document model

  trait Value
  case class Number(numero: Int) extends Value
  case class Text(texto: String) extends Value

  case class Field(name: String, value: Value)

  case class Document(fields: List[Field]) extends Value {
    import Types._

    def filter(filter: Filter) = copy(fields = fields.filter(filter))
    def get(fieldName: String) = fields.find(_.name == fieldName)
  }

  case class DbCollection(documents: List[Document]) {
    import Types._

    def aggregate(steps: AggregateStep*) = steps.foldLeft(documents) {
      (docs, step) => step.perform(docs)
    }
  }

}

