import android.annotation.SuppressLint
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc01.LockerAlbum
import com.example.umc01.Song
import com.example.umc01.databinding.ItemLockerAlbumBinding


class LockerAlbumRVAdapter (private val lockeralbumList : ArrayList<LockerAlbum>) : RecyclerView.Adapter<LockerAlbumRVAdapter.ViewHolder>() {


    interface  LockerItemClickListener{
        fun onLockerItemClick(album: LockerAlbum)
        fun onLockerRemoveSong(position: Int)
    }

    private lateinit var lItemClickListener : LockerItemClickListener

    fun setLockerItemClickListener(itemClickListener: LockerItemClickListener){
        lItemClickListener = itemClickListener
    }

    fun addSong(album: LockerAlbum){
        lockeralbumList.add(album)
        notifyDataSetChanged()
    }

    fun removeSong(position: Int){
        lockeralbumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRVAdapter.ViewHolder, position: Int) {


        holder.bind(lockeralbumList[position])

//        holder.itemView.setOnClickListener { lItemClickListener.onLockerItemClick(lockeralbumList[position])
//        }


        //more 버튼이 눌렸을 때 클릭리스너
        holder.binding.itemLockerAlbumMoreIv.setOnClickListener {
            lItemClickListener.onLockerRemoveSong(position)
        }



    }

    override fun getItemCount(): Int = lockeralbumList.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(lockerAlbum: LockerAlbum){
            binding.itemLockerAlbumTitleTv.text = lockerAlbum.lockertitle
            binding.itemLockerAlbumSingerTv.text = lockerAlbum.lockersinger
            binding.itemLockerAlbumCoverImgIv.setImageResource(lockerAlbum.lockercoverImg!!)
        }
    }



}