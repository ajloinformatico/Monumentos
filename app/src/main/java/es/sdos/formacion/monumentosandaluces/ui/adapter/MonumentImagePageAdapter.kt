package es.sdos.formacion.monumentosandaluces.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.dbo.ImageDBO

/**
 * Page adapter for images
 */
class MonumentImagePageAdapter(monumentImages: List<ImageDBO>,
                               context: Context): PagerAdapter() {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            as LayoutInflater

    private val monumentImagesCopy = monumentImages

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        //Inflate pager images
        val itemView: View = layoutInflater.inflate(R.layout.adapter_pager_monument_image, container, false)
        val monumentImage: ImageView = itemView.findViewById(R.id.monument_detail_img)
        val resource: String = monumentImagesCopy[position].url

        if (resource.isNotBlank() && resource.isNotBlank()){
            Glide.with(monumentImage)
                    .load(resource)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background) //default_img
                    .error(R.drawable.ic_error)
                    .fallback(R.drawable.ic_null_image) //null image
                    .into(monumentImage)
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, instance: Any) {
        container.removeView( instance as ConstraintLayout)
    }

    override fun getCount(): Int = monumentImagesCopy.size

    override fun isViewFromObject(view: View, instance: Any): Boolean = view == instance
}