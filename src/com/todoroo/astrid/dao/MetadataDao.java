/*
 * Copyright (c) 2009, Todoroo Inc
 * All Rights Reserved
 * http://www.todoroo.com
 */
package com.todoroo.astrid.dao;

import android.database.Cursor;

import com.thoughtworks.sql.Criterion;
import com.thoughtworks.sql.Join;
import com.thoughtworks.sql.Query;
import com.todoroo.andlib.data.AbstractDao;
import com.todoroo.andlib.data.AbstractDatabase;
import com.todoroo.andlib.data.Property;
import com.todoroo.andlib.data.TodorooCursor;
import com.todoroo.astrid.model.Metadata;
import com.todoroo.astrid.model.Task;

/**
 * Data Access layer for {@link Metadata}-related operations.
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class MetadataDao extends AbstractDao<Metadata> {

	public MetadataDao() {
        super(Metadata.class);
    }

    // --- SQL clause generators

    /**
     * Generates SQL clauses
     */
    public static class MetadataCriteria {

    	/** Returns all metadata associated with a given task */
    	public static Criterion byTask(long taskId) {
    	    return Metadata.TASK.eq(taskId);
    	}

    	/** Returns all metadata associated with a given key */
    	public static Criterion withKey(String key) {
    	    return Metadata.KEY.eq(key);
    	}

    }

    /**
     * Delete all matching a clause
     * @param database
     * @param where
     * @return # of deleted items
     */
    public int deleteWhere(AbstractDatabase database, Criterion where) {
        return database.getDatabase().delete(Metadata.TABLE.getName(),
                where.toString(), null);
    }

    /**
     * Fetch all metadata that are unattached to the task
     * @param database
     * @param properties
     * @return
     */
    public TodorooCursor<Metadata> fetchDangling(AbstractDatabase database, Property<?>[] properties) {
        Query sql = Query.select(properties).from(Metadata.TABLE).join(Join.left(Task.TABLE,
                Metadata.TASK.eq(Task.ID))).where(Task.TITLE.isNull());
        Cursor cursor = database.getDatabase().rawQuery(sql.toString(), null);
        return new TodorooCursor<Metadata>(cursor);
    }

}

