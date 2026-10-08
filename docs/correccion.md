Correción:

# Informe de corrección — Taller 1: Cifrados clásicos

Para justificar las funciones seguimos el ejemplo del profesor.
Usamos inducción en los procesos recursivos e invariantes en los de cola.

La hipótesis de inducción supone que un caso más pequeño funciona
y permite justificar el siguiente. Un invariante es una condición
que se mantiene mientras avanza el proceso.

Solo se cifran las minúsculas de `a` a `z`. Los demás caracteres
se conservan.

## 1. Corrección de `cesar`

### Qué debe calcular

Llamamos $D(c,k)$ al desplazamiento de un carácter y $C(m,k)$
al cifrado correcto del mensaje.

En las fórmulas, `+` une cadenas y `""` representa el mensaje vacío.
Si `c` es el primer carácter y `r` es el resto:

$$
C("",k)=""
$$

$$
C(c+r,k)=D(c,k)+C(r,k)
$$

`desplazar` reconoce las minúsculas y calcula su nueva posición
dentro de las 26 letras. `Math.floorMod` permite volver al inicio
del alfabeto y manejar desplazamientos negativos.

Al reducir primero `k` con `Math.floorMod(k, letras)`, la suma usa
valores pequeños. Quitar vueltas completas no cambia la letra final.

### Caso base

Si el mensaje está vacío, `cesar` devuelve `""`.
Esto coincide con $C("",k)$.

### Caso de inducción

La hipótesis de inducción es que `cesar(r, k)` devuelve $C(r,k)$.

Para el mensaje completo:

1. `m.head` obtiene `c` y `desplazar` calcula su cifrado.
2. `m.tail` obtiene `r`, que se procesa con la llamada recursiva.
3. Se unen ambos resultados.

Por la hipótesis:

$$
cesar(c+r,k)=D(c,k)+C(r,k)=C(c+r,k)
$$

Así queda demostrado el caso completo a partir del resto.

### Terminación

Cada llamada recibe un carácter menos mediante `tail`.
Después de un número finito de pasos se llega al mensaje vacío.

## 2. Corrección de `cesarCola`

### Estado y cambios

Sean `M` el mensaje original y `A` el acumulador inicial.

- **Estado:** $s=(m,k,acc)$.
- **Inicio:** $s_0=(M,k,A)$.
- **Final:** `m` está vacío.
- **Invariante:**

$$
acc+C(m,k)=A+C(M,k)
$$

Lo que ya está acumulado, unido al cifrado de lo que falta,
debe dar el resultado completo.

Si `m = c + r`, la transformación es:

$$
(c+r,k,acc)\rightarrow(r,k,acc+D(c,k))
$$

### Demostración

**1. Al comenzar.**

Se tiene `m = M` y `acc = A`, por lo que:

$$
A+C(M,k)=A+C(M,k)
$$

El invariante se cumple.

**2. Al pasar al siguiente estado.**

Se agrega el cifrado de `c` a `acc` y se continúa con `r`:

$$
(acc+D(c,k))+C(r,k)=acc+C(c+r,k)
$$

La igualdad conserva el resultado completo.

**3. Al terminar.**

Si el mensaje está vacío, su cifrado también está vacío.
Entonces:

$$
acc=A+C(M,k)
$$

Ese es el valor devuelto. Con el acumulador inicial vacío,
se obtiene $C(M,k)$.

**4. Por qué llega al final.**

`m.tail` reduce el texto en un carácter por llamada.
Por eso se alcanza el caso base. `@tailrec` comprueba que
la llamada recursiva sea de cola.

## 3. Corrección de `frecuencias`

### Qué debe calcular

Debe devolver una pareja por cada minúscula presente, con su cantidad
de apariciones. Las parejas se ordenan por cantidad descendente y,
si empatan, por letra ascendente.

### Estado y cambios

Sean `M` el mensaje original y `P` la parte ya leída.
Llamamos `r` al texto que falta; en el código es `RestoPalabra`.

- **Estado:** $s=(r,acc)$.
- **Inicio:** `r = M` y `acc = List()`.
- **Final:** `r` está vacío.
- **Invariante:** $M=P+r$ y `acc` contiene una sola pareja por cada
  minúscula de `P`, con su cantidad correcta.
- **Transformación:** se lee `r.head`, se actualiza la lista si hace
  falta y se continúa con `r.tail`.

### Demostración

**1. Al comenzar.**

`P` está vacío, así que no hay letras que contar.
La lista vacía representa correctamente lo leído.

**2. Al pasar al siguiente estado.**

Hay tres posibilidades:

- Si el carácter no es minúscula, los conteos quedan iguales.
- Si la letra ya está, `exists` la detecta y `map` aumenta su cantidad.
  Las otras parejas se conservan.
- Si no está, se agrega `(letra, 1)`. Como no existía una pareja para
  esa letra, no se crea un duplicado.

Así, la lista sigue contando exactamente las letras leídas.
También se mantiene $M=P+r$ al pasar un carácter del resto a `P`.

**3. Al terminar.**

Cuando `r` está vacío, `P` es todo el mensaje y `acc` tiene
las cantidades correctas.

La ordenación usa:

```scala
sortBy(x => {
  val (caracter, cantidad) = x
  (-cantidad, caracter)
})
```

La cantidad negativa hace que las mayores aparezcan primero.
Si son iguales, se compara el carácter alfabéticamente.
Ordenar no cambia las letras ni sus cantidades.

**4. Por qué llega al final.**

Cada llamada consume un carácter mediante `tail`.
`exists` y `map` trabajan sobre una lista finita, con un máximo
de 26 parejas. Por eso se llega al mensaje vacío y luego se
termina la ordenación.

