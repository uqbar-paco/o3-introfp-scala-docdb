//import org.scalatest.{FunSpec, Matchers}
//import ar.edu.unq.o3._
//
//class CalculadoraSpec extends FunSpec with Matchers {
//
//  type Instruccion = (Calculadora) => Calculadora
//
//  describe("Una Calculadora") {
//
//    it("debe aceptar un numero de entrada") {
//      Calculadora().presionar(Tecla.numero1).expresion should equal(Numero(1))
//    }
//
//    it("2 3  => 23") {
//      testear(
//        _.presionar(Tecla.numero2),
//        _.presionar(Tecla.numero3),
//      ).expresion should equal (Numero(23))
//    }
////
//    it("(2) (+) (2) (=) => 4") {
//      testear(
//        _.presionar(Tecla.numero2),
//        _.presionar(Tecla.suma),
//        _.presionar(Tecla.numero2),
//        _.presionar(Tecla.igual),
//      ).expresion should equal (Numero(4))
//    }
//
//    it("(+) (2) (=) => 2") {
//      testear(
//        _.presionar(Tecla.suma),
//        _.presionar(Tecla.numero2),
//        _.presionar(Tecla.igual),
//      ).expresion should equal (Numero(2))
//    }
//
////    it("(2) (+) (2) (=) (=) (=) => 8") {
////      testear(
////        _.presionar(Tecla.numero2),
////        _.presionar(Tecla.suma),
////        _.presionar(Tecla.numero2),
////        _.presionar(Tecla.igual),
////        _.presionar(Tecla.igual),
////        _.presionar(Tecla.igual),
////      ).expresion should equal (Numero(8))
////    }
//
//    it("debe 2 + 3 * 4 = 14") {
//      testear(
//        _.presionar(Tecla.numero2),
//        _.presionar(Tecla.suma),
//        _.presionar(Tecla.numero3),
//        _.presionar(Tecla.multiplicacion),
//        _.presionar(Tecla.numero4),
//        _.presionar(Tecla.igual),
//      ).expresion should equal (Numero(14))
//    }
//  }
//
//  describe("Expresiones") {
//    describe("Numero") {
//      it("(2) (+)  => (2 +)") {
//        Numero(2).recibirTecla(Tecla.suma) should equal(Operacion(Operaciones.+, Numero(2)))
//      }
//      it("(2) (3) =  => (23)") {
//        Numero(2).recibirTecla(Tecla.numero3) should equal(Numero(23))
//      }
//    }
//    describe("Suma") {
//
//      import Aliases.{ Suma, Multiplicacion }
//
//      describe("igual") {
//        it("(2 +) (=)  => (2 +)") {
//          Suma(Numero(2)).recibirTecla(Tecla.igual) should equal(Suma(Numero(2)))
//        }
//        it("(2 + (3 * 4) (=) => (14)") {
//          val multi = Some(Multiplicacion(Numero(3), Some(Numero(4))))
//          Suma(Numero(2), multi).recibirTecla(Tecla.igual) should equal(Numero(14))
//        }
//      }
//
//      it("(2 +) (1)  => (2 + 1)") {
//        Suma(Numero(2)).recibirTecla(Tecla.numero1) should equal(Suma(Numero(2), Some(Numero(1))))
//      }
//      it("(2 + 1) (2)  => (2 + 12)") {
//        Suma(Numero(2), Some(Numero(1))).recibirTecla(Tecla.numero2) should equal(Suma(Numero(2), Some(Numero(12))))
//      }
//      it("(2 + 3) (=)  => (5)") {
//        Suma(Numero(2), Some(Numero(3))).recibirTecla(Tecla.igual) should equal(Numero(5))
//      }
//      it("(2 + 3) (*) => (2 + Multiplicar(3))") {
//        Suma(Numero(2), Some(Numero(3))).recibirTecla(Tecla.multiplicacion) should equal(Suma(Numero(2), Some(Multiplicacion(Numero(3)))))
//      }
//
//      describe("combinando con multiplicacion") {
//        it("(2 +) (*) => (2 *)") {
//          Suma(Numero(2)).recibirTecla(Tecla.multiplicacion) should equal(Multiplicacion(Numero(2)))
//        }
//        it("(2 + Multiplicar(3)) (4) => (2 + (3 * 4)") {
//          Suma(Numero(2), Some(Multiplicacion(Numero(3)))).recibirTecla(Tecla.numero4) should equal(
//            Suma(Numero(2), Some(Multiplicacion(Numero(3), Some(Numero(4)))))
//          )
//        }
//      }
//    }
//  }
//
//  def flip[A, B, C](fn: (A,B)=> C)(b: B, a: A) = fn(a, b)
//
//  def testear(instrucciones: Instruccion*) = instrucciones
//    .foldLeft(Calculadora()) {
//      flip(_(_))
//    }
//
//}
