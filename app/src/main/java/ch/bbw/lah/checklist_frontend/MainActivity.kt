package ch.bbw.lah.checklist_frontend

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.bbw.lah.checklist_frontend.model.Checklist
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var checklist: ArrayList<Checklist>? = null

    private lateinit var gridView: GridView
    private lateinit var adapter: CustomAdapter

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.grid_view_1)

        checklist = ArrayList<Checklist>()

        adapter = CustomAdapter(checklist!!, applicationContext)
        gridView.adapter = adapter

        loadChecklist()

        val editText = findViewById<EditText>(R.id.editTextNewItem)
        val addButton = findViewById<Button>(R.id.buttonAdd)

        addButton.setOnClickListener {
            val itemName = editText.text.toString()
            if (itemName.isNotEmpty()) {

                val newItem = Checklist(
                    task = itemName,
                    completed = false,
                    dueDate = "2026-10-15"
                )

                lifecycleScope.launch {
                    try {
                        RetrofitClient.apiService.saveItem(newItem)
                        editText.text.clear()

                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(it.windowToken, 0)

                        loadChecklist()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item: Checklist = checklist!![position]
            item.completed = !item.completed
            adapter.notifyDataSetChanged()
            lifecycleScope.launch {
                try {
                    RetrofitClient.apiService.updateItem(item.checklistId, item)
                    loadChecklist()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadChecklist() {
        lifecycleScope.launch {
            try {
                val dbItems = RetrofitClient.apiService.getChecklist()
                checklist?.clear()
                checklist?.addAll(dbItems)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}