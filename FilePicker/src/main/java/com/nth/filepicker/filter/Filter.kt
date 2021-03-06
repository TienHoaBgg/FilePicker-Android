package com.nth.filepicker.filter

import android.content.Context
import com.nth.filepicker.MimeTypeManager
import com.nth.filepicker.internal.entity.IncapableCause
import com.nth.filepicker.internal.entity.Item
import com.nth.filepicker.MimeType


/**
 * Describe : Filter for choosing a {@link Item}. You can add multiple Filters through
 * {@link SelectionCreator #addFilter(Filter)}.
 * Created by NTH on 2020/5/5 on 16:12.
 */
abstract class Filter {
    companion object {

        // Convenient constant for a minimum value
        const val MIN = 0

        // Convenient constant for a maximum value
        const val MAX = Int.MAX_VALUE

        // Convenient constant for 1024
        const val K = 1024
    }

    // Against what mime types this filter applies
    abstract fun constraintTypes(): Set<MimeType>

    /**
     * Invoked for filtering each item
     *
     * @return null if selectable, {@link IncapableCause} if not selectable.
     */
    abstract fun filter(context: Context, item: Item?): IncapableCause?

    // Whether an {@link Item} need filtering
    open fun needFiltering(context: Context, item: Item?): Boolean {
        constraintTypes().forEach {
            if (MimeTypeManager.checkType(context, item?.contentUri, it.getValue())
            ) return true
        }

        return false
    }
}