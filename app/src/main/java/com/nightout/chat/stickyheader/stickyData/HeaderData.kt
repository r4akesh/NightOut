
package com.nightout.chat.stickyheader.stickyData;

import androidx.annotation.LayoutRes
import com.nightout.chat.model.StickyMainData

interface HeaderData : StickyMainData {
    @LayoutRes
    fun getHeaderLayout(): Int
}