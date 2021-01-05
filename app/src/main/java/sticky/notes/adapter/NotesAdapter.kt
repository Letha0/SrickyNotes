package sticky.notes.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import sticky.notes.R
import sticky.notes.SharedPref
import sticky.notes.models.Note
import sticky.notes.ui.editnote.EditNoteFragment


class NotesAdapter(options: FirestoreRecyclerOptions<Note>) :
    FirestoreRecyclerAdapter<Note, NotesAdapter.ViewHolder>(options) {

    var user = FirebaseAuth.getInstance().currentUser
    lateinit var fStore: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_row_item, null)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Note) {
        holder.noteTitle.text = model.title
        holder.noteContent.text = model.content

        val see: FloatingActionButton = holder.view.findViewById(R.id.see)
        val edit: FloatingActionButton = holder.view.findViewById(R.id.edit)
        val delete: FloatingActionButton = holder.view.findViewById(R.id.delete)

        val docId: String = snapshots.getSnapshot(position).id
        fStore = FirebaseFirestore.getInstance();


        //SEE NOTE
        see.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(it.context)
            with(dialogBuilder)
            {
                setTitle(model.title + System.lineSeparator())
                setMessage(
                    model.content + System.lineSeparator()
                )
            }
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = dialogBuilder.create()
            alert.show()

        }

        //EDIT NOTE

        edit.setOnClickListener {
            var fragmentManager =
                (it.context as FragmentActivity).supportFragmentManager //to handle context
            val session = SharedPref(it.context)
            val fragmentTransaction :FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, EditNoteFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            session.getNoteToEdit(model.title, model.content, docId)
        }




        //DELETE NOTE
            delete.setOnClickListener {
                val context = it.context

                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setMessage("Are you sure you wan to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Delete") { dialog, id ->
                        val docRef: DocumentReference =
                            fStore.collection("notes").document(user!!.uid)
                                .collection("myNotes").document(docId)
                        docRef.delete().addOnSuccessListener {
                            // note deleted
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error in Deleting Note.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    .setNegativeButton("Dismiss"){ dialog, id->
                        dialog.dismiss()
                    }
                val alert = dialogBuilder.create()
                alert.show()

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN)

            }




    }


   // override fun getItemCount(): Int = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        var noteContent: TextView = itemView.findViewById(R.id.noteContent)
        var view: View = itemView


    }



}