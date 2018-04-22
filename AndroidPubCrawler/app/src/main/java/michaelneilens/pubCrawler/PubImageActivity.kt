package michaelneilens.pubCrawler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PubImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("photoURL")

        setContentView(R.layout.activity_pub_image)
        val imageView = findViewById<View>(R.id.imageView) as ImageView
        Picasso.with(this).load(url).into(imageView)
    }

    companion object {
        const val PHOTO_URL = "photoURL"
    }

}
