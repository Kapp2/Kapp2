package com.example.kapp2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kapp2.R
import com.example.kapp2.db.relations.BotonPerfilCrossRef
import com.example.kapp2.model.Boton
import com.example.kapp2.model.Perfil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Boton::class,
        Perfil::class,
        BotonPerfilCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Kapp2DataBase : RoomDatabase(){

    abstract val kapp2Dao: Kapp2Dao

    companion object {
        @Volatile
        private var INSTANCE: Kapp2DataBase? = null

        fun getInstance(context: Context): Kapp2DataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Kapp2DataBase::class.java,
                    "Kapp2_db"
                )
                .addCallback(InicioDbCallback())
                .build().also {
                    INSTANCE = it
                }
            }
        }

        //***************CallBack******************************
        //Permite iniciar la base de datos
        private class InicioDbCallback() : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    GlobalScope.launch {
                        cargarDatabase(database.kapp2Dao)
                    }
                }
            }

            //Iniciamos la base de datos con Perfiles de ejemplo
            suspend fun cargarDatabase(kapp2Dao: Kapp2Dao) {
                val perfiles = listOf(
                    Perfil("Pedro", "pdr2511"),
                    Perfil("Juan", "ja1901"),
                    Perfil("Paco", "pc0610")
                )
                perfiles.forEach { kapp2Dao.addPerfil(it) }
                val botones = listOf(
                    Boton("La Actitud Cuenta?", R.raw.cuenta_actitud, 5),
                    Boton("Blueberries",R.raw.blueberries,1),
                    Boton("Para los Despistados", R.raw.despitados_paco, 5),
                    Boton("Directed By", R.raw.directed_by, 2),
                    Boton("Hablando en Kotlin", R.raw.hablando_kotlin, 5),
                    Boton("Empanadas", R.raw.empanadas_dross, 3),
                    Boton("Alerta subnormal", R.raw.alerta_subnormal, 1),
                    Boton("Grito WillyRex", R.raw.grito_willyrex, 1),
                    Boton("Sing Winner", R.raw.sing_winner, 2),
                    Boton("Jose Antonio", R.raw.jose_antonio, 5),
                    Boton("Una Tragedia", R.raw.una_tragedia, 4),
                    Boton( "Paco Nervioso", R.raw.paco_nervioso, 5),
                    Boton("YA esta aqui la guerra", R.raw.ya_esta_la_guerra, 3),
                    Boton("Veggeta me la ", R.raw.v_mete_w, 3),
                    Boton("Sa matao Paco ", R.raw.sa_matao_paco, 3),
                    Boton("Deja Vu", R.raw.deja_vu, 2),
                    Boton("Todo Mal", R.raw.paco_todo_mal, 5),
                    Boton("Croquetas", R.raw.croquetas, 4),
                    Boton("Una Pregunta Curiosa...", R.raw.pregunta_curiosa, 5),
                    Boton("Chacarron Dog", R.raw.chacarron_dog, 1),
                    Boton("Jumping frog", R.raw.jumping_frog, 1),
                    Boton("Música de tutoriales", R.raw.musica_tutoriales, 3),
                    Boton("Quiero ver a tilín", R.raw.tilin, 2),
                    Boton("Me corrooooahhh", R.raw.me_corro, 3),
                    Boton("Top 5 niños...", R.raw.top5, 4)
                )
                botones.forEach { kapp2Dao.addBoton(it) }
                val botonesFavoritos = listOf(
                    BotonPerfilCrossRef(1, "Paco"),
                    BotonPerfilCrossRef(2, "Juan"),
                    BotonPerfilCrossRef(4, "Juan"),
                    BotonPerfilCrossRef(7, "Pedro"),
                    BotonPerfilCrossRef(3, "Paco")
                )
                botonesFavoritos.forEach { kapp2Dao.addBotonesFavoritos(it) }
            }
        }
    }
}