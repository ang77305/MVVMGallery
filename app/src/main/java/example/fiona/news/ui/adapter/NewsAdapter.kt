package example.fiona.news.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import example.fiona.news.R
import example.fiona.news.data.model.NewsData


class ImagesAdapter(
    val context: Activity,
    var imagesList: ArrayList<NewsData>,
    val cellClickListener: CellClickListener,
    val tagClickListener: TagClickListener
) :
    RecyclerView.Adapter<ImagesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesViewHolder {

        return ImagesViewHolder(context.layoutInflater.inflate(R.layout.item_image, parent, false))

    }

    fun updateList(imagesList: ArrayList<NewsData>) {

        this.imagesList = imagesList
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {

        holder.userTextView.setText(imagesList.get(position).user)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(imagesList.get(position))
        }

        Glide.with(context).load(imagesList.get(position).webformatURL).apply(
            RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .signature(ObjectKey(imagesList.get(position).webformatURL))
                .error(R.mipmap.ic_launcher)
        ).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.previewImageView)

        Glide.with(context).load(imagesList.get(position).userImageURL).apply(
            RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .signature(ObjectKey(imagesList.get(position).userImageURL))
                .error(R.mipmap.ic_launcher)
        ).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.userImageView)

        val allTags = imagesList.get(position).tags.split(',')

        val tagsAdapter =
            TagsAdapter(context, allTags as ArrayList<String>, tagClickListener)
        holder.tagsRecyclerView.adapter = tagsAdapter

    }

}

class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val userTextView: TextView = view.findViewById(R.id.tv_user)
    val previewImageView: ImageView = view.findViewById(R.id.iv_previewImg)
    val userImageView: ImageView = view.findViewById(R.id.iv_userImg)
    val tagsRecyclerView: RecyclerView = view.findViewById(R.id.rv_tags)
}

interface CellClickListener {
    fun onCellClickListener(place: NewsData)
}


