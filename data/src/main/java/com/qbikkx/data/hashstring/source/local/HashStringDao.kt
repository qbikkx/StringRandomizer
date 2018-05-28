package com.qbikkx.data.hashstring.local

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.qbikkx.data.hashstring.HashString
import io.reactivex.Completable

/**
 * Created by qbikkx on 15.03.18.
 */
@Dao
abstract class HashStringDao {

	@Query("SELECT * FROM HashStrings ORDER BY id ASC")
	abstract fun getAllHashStrings(): DataSource.Factory<Int, HashString>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insertHashString(hashString: HashString): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	@Transaction
	abstract fun insertHashStrings(hashStrings: List<HashString>)

	@Update
	protected abstract fun updateHashString(hashString: HashString)

	@Query("DELETE FROM HashStrings")
	@Transaction
	abstract fun deleteAllHashStrings() : Completable

	@Query("DELETE FROM HashStrings WHERE id == :id")
	abstract fun deleteHashString(id: Long) : Completable

	fun insertOrUpdateShow(hashString: HashString): HashString = when {
		hashString.id == null -> {
			hashString.copy(id = insertHashString(hashString))
		}
		else                  -> {
			updateHashString(hashString)
			hashString
		}
	}
}