# Práctica Monumentos

Un profesor quiere que sus alumnos aprueben geografía y se le ocurre una manera de ayudarlos. Dado 
que los alumnos están siempre mirando sus teléfonos, nos pide que hagamos una aplicación para añadir 
y ver los monumentos andaluces más importantes.

La aplicación tendrá 4 pantallas:
- Lista de Monumentos
- Mapa de Monumentos
- Detalle Monumento
- Añadir Monumento

El diseño de la aplicación se puede consultar en [Marvel](https://marvelapp.com/1eh45e3g)


# Objetos

### Provincia

|Código | Nombre|
|-|-|
|AL| Almería|
|CA| Cádiz|
|CO| Córdoba|
|GR| Granada|
|HU| Huelva|
|JA| Jaén|
|MA| Málaga|
|SE| Sevilla|

### Monumento

| Id | Nombre |   Descripción  | Código Provincia | Imagen | Latitud | Longitud |
|----|---------|------------------------|-|-|-|-|

# Tareas

## Tarea 1
Implementar el objeto de dominio llamado *MonumentDomain*. El identificador tiene que ser autogenerado, 
la descripción no sobrepasar los 250 caracteres, nombre, latitud y longitud no pueden ser nulos.

## Tarea 2
Implementar la pantalla *MonumentListActivity*. Ésta tiene un RecyclerView el cuál debe mostrar todos 
los monumentos usando el *MonumentAdapter*. Podéis probarlo creando monumentos y devolverlos en el 
método *mockMonuments* de la clase *MockUtils*.

## Tarea 3
Implementar la pantalla de detalle llamada *MonumentDetailActivity*. Ésta mostrará todos los datos 
del monumento:
    * Arriba la imagen del monumento 
    * La Toolbar ha de ser colapsable y el titulo será el Nombre del monumento
    * Provincia en la que se encuentra
    * Descripción del monumento.
    * Mapa con la posición del monumento, no se puede interactuar con él y tendrá el zoom al máximo.

## Tarea 4
Implementar la clase *MonumentMapActivity* en la que se ha de mostrar un mapa de Andalucía con todos 
los monumentos registrados.
    * Si se pulsa un punto de interés de monumento se ha de mostrar una ventana flotante con el nombre 
        y la provincia del monumento. 
    * Si se pulsa sobre esta ventana, se tendrá que abrir la pantalla *MonumentDetailActivity*

## Tarea 5
Implementar la funcionalidad de crear un Monumento. Desde la pantalla de mapa, si se deja pulsado durante un tiempo, se abrirá la pantalla *AddMonumentActivity* con la posición pulsada. Esta pantalla constará de los campos necesarios para rellenar los datos de un monumento con la especificación de *MonumentDomain*. En la parte de abajo del formulario un botón que estará deshabilitado hasta rellenar los campos necesarios. Cuando se añada correctamente debe mostrarse un mensaje indicando "NombreMonumento añadido" y se volverá a la pantalla de mapa este nuevo Monumento visible.

## Tarea 6
Cargar los monumentos desde la [API](https://gist.github.com/ignaciogs/f766e43d45e09bb4d65296c95b1848d1/raw/ff109567f93e9cbcae18b291ce6e4e664b578177/monuments_data.json).
Implementar un método que devuelva todos los monumentos en la clase *MonumentDao*.
Usar las dos fuentes de datos para cargar los monumentos en la aplicación.

## Tarea 7
Añadir un menú en la Toolbar de *MonumentListActivity* haciendo uso del recurso *list_menu.xml*. 
Éste menú debe de permitir ordenar los monumentos según los siguientes criterios:
* Id
* Nombre
* Norte - Sur
* Este - Oeste

Añadir los métodos necesarios en la clase *MonumentDao* para obtener los monumentos con estas ordenaciones.

## Tarea 8
Añadir un menú en la Toolbar de la *MonumentDetailActivity* haciendo uso del recurso *monument_menu.xml*. 
Este menú debe permitir el mostrar el monumento en la aplicación de Google Maps y además compartir 
por correo los datos del monumento de la siguiente manera:
* Asunto: [Aprueba Historia] *NombreMonumento*
* Texto: Sabías que el monumento *NombreMonumento* se encuentra en la provincia de  *ProvinciaMonumento*. Conócela un poco más, *DescripciónMonumento*"

## Tarea 9
Añadir un menú en la Toolbar de *MonumentMapActivity* haciendo uso del recurso *map_menu.xml*. 
Se podrá filtrar los monumentos por provincia, 
* Al filtrar, el mapa debe mostrar sólo la región en la que se encuentran los monumentos.
* Si al filtrar no existe ningún monumento, el mapa debe volver a la posición inicial y mostrarse un 
mensaje indicando "No hay monumentos registrados en esta provincia".

Añadir al *MonumentDao* un método para realizar el filtrado. 

## Tarea 10
Implementar la eliminación de Monumentos. Añadir al *MonumentDao* un método para eliminar Monumentos. 
Se puede eliminar un monumento de tres formas:
* Pulsación larga desde la pantalla de lista de monumentos
* Pulsación larga sobre un punto de interés de monumento en la pantalla de mapa
* Añadir un botón en la pantalla de detalle para eliminar el monumento.

Al eliminar el monumento, se deberá mostrar un mensaje indicando "Monumento Eliminado" y con una opción 
Deshacer, permitiendo restaurar el monumento el tiempo que dure el mensaje.
 
# Changelog

### 2020/05/25 AndroidX + API 29

### 2019/04/23 Marvel + Json

### 2019/04/17 Primera Versión