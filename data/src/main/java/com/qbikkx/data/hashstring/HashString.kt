package com.qbikkx.data.hashstring

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by qbikkx on 15.03.18.
 */
@Entity(tableName = "HashStrings")
data class HashString(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var mId: Long? = null,
                      @ColumnInfo(name = "value") val string: String) {

    val hash: Int get() = string.hashCode()
}