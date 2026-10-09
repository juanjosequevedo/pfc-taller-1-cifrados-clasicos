package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PruebasPropiasTest extends AnyFunSuite {
  val d = new CifradosClasicos()
  import d._
  // Punto 1: Pruebas Enunciado cesar -----------------
  test("cesar: don cesar dando varias vueltas al alfabeto"){
    assert(cesar("xyz",78) == "xyz")
  }

  test("cesar: conserva mayúsculas, espacios y signos, a ver si se rompe el prorama") {
    assert(cesar("Hola, Zorro! 123", 2) == "Hqnc, Zqttq! 123")
  }

  test("cesar: cadena larga de letras, en este si se rompe yo creo") {
    assert(cesar("abcdefghijklmnopqrstuvwxyz",4) == "efghijklmnopqrstuvwxyzabcd")
  }

  test("cesar: con signos y espacios vacíos de por medio, con esto engaño al programa"){
    assert(cesar("a-b c!",-2) == "y-z a!")
  }

  test("cesar: solo símbolos y números, depronto se confunde con alguno...") {
    assert(cesar("123 !?,.-", 7) == "123 !?,.-")
  }
  // Punto 2: Pruebas enunciado cesarCola

  test("cesarCola: descifrar recupera el mensaje original... yo creo que falla") {
    val mensaje = "prueba de cola, con signos! 456"
    assert(cesarCola(cesarCola(mensaje, 17), -17) == mensaje)
  }

  test("cesarCola: coincide con cesar en distintos casos, o tal vez no...") {
    val casos = List(
      ("xyz", 4),
      ("abc", -1),
      ("Scala 3!", 7),
      ("zebra", 52),
      ("", -20),
      ("a, B! 123", -28)
    )

    assert(casos.forall { case (m, k) =>
      cesarCola(m, k) == cesar(m, k)
    })
  }

  test("cesarCola: una clave grande equivale a su resto módulo 26, quizás...") {
    assert(cesarCola("perro", 53) == "qfssp")
  }

  test("cesarCola: a ver si rompemos la pila") {
    val largo = "zxyabcqwep" * 4000
    assert(cesarCola(largo, 7).length == 40000)
  }

  test("cesarCola: otro con más de lo mismo...") {
    assert(cesarCola("Hola, mundo! 2026", 2) == "Hqnc, owpfq! 2026")
  }

  // Punto 3: Pruebas enunciado frecuencias -------------

  test("frecuencias: caso pesado con mayúsculas, signos, números y empates") {
    assert(
      frecuencias("bAaa!ccC??ddd-EE111ffgGg") ==
        List(('d', 3), ('a', 2), ('c', 2), ('f', 2), ('g', 2), ('b', 1))
    )
  }

  test("frecuencias: mezcla de frecuencias y empates") {
    assert(
      frecuencias("zzzyyyxxwwwwvv") ==
        List(('w', 4), ('y', 3), ('z', 3), ('v', 2), ('x', 2))
    )
  }

  test("frecuencias: solo mayúsculas, símbolos y números") {
    assert(
      frecuencias("ABC XYZ!! 123 #$%") == Nil
    )
  }

  test("frecuencias: muchos empates en orden alfabético") {
    assert(
      frecuencias("ddccbbaa") ==
        List(('a', 2), ('b', 2), ('c', 2), ('d', 2))
    )
  }

  test("frecuencias: texto complejo con caracteres ignorados, repeticiones y empates") {
    assert(
      frecuencias("ZZzaaaBBBcc!!1122ddd-eeffGGg") ==
        List(('a', 3), ('d', 3), ('c', 2), ('e', 2), ('f', 2), ('g', 1), ('z', 1))
    )
  }

  // Punto 4: Enunciado desplazamiento probable y romper cesar -------------

  test("desplazamientoProbable: la letra está antes de e en el alfabeto") {
    assert(desplazamientoProbable("b") == 23)
  }

  test("desplazamientoProbable: encuentra el desplazamiento entre varias frecuencias") {
    assert(desplazamientoProbable("xxxyyyyyhh") == 20)
  }

  test("desplazamientoProbable: ignora mayúsculas, números y símbolos") {
    assert(desplazamientoProbable("HHH!!!222hhh") == 3)
  }

  test("desplazamientoProbable: empate entre letras no consecutivas") {
    // 'a' y 'h' aparecen dos veces; gana 'a' por orden alfabético.
    assert(desplazamientoProbable("ahha") == 22)
  }

  test("romperCesar: recupera un mensaje largo cifrado con clave positiva") {
    val original = "este mensaje secreto contiene muchas letras e"
    assert(romperCesar(cesar(original, 7)) == original)
  }

  test("romperCesar: recupera un mensaje cifrado con clave negativa") {
    val original = "el secreto esta entre estas letras"
    assert(romperCesar(cesar(original, -4)) == original)
  }

  test("romperCesar: el resultado puede diferir si otra letra es la más frecuente") {
    val original = "aaaa casa amarilla"
    assert(romperCesar(cesar(original, 7)) != original)
  }

  // Punto 5: Enunciado de combinaciones y Vigenerè --------------

  test("combinaciones: longitud 4 sobre un alfabeto de 3") {
    assert(combinaciones(4, 3) == BigInt(24))
  }

  test("combinaciones: longitud 6 sobre un alfabeto de 5") {
    assert(combinaciones(6, 5) == BigInt(5120))
  }

  test("combinaciones: longitud 8 sobre 26 con BigInt") {
    assert(combinaciones(8, 26) == BigInt("158691406250"))
  }

  test("vigenere: mensaje más largo que la clave") {
    assert(vigenere("programar", "sol") == "hfzyfleoc")
  }

  test("vigenere: la clave es más larga que el mensaje") {
    assert(vigenere("sol", "programacion") == "hfz")
  }

  test("vigenere: la clave se repite y los espacios se conservan") {
    assert(vigenere("abc xyz", "bc") == "bdd zzb")
  }

}