package com.example.play_app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import android.widget.*
import com.example.play_app.db.PlayDatabase
import com.example.play_app.db.entity.Play
import kotlinx.android.synthetic.main.activity_play_list.*

class PlayListActivity : AppCompatActivity() {
    var place_current:Boolean = false
    var cost_current:Boolean = false
    var num_current:Boolean = false
    var act_current:Boolean = false

    private lateinit var mAdpater : MyCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)

        val db: PlayDatabase ?= PlayDatabase.getInstance(this)
        val item = db?.playDao()?.getAll() as ArrayList<Play>

        play_list_back_button.setOnClickListener {
            finish()
        }
      
        val plus = findViewById<ImageButton>(R.id.list_add_btn)
        plus.setOnClickListener {
            showPlus(db,item)
        }

        val listView = findViewById<ListView>(R.id.listView)
        mAdpater = MyCustomAdapter(this, item)
        listView.adapter = mAdpater

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val delbtn = findViewById<ImageButton>(R.id.list_del_btn)
        delbtn.setOnClickListener()
        {
            val checkedItem = listView.checkedItemPositions

            (mAdpater.count downTo 0)
                .filter { checkedItem.get(it) }
                .forEach {
                    db.playDao().delete(item.get(it))
                    item.removeAt(it)
                }

            listView.clearChoices()
            mAdpater.notifyDataSetChanged()
        }

    }


    fun showPlus(db:PlayDatabase,item:ArrayList<Play>){
        val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.add_popup_layout,null)
        val place_btn: Button = view.findViewById<Button>(R.id.place_button)
        val cost_btn: Button = view.findViewById<Button>(R.id.cost_button)
        val num_btn: Button = view.findViewById<Button>(R.id.num_button)
        val act_btn: Button = view.findViewById<Button>(R.id.act_button)

        fun p_btn(){
            if(!place_current){
                place_btn.setText("실외")
                place_current = true
            }
            else{
                place_btn.setText("실내")
                place_current = false
            }
        }
        place_btn.setOnClickListener(View.OnClickListener(){
            p_btn()
        }
        )

        fun c_btn(){
            if(!cost_current){
                cost_btn.setText("유료")
                cost_current = true
            }
            else{
                cost_btn.setText("무료")
                cost_current = false
            }
        }
        cost_btn.setOnClickListener(View.OnClickListener(){
            c_btn()
        }
        )

        fun n_btn(){
            if(!num_current){
                num_btn.setText("여럿이서")
                num_current = true
            }
            else{
                num_btn.setText("혼자서")
                num_current = false
            }
        }
        num_btn.setOnClickListener(View.OnClickListener(){
            n_btn()
        }
        )

        fun a_btn(){
            if(!act_current){
                act_btn.setText("비활동적")
                act_current = true
            }
            else{
                act_btn.setText("활동적")
                act_current = false
            }
        }
        act_btn.setOnClickListener(View.OnClickListener(){
            a_btn()
        }
        )

        val alertDialog =
            AlertDialog.Builder(this).setCancelable(false).create()


        val close_button = view.findViewById<ImageButton>(R.id.close)
        close_button.setOnClickListener {
            alertDialog.cancel()
        }
        val save = view.findViewById<Button>(R.id.save_button)
        save.setOnClickListener() {
            val addPlayName = view.findViewById<EditText>(R.id.add_playname)
            item.add(Play(item.last().play_id+1,addPlayName?.text.toString(),place_btn.text.toString(),cost_btn.text.toString(),num_btn.text.toString(),act_btn.text.toString()))
            db.playDao().insert(item.last())
            mAdpater.notifyDataSetChanged()
            alertDialog.cancel()

        }
        alertDialog.setView(view)
        alertDialog.show()
        alertDialog.window?.setLayout(1000, 1350)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

}



