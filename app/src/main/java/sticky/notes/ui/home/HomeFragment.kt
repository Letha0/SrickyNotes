package sticky.notes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import sticky.notes.adapter.NotesAdapter
import sticky.notes.R
import sticky.notes.models.Note
import sticky.notes.ui.addnote.AddNoteFragment
import sticky.notes.ui.editnote.EditNoteFragment


class HomeFragment : Fragment() {

    private val addNoteFragment = AddNoteFragment()
    private val editNoteFragment = EditNoteFragment()
    lateinit var fStore: FirebaseFirestore
    lateinit var user: FirebaseUser
    lateinit var fAuth: FirebaseAuth
    private lateinit var showNotesAdapter : NotesAdapter
    var notesList: RecyclerView? = null

    private lateinit var database: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<View>(R.id.addNoteFloat).setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction!!.replace(R.id.nav_host_fragment, addNoteFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.currentUser!!;


        val query: Query = fStore.collection("notes").document(user.uid).collection("myNotes")
            .orderBy("title", Query.Direction.DESCENDING)


        val allNotes: FirestoreRecyclerOptions<Note> = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()


        showNotesAdapter = NotesAdapter(allNotes)

        notesList = view.findViewById(R.id.notesList)


        notesList?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        notesList?.adapter = showNotesAdapter

    }


    override fun onStart() {
        super.onStart()
        this.showNotesAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        this.showNotesAdapter.stopListening()
    }

}
