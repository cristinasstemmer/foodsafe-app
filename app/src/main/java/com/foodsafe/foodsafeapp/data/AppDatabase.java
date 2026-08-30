package com.foodsafe.foodsafeapp.data;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Favorito;
import com.foodsafe.foodsafeapp.model.FavoritoReceita;
import com.foodsafe.foodsafeapp.model.Receita;
import com.foodsafe.foodsafeapp.model.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Alimento.class, Receita.class, Favorito.class, FavoritoReceita.class}, version = 29, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDAO usuarioDAO();
    public abstract AlimentoDAO alimentoDAO();
    public abstract ReceitaDAO receitaDAO();
    public abstract FavoritoDAO favoritoDAO();
    public abstract FavoritoReceitaDAO favoritoReceitaDAO();

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "foodsafe.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                AlimentoDAO alimentoDAO = INSTANCE.alimentoDAO();
                alimentoDAO.insertAll(Prepopulation.getAlimentos());

                ReceitaDAO receitaDAO = INSTANCE.receitaDAO();
                for (Receita receita : Prepopulation.getReceitas()) {
                    try {
                        receitaDAO.insert(receita);
                    } catch (Exception e) {
                        Log.e("DB_POPULATE", "Error inserting recipe: " + receita.getNome(), e);
                    }
                }
            });
        }
    };
}