package com.example.demochatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.demochatapp.R
import com.squareup.picasso.Picasso

class ViewFuLLImageActivity : AppCompatActivity() {

    private var img_viewer: ImageView?=null
    private var imgUrl: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_fu_l_l_image)
        imgUrl = intent.getStringExtra("url").toString()
        img_viewer = findViewById(R.id.imageView)
        Picasso.get().load(imgUrl).into(img_viewer)
    }
}