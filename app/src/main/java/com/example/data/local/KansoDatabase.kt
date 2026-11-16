package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartItemEntity::class,
        WishlistItemEntity::class,
        ShippingAddressEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KansoDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: KansoDatabase? = null

        fun getInstance(context: Context): KansoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KansoDatabase::class.java,
                    "kanso_shop.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
