package org.hyperskill.secretdiary

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import java.util.*
const val PREFERENCES_NAME = "PREF_DIARY"

class MainActivity : AppCompatActivity() {
    private lateinit var save: Button
    private lateinit var undo: Button
    private lateinit var edit: EditText
    private lateinit var view: TextView
    private lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        save = findViewById(R.id.btnSave)
        undo = findViewById(R.id.btnUndo)
        edit = findViewById(R.id.etNewWriting)
        view = findViewById(R.id.tvDiary)
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        view.text = sharedPreferences.getString("KEY_DIARY_TEXT","")
        val editor = sharedPreferences.edit()
        save.setOnClickListener {
            if (edit.text.toString().isNotBlank()){

                SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
                view.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(Clock.System.now().toEpochMilliseconds()).toString()+ "\n" + edit.text.toString() + if (view.text.isNotBlank()) "\n\n"+view.text else ""
                edit.setText("")
            } else {
                val toast = Toast.makeText(this, "Empty or blank input cannot be saved", Toast.LENGTH_LONG)
                toast.show()
            }
            editor.putString("KEY_DIARY_TEXT", view.text.toString()).apply()
        }


        undo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Remove last note")
                .setMessage("Do you really want to remove the last writing? This operation cannot be undone!")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val k = view.text.toString().indexOf("\n\n")
                    if (k != -1) view.setText(view.text.toString().substring(k+2))
                    else view.text = ""
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
            editor.putString("KEY_DIARY_TEXT", view.text.toString()).apply()
        }


    }
}