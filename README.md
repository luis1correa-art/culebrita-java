# Culebrita

Juego clásico tipo **Snake** hecho en **Java 11** con **Swing**. La culebra se mueve por una grilla, come, crece y termina la partida al chocar con un borde o con su propio cuerpo.

## Qué hace el programa

- **Controles de teclado:** menú, movimiento, pausa, reinicio y salida se manejan con el teclado (detalle en la tabla de abajo).
- **Movimiento y crecimiento:** la culebra avanza una celda por ciclo. Si come, suma un segmento; si no, la cola se recorre y el tamaño se mantiene.
- **Generación aleatoria de comida:** la manzana aparece en una celda libre al azar (nunca encima del cuerpo).
- **Detección de colisiones:** choque con los bordes del tablero y con el propio cuerpo. Entrar en la celda de la cola al moverse (sin crecer) no cuenta como choque.
- **Interfaz gráfica con Swing:** ventana `JFrame`, dibujo en `JPanel` (`paintComponent`), menú, HUD de puntaje y pantallas de pausa / Game Over.
- **Ciclo de ejecución:** el avance del juego lo marca un `javax.swing.Timer` (el equivalente correcto en Swing a un hilo de juego: actualiza y pinta en el hilo de la interfaz, sin un `Thread` suelto).
- **Game Over:** al perder se muestra el letrero, el puntaje de la partida y el récord. Se puede reiniciar con `R` o volver al menú con `ENTER`.

## Extra (además del Snake básico)

- **Menú inicial:** título, selector de dificultad (tres recuadros) y recuadro `ENTER` para empezar.
- **Tres dificultades:** Fácil, Normal y Difícil (`1` / `2` / `3` o flechas izquierda/derecha en el menú). Cambia la velocidad del Timer.
- **La partida se acelera:** cada 5 puntos el ciclo se acorta un poco, hasta un mínimo según la dificultad.
- **Pausa:** `P` o `Espacio` congelan o reanudan la partida.
- **Puntaje y récord:** el puntaje es de **esta** partida; el récord es el **mejor de siempre** y se guarda en el usuario de Windows (`Preferences`), aunque cierres el programa.
- **Reinicio:** `R` arranca otra partida sin salir.
- **Cabeza distinta de la cola** y comida dibujada como círculo, sobre una grilla oscura.
- **Victoria rara:** si la culebra llena el tablero, se muestra “tablero completo”.
- **Arquitectura:** lógica en `model` (`Game`, `Snake`, `Direction`), vista en `ui`, récord en `persist`.
- **Tests JUnit 5** (con Maven): movimiento, no girar 180°, colisión con muro, crecimiento y comida fuera del cuerpo.

## Controles

| Tecla | Dónde | Acción |
| --- | --- | --- |
| `1` / `2` / `3` | Menú | Fácil / Normal / Difícil (también teclado numérico) |
| Flechas izquierda / derecha | Menú | Cambiar dificultad |
| `ENTER` | Menú | Empezar a jugar |
| Flechas | Partida | Mover la culebra |
| `P` o `Espacio` | Partida | Pausa / continuar |
| `R` | Partida o Game Over | Reiniciar |
| `ENTER` | Game Over | Volver al menú |
| `ESC` | Menú o Game Over | Salir |
| `ESC` | Partida | Volver al menú |

## Requisitos

- JDK 11 o superior

## Cómo ejecutar

En Windows, desde la carpeta del proyecto:

```bat
run.bat
```

Con Maven (opcional):

```bash
mvn test
mvn exec:java
```

```bash
mvn -q package
java -jar target/culebrita-1.0.0.jar
```

## Cómo está organizado

```
src/main/java/culebrita/
  CulebritaApp.java          punto de entrada
  model/                     reglas del juego (sin Swing)
  ui/                        ventana, dibujo y teclado
  persist/                   récord guardado entre sesiones
src/test/java/               pruebas del modelo
```
