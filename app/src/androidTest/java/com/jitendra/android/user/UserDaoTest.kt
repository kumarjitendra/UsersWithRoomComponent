package com.jitendra.android.user

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: userDao
    private lateinit var db: UserRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, UserRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        userDao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val word = User("word")
        userDao.insert(word)
        val allWords = userDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].user, word.user)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val word = User("aaa")
        userDao.insert(word)
        val word2 = User("bbb")
        userDao.insert(word2)
        val allUsers = userDao.getAlphabetizedWords().first()
        assertEquals(allUsers[0].user, word.user)
        assertEquals(allUsers[1].user, word2.user)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val word = User("word")
        userDao.insert(word)
        val word2 = User("word2")
        userDao.insert(word2)
        userDao.deleteAll()
        val allWords = userDao.getAlphabetizedWords().first()
        assertTrue(allWords.isEmpty())
    }
}
