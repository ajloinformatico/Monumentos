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

class MyAndLikeMonumentsAdapter(listener: MyAndLikeMonumentsAdapterListener):
    ListAdapter<MonumentWithImages, MyAndLikeMonumentsAdapter.MyMonumentViewHolder>
    (MyMonumentComparator()) {

    /** MonumentListAdapterListener callback*/
    private var myMonumentsAndLikesListener: MyAndLikeMonumentsAdapterListener = listener
    interface  MyAndLikeMonumentsAdapterListener{
        fun onItemClicked(id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMonumentViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_like_and_my_monument, parent, false)
        return MyMonumentViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyMonumentViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current, myMonumentsAndLikesListener)
    }

    class MyMonumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val monumentImageView: ImageView = itemView.findViewById(R.id.like_and_my_monument_row__image)
        private val monumentName: TextView = itemView.findViewById(R.id.like_and_my_monument_row__name)
        private val monumentDescription: TextView = itemView.findViewById(R.id.like_and_my_monument_row__description)

        fun bind(monument: MonumentWithImages, myAndLikeMonumentsAdapterListener: MyAndLikeMonumentsAdapterListener) {
            val image = if (monument.images.isEmpty()) "" else monument.images.random().url
            val description = if (monument.monumentDBO.description!=null) {
                if (monument.monumentDBO.description.length > 90){
                    monument.monumentDBO.description.subSequence(0,90).toString() + " ..."
                } else {
                    monument.monumentDBO.description + " ..."
                }
            } else {
                "No description"
            }

            monumentName.text = monument.monumentDBO.name
            monumentDescription.text = description

            Glide.with(monumentImageView)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background) //default_img
                .error(R.drawable.ic_error) //error img
                .fallback(R.drawable.ic_null_image) //null image
                .into(monumentImageView)


            //Note: Onclick to load monumentDetail by callback
            itemView.setOnClickListener {
                myAndLikeMonumentsAdapterListener.onItemClicked(monument.monumentDBO.id)
            }
        }
    }

    /** Diff Util*/
    class MyMonumentComparator: DiffUtil.ItemCallback<MonumentWithImages>() {
        override fun areItemsTheSame( oldItem: MonumentWithImages, newItem: MonumentWithImages): Boolean
        = oldItem.monumentDBO.id == newItem.monumentDBO.id


        override fun areContentsTheSame( oldItem: MonumentWithImages, newItem: MonumentWithImages ): Boolean
        = oldItem.monumentDBO.name == newItem.monumentDBO.name
    }
}