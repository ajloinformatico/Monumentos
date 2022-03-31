package es.sdos.formacion.monumentosandaluces.ui.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.util.setVisibility

class MonumentAddImagesAdapter(listenerRemove: MonumentAddImagesAdapterListener): ListAdapter<Uri,
    MonumentAddImagesAdapter.MonumentAddImagesViewHolder> (ImageComparator()) {

    interface MonumentAddImagesAdapterListener {
        fun onRemoveImage(position: Int)
        fun onItemAdd()
    }

    private val listenerMonumentAddImagesAdapterListener : MonumentAddImagesAdapterListener = listenerRemove

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonumentAddImagesViewHolder{
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_add_monument_image, parent, false)
        return MonumentAddImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonumentAddImagesViewHolder, position: Int) {
        val uriImage = getItem(position)
        holder.bind(uriImage, listenerMonumentAddImagesAdapterListener)
    }

    class MonumentAddImagesViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView) {
        private val imagePreview: ImageView = itemView.findViewById(R.id.add_monument__recycler_image)
        private val deleteImage: ImageView = itemView.findViewById(R.id.delete_icon)


        fun bind(uriImage: Uri, listenerMonumentAddImagesAdapterListener: MonumentAddImagesAdapterListener){
            deleteImage.setVisibility(uriImage != Uri.EMPTY)

            if (uriImage == Uri.EMPTY) {
                imagePreview.setImageResource(R.drawable.ic_add_icon)
                imagePreview.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_float_action_button))
                imagePreview.setOnClickListener {
                    listenerMonumentAddImagesAdapterListener.onItemAdd()
                }

            } else {
                imagePreview.setImageURI(uriImage)
                deleteImage.setOnClickListener {
                    listenerMonumentAddImagesAdapterListener.onRemoveImage(position)
                }
            }

        }
    }

    class ImageComparator: DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
            oldItem.toString() == newItem.toString()

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
            oldItem.toString() == newItem.toString()
    }
}