El recorrido de `aux` es de cola, como comprueba `@tailrec`.

## 4. Corrección de `desplazamientoProbable` y `romperCesar`

### `desplazamientoProbable`

La función supone que la letra más frecuente del texto cifrado
corresponde a una `e` del original.

Primero obtiene `frecuencias(m)`. Si no hay minúsculas, devuelve `0`.
Si hay letras, toma la primera pareja. Por el orden de la lista,
esa letra es la más frecuente y los empates se resuelven alfabéticamente.

Sean `f` la letra elegida y $p(f)$ su posición en el alfabeto.
Como `e` ocupa la posición cuatro, se calcula:

$$
d=(p(f)-4)\bmod 26
$$

`Math.floorMod` deja el resultado entre 0 y 25.
Por ejemplo, con `h` se obtiene `3`.

### `romperCesar`

Esta función obtiene `d` y llama a `cesar(m, -d)`.
Por la corrección de César, aplica ese desplazamiento hacia atrás
y conserva los caracteres que no son minúsculas.

Si la letra elegida viene realmente de una `e`, el desplazamiento
estimado coincide con el original, salvo vueltas completas.
Por eso:

$$
C(C(M,k),-d)=M
$$

Si `e` es la única letra más frecuente del original, la estimación
acierta. Puede fallar si domina otra letra o si un empate hace
seleccionar una distinta.

Ambas funciones terminan porque llaman a `frecuencias` y `cesar`,
cuyos recorridos terminan.

### Ejemplo donde falla

Tomamos `"aaa"` y lo ciframos con desplazamiento uno:

```scala
val cifrado = c.cesar("aaa", 1) // "bbb"
c.romperCesar(cifrado)          // "eee"
```

El método encuentra `b` como la letra más frecuente y supone
que viene de una `e`. Calcula `23` y después aplica `-23`.

El resultado es `"eee"`, pero el original era `"aaa"`.
La suposición sobre la letra más frecuente no se cumple en este caso.

## 5. Corrección de `combinaciones` y `vigenere`

### 5.1. `combinaciones`

Llamamos $B(n,a)$ al número de mensajes de longitud `n` que se pueden
formar con `a` letras, sin repetir una letra en posiciones seguidas.
Se consideran `n` y `a` mayores o iguales a cero.

**Casos base.**

Con longitud cero existe un solo mensaje: el vacío.
Con longitud uno hay tantas opciones como letras:

$$
B(0,a)=1
$$

$$
B(1,a)=a
$$

La función devuelve esos valores.

**Caso de inducción.**

Suponemos que `combinaciones(n - 1, a)` cuenta bien los mensajes
de longitud `n - 1`.

Si hay al menos una letra disponible, cada mensaje se puede extender
de `a - 1` maneras, porque no se puede repetir la última letra.
Para `n` mayor o igual a dos:

$$
B(n,a)=(a-1)\times B(n-1,a)
$$

El código hace esa misma multiplicación.

Si `a = 0`, no hay mensajes de longitud positiva. El caso de longitud
uno devuelve cero y las multiplicaciones siguientes mantienen ese cero.

Por ejemplo:

$$
B(3,3)=3\times2\times2=12
$$

**Terminación.**

`n` disminuye hasta llegar a cero o uno. Con los casos base y el paso
de inducción queda justificado el resultado. `BigInt` permite guardar
cantidades mayores que las que admite `Int`.

### 5.2. `vigenere`

Cada minúscula se cifra con la letra de clave que le corresponde.
La clave se repite al agotarse. Los otros caracteres se copian y
no consumen clave.

Para esta explicación se supone una clave de minúsculas.
Si está vacía, el código devuelve el mensaje original.

### Estado y cambios

Para una clave no vacía, sean `M` el mensaje original,
`K` la clave completa y `P` la parte ya leída.

- **Estado:** $s=(resto,claveRestante,acc)$.
- **Inicio:** $s_0=(M,K,"")$.
- **Final:** `resto` está vacío.
- **Invariante:** $M=P+resto$, `acc` contiene el cifrado de `P`
  y la clave restante comienza en la letra que toca usar.
  Si está vacía, se completó una vuelta de la clave.
- **Transformación:** se copia un carácter, se reinicia la clave
  o se cifra una minúscula y se avanza en ambos textos.

### Demostración

**1. Al comenzar.**

No se ha leído ningún carácter, así que `acc` está vacío.
La clave empieza en su primera letra. Se cumple el invariante.

**2. Al pasar al siguiente estado.**

Se revisan los tres casos:

1. Si el carácter no es minúscula, se copia a `acc` y se consume
   del mensaje. La clave conserva su posición.
2. Si toca una minúscula y la clave está agotada, se recupera `K`.
   El mensaje y el acumulador quedan iguales.
3. Si hay una minúscula y clave disponible, se cifra con
   `claveRestante.head - 'a'`. Luego se avanza en el mensaje
   y en la clave.

En cada caso se conserva el cifrado de la parte leída y queda
preparada la letra de clave que corresponde al siguiente paso.

**3. Al terminar.**

Cuando `resto` está vacío, `P` es todo el mensaje.
Por el invariante, `acc` contiene su cifrado completo.
Ese es el valor devuelto.

**4. Por qué llega al final.**

Cada paso consume un carácter, excepto cuando se reinicia la clave.
Ese reinicio no puede repetirse de inmediato: la clave recuperada
tiene letras y el paso siguiente consume la minúscula pendiente.

Por eso se avanza al menos un carácter cada dos llamadas y se llega
al mensaje vacío. `@tailrec` comprueba la recursión de cola.