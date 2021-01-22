package com.example.demochatapp.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.demochatapp.Model.Chat
import com.example.demochatapp.R
import com.example.demochatapp.activities.MainActivity
import com.example.demochatapp.activities.MessageChatActivity
import com.example.demochatapp.activities.ViewFuLLImageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    mContext: Context,
    mChatList: List<Chat>,
    imgUrl: String
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val mContext: Context?
    private val mChatList: List<Chat>
    private val imgUrl: String
    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mContext = mContext
        this.mChatList = mChatList
        this.imgUrl = imgUrl
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return if (position == 1) {
            val view: View =
                LayoutInflater.from(mContext)
                    .inflate(com.example.demochatapp.R.layout.message_item_right, parent, false)
            ViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(mContext)
                    .inflate(com.example.demochatapp.R.layout.message_item_left, parent, false)
            ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat: Chat = mChatList[position]
        Picasso.get().load(imgUrl).into(holder.profile_img)

        //send Img
        if (chat.getMessage().equals("send you an image") && !chat.getUrl().equals("")) {

            //img message-right
            if (chat.getSender().equals(firebaseUser!!.uid)) {
                holder.text_message!!.visibility = View.GONE
                holder.right_img!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.right_img)

                holder.right_img!!.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image",
                        "Cancel"
                    )

                    val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")

                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(mContext, ViewFuLLImageActivity::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext!!.startActivity(intent)
                        } else if (which == 1) {
                            deleteSentMessage(position, holder)
                        }
                    })
                    builder.show()
                }
            }
            //img message-left
            else if (!chat.getSender().equals(firebaseUser!!.uid)) {
                holder.text_message!!.visibility = View.GONE
                holder.left_img!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.left_img)

                holder.left_img!!.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Cancel"
                    )

                    val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")

                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(mContext, ViewFuLLImageActivity::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext!!.startActivity(intent)
                        }
                    })
                    builder.show()
                }
            }
        }
        //text messeage
        else {
            holder.text_message!!.text = chat.getMessage()
           if (firebaseUser!!.uid == chat.getSender()){
               holder.text_message!!.setOnClickListener {
                   val options = arrayOf<CharSequence>(
                       "Delete Message",
                       "Cancel"
                   )

                   val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                   builder.setTitle("What do you want?")

                   builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                       if (which == 0) {
                           deleteSentMessage(position, holder)
                       }
                   })
                   builder.show()
               }
           }
        }

        if (position == mChatList.size - 1) {
            if (chat.getIsSeen()) {
                holder.textSeen!!.text = "Seen"
                if (chat.getMessage().equals("send you an image") && !chat.getUrl().equals("")) {
                    val lp: RelativeLayout.LayoutParams? =
                        holder.textSeen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.textSeen!!.layoutParams = lp
                }
            } else {
                holder.textSeen!!.text = "Sent"
                if (chat.getMessage().equals("send you an image") && !chat.getUrl().equals("")) {
                    val lp: RelativeLayout.LayoutParams? =
                        holder.textSeen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.textSeen!!.layoutParams = lp
                }
            }
        } else {
            holder.textSeen!!.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mChatList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profile_img: CircleImageView? = null
        var text_message: TextView? = null
        var left_img: ImageView? = null
        var textSeen: TextView? = null
        var right_img: ImageView? = null

        init {
            profile_img = itemView.findViewById(R.id.profile_image)
            textSeen = itemView.findViewById(R.id.text_seen)
            text_message = itemView.findViewById(R.id.text_message)
            left_img = itemView.findViewById(R.id.left_image_view)
            right_img = itemView.findViewById(R.id.right_image_view)

        }

    }

    override fun getItemViewType(position: Int): Int {

        Log.d("AAA", mChatList[position].getSender())
        Log.d("AAA", firebaseUser!!.uid.toString())

        return if (mChatList[position].getSender().equals(firebaseUser!!.uid)) {
            1
        } else {
            0
        }
    }

    private fun deleteSentMessage(position: Int, holder: ViewHolder) {
        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
            .child(mChatList.get(position).getMessageId())
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Deleted.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed, Not Deleted.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

}