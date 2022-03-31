package es.sdos.formacion.monumentosandaluces.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.sdos.formacion.monumentosandaluces.data.dao.ImageDAO
import es.sdos.formacion.monumentosandaluces.data.dao.MonumentDAO
import es.sdos.formacion.monumentosandaluces.data.dbo.*
import es.sdos.formacion.monumentosandaluces.util.Util

@Database(entities = [MonumentDBO::class, ImageDBO::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun monumentDao(): MonumentDAO
    abstract fun imageDao(): ImageDAO
}