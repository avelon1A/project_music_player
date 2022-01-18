

package io.github.uditkarode.able.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import io.github.uditkarode.able.R
import io.github.uditkarode.able.activities.AlbumPlaylist
import io.github.uditkarode.able.fragments.Search
import io.github.uditkarode.able.models.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.ref.WeakReference

/**
 * Shows results in the search fragment when the search mode is set to YouTube Music.
 */
@ExperimentalCoroutinesApi
class YtmResultAdapter(private val songList: ArrayList<Song>,
                       private val wr: WeakReference<Search>,
                       private val mode: String): RecyclerView.Adapter<YtmResultAdapter.RVVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVVH =
        RVVH(LayoutInflater.from(parent.context).inflate(R.layout.rv_ytm_result, parent, false))

    override fun getItemCount() = songList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RVVH, position: Int) {
        val current = songList[position]
        holder.songName.text = current.name
        holder.songUploader.text = "$mode • " + current.artist

        holder.songAlbumArt.run {
            Glide.with(context)
                .load(current.ytmThumbnail)
                .signature(ObjectKey("stream"))
                .into(this)
        }

        holder.songName.typeface =
            Typeface.createFromAsset(holder.songName.context.assets, "fonts/interbold.otf")

        holder.songUploader.typeface =
            Typeface.createFromAsset(holder.songName.context.assets, "fonts/inter.otf")

        holder.itemView.setOnClickListener {
            if(current.youtubeLink.contains("youtube.com/playlist")){
                holder.itemView.context.run {
                    startActivity(Intent(this, AlbumPlaylist::class.java).run {
                        this.putExtra("name", current.name)
                        this.putExtra("artist", current.artist)
                        this.putExtra("link", current.youtubeLink)
                        this.putExtra("art", current.ytmThumbnail)
                        this
                    })
                }
            } else wr.get()?.itemPressed(songList[position])
        }
    }

    class RVVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val songName = itemView.findViewById<TextView>(R.id.vid_song)!!
        val songUploader = itemView.findViewById<TextView>(R.id.vid_uploader)!!
        val songAlbumArt = itemView.findViewById<ImageView>(R.id.vid_albart)!!
    }
}