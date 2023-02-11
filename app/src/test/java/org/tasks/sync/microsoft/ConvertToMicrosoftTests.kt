package org.tasks.sync.microsoft

import com.natpryce.makeiteasy.MakeItEasy.with
import com.todoroo.astrid.data.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.tasks.makers.CaldavTaskMaker.REMOTE_ID
import org.tasks.makers.CaldavTaskMaker.newCaldavTask
import org.tasks.makers.TagDataMaker.NAME
import org.tasks.makers.TagDataMaker.newTagData
import org.tasks.makers.TaskMaker.COMPLETION_TIME
import org.tasks.makers.TaskMaker.DESCRIPTION
import org.tasks.makers.TaskMaker.PRIORITY
import org.tasks.makers.TaskMaker.TITLE
import org.tasks.makers.TaskMaker.newTask
import org.tasks.sync.microsoft.MicrosoftConverter.toRemote
import org.tasks.sync.microsoft.Tasks.Task.Importance
import org.tasks.time.DateTime

class ConvertToMicrosoftTests {
    @Test
    fun noIdForNewTask() {
        val remote =
            newTask().toRemote(newCaldavTask(with(REMOTE_ID, null as String?)), emptyList())
        assertNull(remote.id)
    }

    @Test
    fun setTitle() {
        val remote =
            newTask(with(TITLE, "title")).toRemote(newCaldavTask(), emptyList())
        assertEquals("title", remote.title)
    }

    @Test
    fun noBody() {
        val remote =
            newTask(with(DESCRIPTION, null as String?))
                .toRemote(newCaldavTask(), emptyList())
        assertNull(remote.body)
    }

    @Test
    fun setBody() {
        val remote =
            newTask(with(DESCRIPTION, "Description"))
                .toRemote(newCaldavTask(), emptyList())
        assertEquals("Description", remote.body?.content)
        assertEquals("text", remote.body?.contentType)
    }

    @Test
    fun setHighPriority() {
        val remote =
            newTask(with(PRIORITY, Task.Priority.HIGH))
                .toRemote(newCaldavTask(), emptyList())
        assertEquals(Importance.high, remote.importance)
    }

    @Test
    fun setNormalPriority() {
        val remote =
            newTask(with(PRIORITY, Task.Priority.MEDIUM))
                .toRemote(newCaldavTask(), emptyList())
        assertEquals(Importance.normal, remote.importance)
    }

    @Test
    fun setLowPriority() {
        val remote =
            newTask(with(PRIORITY, Task.Priority.LOW))
                .toRemote(newCaldavTask(), emptyList())
        assertEquals(Importance.low, remote.importance)
    }

    @Test
    fun setNoPriorityToLow() {
        val remote =
            newTask(with(PRIORITY, Task.Priority.NONE))
                .toRemote(newCaldavTask(), emptyList())
        assertEquals(Importance.low, remote.importance)
    }

    @Test
    fun statusForUncompletedTask() {
        val remote = newTask().toRemote(newCaldavTask(), emptyList())
        assertEquals(Tasks.Task.Status.notStarted, remote.status)
    }

    @Test
    fun statusForCompletedTask() {
        val remote =
            newTask(with(COMPLETION_TIME, DateTime())).toRemote(newCaldavTask(), emptyList())
        assertEquals(Tasks.Task.Status.completed, remote.status)
    }

    @Test
    fun noCategories() {
        val remote = newTask().toRemote(newCaldavTask(), emptyList())
        assertNull(remote.categories)
    }

    @Test
    fun setCategories() {
        val remote = newTask().toRemote(
            newCaldavTask(),
            listOf(
                newTagData(with(NAME, "tag1")),
                newTagData(with(NAME, "tag2")),
            )
        )
        assertEquals(listOf("tag1", "tag2"), remote.categories)
    }
}