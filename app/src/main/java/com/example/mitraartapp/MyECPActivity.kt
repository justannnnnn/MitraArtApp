package com.example.mitraartapp

//import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.integrity.p


class MyECPActivity : AppCompatActivity() {

    private val ecpService: ECPService // Объект LotService
        get() = (applicationContext as App).ecpService
    lateinit var ecps: MutableList<ECP>
    lateinit var adapter: ECPAdapter
    lateinit var rvECPs: RecyclerView
    private var p = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ecpactivity)

        // Back button
        var backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // RecyclerView
        rvECPs = findViewById<View>(R.id.ecp_RecyclerView) as RecyclerView
        ecps = ecpService.getECPs()
        adapter = ECPAdapter(ecps)
        rvECPs.adapter = adapter
        rvECPs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        enableSwipe()

    }

    private fun enableSwipe() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    val deletedModel: ECP = ecps.get(position)
                    adapter.removeItem(position)
                    // showing snack bar with Undo option
                    val snackbar = Snackbar.make(
                        window.decorView.rootView,
                        "ЭЦП был удален",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("ОТМЕНИТЬ") { // undo is selected, restore the deleted item
                        adapter.restoreItem(deletedModel, position)
                    }
                    snackbar.setActionTextColor(Color.MAGENTA)
                    snackbar.show()
                } /*else {
                    val deletedModel: ECP = ecps.get(position)
                    adapter.removeItem(position)
                    // showing snack bar with Undo option
                    val snackbar = Snackbar.make(
                        window.decorView.rootView,
                        " removed from Recyclerview!",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") { // undo is selected, restore the deleted item
                        adapter.restoreItem(deletedModel, position)
                    }
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }*/
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    /*if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"))
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                        val icon_dest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top.toFloat() + width,
                            itemView.left.toFloat() + 2 * width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {*/
                        p.setColor(Color.parseColor("#D32F2F"))
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRoundRect(background, 80f, 80f, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                        val icon_dest = RectF(
                            itemView.right.toFloat() - 2 * width,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, icon_dest, p)
                    //}
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvECPs)
    }
}