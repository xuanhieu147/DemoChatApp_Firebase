package com.example.demochatapp.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.demochatapp.Model.Users
import com.example.demochatapp.R
import com.example.demochatapp.activities.MainActivity
import com.example.demochatapp.activities.MessageChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    mContext: Context,
    mUsers: List<Users>,
    isChatCheck: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<Users>
    private var isChatCheck: Boolean

    init {
        this.mContext = mContext
        this.mUsers = mUsers
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.user_search_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users: Users = mUsers[position]
        holder.tvUserName.text = users!!.getUserName()
        Picasso.get().load(users.getProfile()).into(holder.imgProfile)
        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )

            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setTitle("What do you want?")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    val intent = Intent(mContext, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", users.getUID())
                    mContext.startActivity(intent)
                } else if (which == 1) {

                }
            })
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUserName: TextView
        var imgProfile: CircleImageView
        var imgOnline: CircleImageView
        var imgOffline: CircleImageView
        var tvLastMessage: TextView

        init {
            tvUserName = itemView.findViewById(R.id.user_name)
            imgProfile = itemView.findViewById(R.id.profile_image)
            imgOnline = itemView.findViewById(R.id.image_online)
            imgOffline = itemView.findViewById(R.id.image_offline)
            tvLastMessage = itemView.findViewById(R.id.message_last)

        }

    }


}