package sticky.notes.ui.addnote

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_add_note.*
import sticky.notes.MainActivity
import sticky.notes.R
import sticky.notes.ui.home.HomeFragment


class AddNoteFragment : Fragment() {

    lateinit var fStore :FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser

        fStore = FirebaseFirestore.getInstance()

        addNote.setOnClickListener {
            val titleData = title.text.toString().trim()
            val contentData = content.text.toString().trim()

            if(titleData.isEmpty() || contentData.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Can not save note with empty field.",
                    Toast.LENGTH_SHORT
                ).show();
                return@setOnClickListener
            }


            val docRef :DocumentReference = fStore.collection("notes")
                .document(user!!.uid).collection(
                "myNotes"
            ).document()

            val note: MutableMap<String, Any> = HashMap()
            note["title"] = titleData
            note["content"] = contentData

            docRef.set(note).addOnSuccessListener(OnSuccessListener<Void>() {

                Toast.makeText(requireContext(), "Note Added.", Toast.LENGTH_SHORT).show()

                val i = Intent(requireContext(), MainActivity::class.java)
                startActivity(i)

            }).addOnFailureListener(OnFailureListener()
            {
                Toast.makeText(requireContext(), "Note Added.", Toast.LENGTH_SHORT).show();
            })


        }

    }



    }
