package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 07/03/2018.
 */

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

class ProgressFragment:Fragment(){

    private lateinit var progressBar:ProgressBar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val layout = R.layout.fragment_progress
        val view = inflater!!.inflate(layout, container, false)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

}