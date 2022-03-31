package es.sdos.formacion.monumentosandaluces.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages

class MonumentListAdapter(listener: MonumentListAdapterListener):
    ListAdapter<MonumentWithImages, MonumentListAdapter.MonumentViewHolder>
    (MonumentsComparator()) {

    /** items click callback */
    private val monumentListItemListener: MonumentListAdapterListener = listener
    interface MonumentListAdapterListener{
        fun onItemClicked(id: Long)
        fun onLongItemClicked(id: Long)
        fun onFavClick(id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonumentViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_monument, parent, false)
        return MonumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonumentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, monumentListItemListener)
    }

     class MonumentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageItemVIew: ImageView = itemView.findViewById(R.id.monument_row__card_view__image)
        private val nameItemView: TextView = itemView.findViewById(R.id.monument_row__card_view__monument_txt)
        private val citytItemView: TextView = itemView.findViewById(R.id.monument_row__card_view__city_txt)
        private val startItemView: ImageView = itemView.findViewById(R.id.monument_row__card_view_start)

         fun bind(monument: MonumentWithImages, monumentListItemListener: MonumentListAdapterListener){
             nameItemView.text = monument.monumentDBO.name
             citytItemView.text = monument.monumentDBO.city
             startItemView.setImageResource(
                 if (monument.monumentDBO.isFavorite){
                     R.drawable.ic_start_true_favicon
                 } else {
                     R.drawable.ic_start_not_favicon
                 })

             val image = if (monument.images.isEmpty()) {
                 ""
             } else {
                 monument.images.first().url
             }

             Glide.with(imageItemVIew)
                 .load(image)
                 .centerCrop() //ScaleType
                 .placeholder(R.drawable.ic_launcher_background) //default_img
                 .error(R.drawable.ic_error) //error img
                 .fallback(R.drawable.ic_null_image) //null image
                 .into(imageItemVIew)

             startItemView.setOnClickListener {
                 monumentListItemListener.onFavClick(monument.monumentDBO.id)
             }
             itemView.setOnClickListener {
                 monumentListItemListener.onItemClicked(monument.monumentDBO.id)
             }
             itemView.setOnLongClickListener {
                 monumentListItemListener.onLongItemClicked(monument.monumentDBO.id)
                 true
             }
         }
     }


    class MonumentsComparator : DiffUtil.ItemCallback<MonumentWithImages>() {
        override fun areItemsTheSame(oldItem: MonumentWithImages, newItem: MonumentWithImages): Boolean =
            oldItem.monumentDBO.id == newItem.monumentDBO.id


        override fun areContentsTheSame(oldItem: MonumentWithImages, newItem: MonumentWithImages): Boolean =
            oldItem.monumentDBO.isFavorite == newItem.monumentDBO.isFavorite
    }
}