package taller

import scala.annotation.tailrec

/**
 * Taller 1 — cifrados clásicos con recursión.
 *
 * Solo se cifran las 26 letras minúsculas del alfabeto inglés; cualquier otro
 * carácter se copia sin cambio.
 */
class CifradosClasicos {

  type Mensaje = String
  type Clave = String

  // Una frecuencia asocia cada letra con las veces que aparece.
  type Frecuencias = List[(Char, Int)]

  val letras = 26
  val primera = 'a'.toInt

  def esMinuscula(c: Char): Boolean = c >= 'a' && c <= 'z'
  def desplazar(letra: Char, k: Int): Char =
    if(esMinuscula(letra))
      (primera + Math.floorMod(letra - primera + k, letras)).toChar
    else letra


  // Punto 1 -------------------------------------------------------------------

  /** César con recursión lineal: una operación pendiente por letra. */

  def cesar(m: Mensaje, k: Int): Mensaje =
    if(m.isEmpty()) ""
    else desplazar(m(0),k) + cesar(m.substring(1),k)




  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con
   * @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
    @tailrec
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje =
    if(m.isEmpty) acc
    else cesarCola(m.substring(1),k, acc + desplazar(m(0),k))

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = {

    def aux(RestoPalabra: String, acc: Frecuencias): Frecuencias = {

      if(RestoPalabra.isEmpty) acc
      else {

        val letra = RestoPalabra.head

        if (letra >= 'a'  && letra <= 'z') {

          val existe = acc.exists(x => x._1 == letra)

          if (existe)
            aux(RestoPalabra.tail, acc)
          else
            aux(RestoPalabra.tail, (letra, 1) :: acc)
        }
        else {
          aux(RestoPalabra.tail, acc)
        }
      }
    }

    aux(m, List()).sortBy(x => (-x._2, x._1))
  }
  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = ???

  def romperCesar(m: Mensaje): Mensaje = ???

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = ???

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = ???
}
