/*
 * ************************************************************************
 *  NextResultAdapter.kt
 * *************************************************************************
 * Copyright © 2019 VLC authors and VideoLAN
 * Author: Nicolas POMEPUY
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 * **************************************************************************
 *
 *
 */

package org.videolan.vlc.gui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.videolan.vlc.databinding.NextItemBinding
import org.videolan.vlc.gui.helpers.SelectorViewHolder
import org.videolan.vlc.next.models.MediaResult
import org.videolan.vlc.next.models.getImageUri
import org.videolan.vlc.next.models.getYear

class NextResultAdapter internal constructor(private val mLayoutInflater: LayoutInflater) : RecyclerView.Adapter<NextResultAdapter.ViewHolder>() {

    private var dataList: List<MediaResult>? = null
    internal lateinit var clickHandler: NextActivity.ClickHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NextItemBinding.inflate(mLayoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mediaResult = dataList!![position]
        holder.binding.item = mediaResult
        holder.binding.imageUrl = mediaResult.getImageUri()
    }

    fun setItems(newList: List<MediaResult>) {
        dataList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList!!.size
    }

    inner class ViewHolder(binding: NextItemBinding) : SelectorViewHolder<NextItemBinding>(binding) {

        init {
            binding.handler = clickHandler
        }
    }
}

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@BindingAdapter("year")
fun showYear(view: TextView, item: MediaResult) {
    view.text = item.getYear()
}
