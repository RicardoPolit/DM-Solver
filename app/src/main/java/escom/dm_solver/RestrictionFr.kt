package escom.dm_solver

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_restriction.view.*

class RestrictionFr : Fragment(), View.OnClickListener {

    private var rid: Int = -1
    private var text: String? = null
    //private var listener: OnFragmentInteractionListener? = null

    companion object {
        fun newInstance(id:Int,text:String) = RestrictionFr().apply {
            arguments = Bundle().apply {
                putInt("ID",id)
                putString("TEXT",text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var viewReturned = inflater.inflate(R.layout.fragment_restriction, container, false)
        viewReturned.imageButton.setOnClickListener(this)
        viewReturned.label.text = text
        return viewReturned
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt("ID")?.let { rid = it }
        arguments?.getString("TEXT")?.let { text = it }
    }

    override fun onClick(v:View?){
        var act = activity as InputActivity
        act.removeRestriction(rid,this)
    }

}
