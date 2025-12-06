package com.foodsafe.foodsafeapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Favorito;
import com.foodsafe.foodsafeapp.model.Receita;
import com.foodsafe.foodsafeapp.model.Usuario;

@Database(entities = {Usuario.class, Alimento.class, Receita.class, Favorito.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDAO usuarioDAO();
    public abstract AlimentoDAO alimentoDAO();
    public abstract ReceitaDAO receitaDAO();
    public abstract FavoritoDAO favoritoDAO();

    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "FoodSafeDB";

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "foodsafe.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}