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
    else desplazar(m.head,k) + cesar(m.tail,k)




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
    else cesarCola(m.tail,k, acc + desplazar(m.head,k))

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */

  def frecuencias(m: Mensaje): Frecuencias = {
    @tailrec
    def aux(RestoPalabra: String, acc: Frecuencias): Frecuencias = {

      if(RestoPalabra.isEmpty) acc
      else {

        val letra = RestoPalabra.head

        if (letra >= 'a'  && letra <= 'z') {

          val existe = acc.exists(x => x._1 == letra)

          if (existe)
            aux(RestoPalabra.tail, acc.map(x => if
            (x._1 == letra) (x._1, x._2 + 1)
            else x) )
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
  def desplazamientoProbable(m: Mensaje): Int = {
    val lista = frecuencias(m)

    if (lista.isEmpty) 0
    else Math.floorMod(lista.head._1 - 'e', letras)
  }

  def romperCesar(m: Mensaje): Mensaje =
    cesar(m, -desplazamientoProbable(m))

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt =
    if (n == 0) BigInt(1)
    else if (n == 1) BigInt(a)
    else BigInt(a - 1) * combinaciones(n - 1, a)

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = {
  @tailrec
  def aux(resto: Mensaje, claveRestante: Clave, acc: Mensaje): Mensaje =
    if (resto.isEmpty) acc
    else if (!esMinuscula(resto.head))
      aux(resto.tail, claveRestante, acc + resto.head)
    else if (claveRestante.isEmpty)
      aux(resto, clave, acc)
    else
      aux(resto.tail, claveRestante.tail, acc + desplazar(resto.head, claveRestante.head - 'a') )
  if (clave.isEmpty) m
  else aux(m, clave, "")
}
}
