package es.sdos.formacion.monumentosandaluces.util

import es.sdos.formacion.monumentosandaluces.data.dto.LocationDTO
import es.sdos.formacion.monumentosandaluces.data.dto.MonumentDTO
import es.sdos.formacion.monumentosandaluces.data.dto.ImageDTO

class MockUpUtils {
    companion object {

        /**
         * Get default data by mocky
         */
        fun getExampleMonument(): List<MonumentDTO> {
            val monumentImageGiralda = listOf<ImageDTO>(
                    ImageDTO(1001, "https://saposyprincesas.elmundo.es/wp-content/uploads/2017/09/Giralda.jpg"),
                    ImageDTO(1002, "https://tuiteartees.files.wordpress.com/2012/08/20120825-giralda-de-sevilla.jpg?w=1024"),
                    ImageDTO(1003, "https://i2.wp.com/traveleandoporelmundo.com/wp-content/uploads/CatedralAlzazar/img_0378.jpg?resize=800%2C533&ssl=1")
            )

            val monumentImageNotreDam = listOf<ImageDTO>(
                    ImageDTO(2001, "https://files.merca20.com/uploads/2017/06/bigstock-Notre-Dame-de-Paris-France-at-16555229.jpg"),
                    ImageDTO(2002, "https://www.paris360.de/sites/default/files/styles/node/public/field/image/location/notre-dame-paris.jpg?itok=7U7gZ8so"),
                    ImageDTO(2003, "https://cdn.getyourguide.com/img/tour_img-754825-148.jpg")
            )

            return listOf(
                    MonumentDTO(100,
                            "La Giralda",
                            "Sevilla",
                            "Giralda es el nombre que recibe la torre campanario de la catedral de Santa María de la Sede de la ciudad de Sevilla, en Andalucía (España). Los dos tercios inferiores de la torre corresponden al alminar de la antigua mezquita de la ciudad, de finales del siglo XII, en la época almohade, mientras que el tercio superior es una construcción sobrepuesta en época cristiana para albergar las campanas. En su cúspide se halla una bola llamada tinaja sobre la cual se alza el Giraldillo. <br>La Giralda mide 94,69 metros de altura, incluido el Giraldillo, que mide 7,69 metros. Fue durante siglos la torre más alta de España, así como una de las construcciones más elevadas y famosas de toda Europa (por comparación; la Torre de Pisa mide 55,8 m y el Big Ben 96,3 m). El 29 de diciembre de 1928 fue declarada Patrimonio Nacional y en 1987 integró la lista del Patrimonio de la Humanidad. Su arquitectura única y original de formas cuadrangulares exactas, adornada de torrecillas y pináculos, ha servido de inspiración a multitud de torres posteriores en Estados Unidos, Rusia, Polonia y otros países del mundo.",
                            "https://es.wikipedia.org/wiki/Giralda",
                            LocationDTO(37.3850484598, -5.9887793782),
                            monumentImageGiralda),
                    MonumentDTO(2001,
                            "Notre Dam",
                            "París",
                            "Se trata de uno de los edificios más señeros y antiguos de cuantos se construyeron en estilo gótico. El uso innovador de la bóveda de crucería y del arbotante, los enormes y coloridos rosetones y el naturalismo y la abundancia de decoración escultórica lo diferencian de la arquitectura románica.<br>Su edificación comenzó en el año 1163 y, para 1260, ya estaba completada en su mayor parte, aunque se terminó en el año 1345 y se modificó de manera frecuente a lo largo de los siglos siguientes, debido a necesidades de renovación y también por la evolución del gusto dominante. En 1786 la aguja central, dañada por las inclemencias del tiempo, hubo de ser desmontada. Durante la década de 1790, tras la Revolución francesa, Notre Dame fue desacralizada y sufrió el robo o dispersión de muchos de sus bienes así como la profanación de parte de su imaginería religiosa, que quedó dañada o destruida. Tras ser empleada como almacén, en 1802 se devolvió su uso a la Iglesia católica gracias a Napoleón Bonaparte, quien se coronaría emperador en Notre Dame dos años después. Con todo, el templo subsistió en modestas condiciones hasta que la publicación en 1831 de Nuestra Señora de París, novela escrita por Victor Hugo y cuyo escenario principal era Notre Dame, reavivó el interés popular por la vieja catedral parisina. El arquitecto Eugène Viollet-le-Duc, defensor del naciente estilo neogótico, encabezó un proyecto de restauración que comenzó en 1845 y se prolongó durante un cuarto de siglo; esta intervención, demasiado audaz según algunos historiadores, no solo reparó ornamentos dañados sino que también incorporó elementos enteramente nuevos, como una nueva aguja de 96 metros de altura y las hoy célebres gárgolas, y demolió los edificios circundantes. Ya en 1963 se procedió a limpiar de hollín la fachada, que así recuperó su color original. Entre 1991 y 2000 se llevó a cabo una nueva campaña de limpieza y restauración, pero el edificio seguía necesitando intervenciones en otras partes, como su aguja central, y (tras dificultades para reunir financiación) las reparaciones se reactivaron en 2019.<br>El 15 de abril de 2019 el edificio sufrió daños significativos a causa de un incendio; dos tercios de la techumbre fueron destruidos, la aguja central de Viollet-le-Duc cayó y los rosetones quedaron dañados. El fuego pudo deberse a un descuido durante las obras de remozamiento que se estaban efectuando, pero esta suposición está sujeta a una investigación ahora en curso.",
                            "",
                            LocationDTO(48.852966, 2.349902),
                            monumentImageNotreDam)
            )
        }
    }
}