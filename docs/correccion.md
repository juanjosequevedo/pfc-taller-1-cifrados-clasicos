# Informe de corrección — Taller 1: Cifrados Clásicos

## 1. Corrección de `cesar`

La función implementada es:

```scala
def cesar(m: Mensaje, k: Int): Mensaje =
  if(m.isEmpty()) ""
  else desplazar(m(0), k) + cesar(m.substring(1), k)
```

Queremos demostrar que la función devuelve correctamente el mensaje cifrado
mediante el desplazamiento César.

Definimos la función matemática $C$ que representa el cifrado César sobre un
mensaje:

$$
C("") = ""
$$

y para un mensaje no vacío $m = c \cdot r$:

$$
C(c \cdot r,k)
=
\operatorname{desplazar}(c,k) + C(r,k)
$$

donde $c$ es el primer carácter del mensaje y $r$ es el resto del mensaje.

Queremos demostrar:

$$
\forall m \in \text{String},\ \forall k \in \mathbb{Z}:
\quad
\text{cesar}(m,k) = C(m,k)
$$

La demostración se realiza mediante inducción estructural sobre el mensaje.

### Caso base

Sea:

$$
m = ""
$$

Al evaluar el programa:

$$
\begin{aligned}
\text{cesar}("",k)
&\rightarrow
\text{if } "".isEmpty()\ ""\ \text{else }\ldots \\
&\rightarrow ""
\end{aligned}
$$

Por definición de $C$:

$$
C("",k) = ""
$$

Por lo tanto:

$$
\text{cesar}("",k) = C("",k)
$$

El caso base se cumple.

### Caso de inducción

Sea un mensaje no vacío:

$$
m = c \cdot r
$$

donde $c$ es el primer carácter y $r$ es el resto del mensaje.

La hipótesis de inducción es:

$$
\text{cesar}(r,k) = C(r,k)
$$

Debemos demostrar:

$$
\text{cesar}(c \cdot r,k) = C(c \cdot r,k)
$$

Evaluamos la función:

$$
\operatorname{cesar}(c \cdot r,k)
\rightarrow
\operatorname{desplazar}(c,k)
+
\operatorname{cesar}(r,k)
$$

Aplicando la hipótesis de inducción:

$$
\begin{aligned}
\operatorname{desplazar}(c,k)
+
\text{cesar}(r,k)
&=
\operatorname{desplazar}(c,k)
+
C(r,k)
\end{aligned}
$$

Por la definición de $C$:

$$
\operatorname{desplazar}(c,k) + C(r,k)
=
C(c \cdot r,k)
$$

Por lo tanto:

$$
\text{cesar}(c \cdot r,k) = C(c \cdot r,k)
$$

Así, por inducción estructural:

$$
\boxed{
\forall m \in \text{String},\ \forall k \in \mathbb{Z}:
\text{cesar}(m,k)=C(m,k)
}
$$

Por lo tanto, `cesar` es correcta respecto a la especificación del cifrado
César.

---

## 2. Corrección de `cesarCola`

La función implementada es:

```scala
@tailrec
final def cesarCola(
  m: Mensaje,
  k: Int,
  acc: Mensaje = ""
): Mensaje =
  if(m.isEmpty) acc
  else cesarCola(
    m.substring(1),
    k,
    acc + desplazar(m(0), k)
  )
```

Para demostrar su corrección se utiliza un invariante sobre el estado de la
función.

Sea $M$ el mensaje original que se desea cifrar y sea $C(M,k)$ su resultado
correcto según la función matemática definida anteriormente.

Representamos un estado de `cesarCola` mediante:

$$
s = (m,acc)
$$

donde:

- $m$ es la parte del mensaje que todavía falta por procesar.
- $acc$ es el resultado que ya ha sido construido.

### Invariante

Definimos el siguiente invariante:

$$
\boxed{
\operatorname{Inv}(m,acc)
\equiv
acc + C(m,k) = C(M,k)
}
$$

Esto significa que el resultado que ya está almacenado en `acc`, junto con
el resultado que todavía falta obtener a partir de `m`, corresponde
exactamente al cifrado del mensaje original $M$.

---

## 2.1. Estado inicial

La función comienza con:

$$
s_0 = (M,"")
$$

Debemos demostrar que el estado inicial cumple el invariante.

Sustituyendo:

$$
\begin{aligned}
acc + C(m,k)
&=
"" + C(M,k) \\
&=
C(M,k)
\end{aligned}
$$

Por lo tanto:

$$
\operatorname{Inv}(M,"")
$$

se cumple en el estado inicial.

---

## 2.2. Conservación del invariante

Supongamos que el estado actual es:

$$
s=(c \cdot r,acc)
$$

donde $c$ es el primer carácter del mensaje y $r$ es el resto.

Suponemos como hipótesis que se cumple el invariante:

$$
acc + C(c \cdot r,k)=C(M,k)
$$

Por definición de $C$:

$$
C(c \cdot r,k)
=
\operatorname{desplazar}(c,k)+C(r,k)
$$

Por lo tanto:

$$
acc+
\operatorname{desplazar}(c,k)+C(r,k)
=
C(M,k)
$$

La función realiza la siguiente transformación:

$$
(c \cdot r,acc)
\rightarrow
\left(
r,\,
acc+\operatorname{desplazar}(c,k)
\right)
$$

Sea:

$$
acc' =
acc+\operatorname{desplazar}(c,k)
$$

Entonces:

$$
\begin{aligned}
acc' + C(r,k)
&=
\left(
acc+\operatorname{desplazar}(c,k)
\right)
+
C(r,k) \\
&=
acc+
\operatorname{desplazar}(c,k)
+
C(r,k) \\
&=
C(M,k)
\end{aligned}
$$

Por lo tanto:

$$
\operatorname{Inv}(r,acc')
$$

El invariante se conserva después de cada paso.

---

## 2.3. Estado final

La función termina cuando:

$$
m = ""
$$

En ese momento el estado tiene la forma:

$$
("",acc)
$$

Por el invariante:

$$
acc+C("",k)=C(M,k)
$$

Como:

$$
C("",k)=""
$$

tenemos:

$$
acc+""=C(M,k)
$$

y por lo tanto:

$$
acc=C(M,k)
$$

El programa devuelve precisamente `acc`, así que:

$$
\boxed{
\text{cesarCola}(M,k)=C(M,k)
}
$$

---

## 2.4. Terminación

En cada paso de `cesarCola`, el mensaje se reemplaza por:

```scala
m.substring(1)
```

Por lo tanto, su longitud disminuye en una unidad:

$$
|m'| = |m|-1
$$

La longitud del mensaje es un número natural y no puede disminuir
indefinidamente.

Después de un número finito de pasos se alcanza:

$$
|m|=0
$$

es decir:

$$
m=""
$$

En ese momento se cumple la condición:

```scala
m.isEmpty
```

y la función termina.

Por lo tanto, `cesarCola` siempre alcanza su estado final para cualquier
mensaje finito.

---

## 2.5. Conclusión de la corrección

Se ha demostrado que:

1. El estado inicial de `cesarCola` cumple el invariante.
2. El invariante se conserva en cada transformación del estado.
3. Cuando se alcanza el estado final, el acumulador contiene exactamente el
   cifrado César del mensaje original.
4. El estado final siempre se alcanza porque la longitud del mensaje
   disminuye en cada paso.

Por lo tanto:

$$
\boxed{
\forall M \in \text{String},\ \forall k \in \mathbb{Z}:
\text{cesarCola}(M,k)=C(M,k)
}
$$

Así, `cesarCola` es correcta respecto a la especificación del cifrado
César.