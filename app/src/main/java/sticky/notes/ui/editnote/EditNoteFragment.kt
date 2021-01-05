package sticky.notes.ui.editnote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import sticky.notes.MainActivity
import sticky.notes.R
import sticky.notes.SharedPref
import sticky.notes.auth.LoginActivity
import sticky.notes.ui.addnote.AddNoteFragment
import sticky.notes.ui.home.HomeFragment


class EditNoteFragment : Fragment() {

    var user = FirebaseAuth.getInstance().currentUser
    lateinit var fStore: FirebaseFirestore
    lateinit var session: SharedPref
    lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SharedPref(requireContext())

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.currentUser!!;

        val title = session.title
        editTitle.setText(title)
        val content = session.content
        editContent.setText(content)

        editnote.setOnClickListener {

            val titleData = editTitle.text.toString().trim()
            val contentData = editContent.text.toString().trim()

            if(titleData.isEmpty() || contentData.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Can not save note with empty fField.",
                    Toast.LENGTH_SHORT
                ).show();
                return@setOnClickListener
            }


            val docref: DocumentReference =
                fStore.collection("notes").document(user!!.uid).collection("myNotes")
                    .document(session.idNote.toString())

            val note: MutableMap<String, Any> = HashMap()
            note["title"] = titleData
            note["content"] = contentData

            docref.update(note).addOnSuccessListener(OnSuccessListener<Void?> {
                Toast.makeText(requireContext(), "Note Saved.", Toast.LENGTH_SHORT).show()

                val i = Intent(requireContext(), MainActivity::class.java)
                startActivity(i)

            }).addOnFailureListener(OnFailureListener {
                Toast.makeText(requireContext(), "Error, Try again.", Toast.LENGTH_SHORT).show()
            })

        }





    }

}
