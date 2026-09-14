package ch.bbw.lah.checklist_frontend

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import ch.bbw.lah.checklist_frontend.R

class MainActivity : AppCompatActivity() {

    // Declaring the DataModel Array
    private var dataModel: ArrayList<DataModel>? = null

    // Declaring the elements from the main layout file
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing the elements from the main layout file
        listView = findViewById<View>(R.id.list_view_1) as ListView

        // Initializing the model and adding data
        dataModel = ArrayList<DataModel>()
        dataModel!!.add(DataModel("Apple Pie", false))
        dataModel!!.add(DataModel("Banana Bread", false))
        dataModel!!.add(DataModel("Cupcake", false))
        dataModel!!.add(DataModel("Donut", true))
        dataModel!!.add(DataModel("Eclair", true))

        // Setting the adapter
        adapter = CustomAdapter(dataModel!!, applicationContext)
        listView.adapter = adapter

        // Setup the Input Logic
        val editText = findViewById<EditText>(R.id.editTextNewItem)
        val addButton = findViewById<Button>(R.id.buttonAdd)

        addButton.setOnClickListener {
            val itemName = editText.text.toString()
            if (itemName.isNotEmpty()) {
                // 1. Add new data to the list
                dataModel?.add(DataModel(itemName, false))

                // 2. Tell the adapter to refresh the screen
                adapter.notifyDataSetChanged()

                // 3. Clear the input
                editText.text.clear()

                // 4. Tastatur schließen
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }

        // Upon item click, checkbox will be set to checked
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item: DataModel = dataModel!![position]
            item.checked = !item.checked
            adapter.notifyDataSetChanged()
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