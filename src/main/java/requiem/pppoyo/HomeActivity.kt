package requiem.pppoyo

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import java.nio.file.Files.delete







class HomeActivity : AppCompatActivity() {

    private var btnLogOut: Button? = null
    private var btnChange: Button? = null
    private var etMesage: EditText? = null
    private var btnSend: Button? = null
    private var mAuth:FirebaseAuth? = null
    private val usr: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var credential = EmailAuthProvider.getCredential("user@example.com", "password1234")
    private var mDatabase: DatabaseReference? = null
    private var mReference: DatabaseReference? = null
    private var mesages: mesage? = null
    private var tvMessages: TextView? = null

    val messageListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val messagess = dataSnapshot.getValue(mesage::class.java)
                // ...
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Failed to read value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initialice()

    }

    private fun initialice(){
        btnLogOut = findViewById(R.id.logout)
        btnChange = findViewById(R.id.changeData)
        btnSend = findViewById(R.id.send)
        etMesage = findViewById(R.id.message)
        tvMessages = findViewById(R.id.mes)

        mesages?.mesage = etMesage?.text.toString()

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mReference = FirebaseDatabase.getInstance().getReference("messages")

        btnLogOut!!.setOnClickListener{
            mAuth!!.signOut()
            updateUI()}
        btnChange!!.setOnClickListener{changeData()}
        btnSend!!.setOnClickListener{sendMessage()}

        mReference!!.addValueEventListener(messageListener)


    }

    private fun updateUI() {
        Toast.makeText(this@HomeActivity, "Saliendo.",
                Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun changeData(){
        usr!!.reauthenticate(credential)
                .addOnCompleteListener(this, {
                    Log.d("aa", "User re-authenticated.")
                    usr.delete()
                            .addOnCompleteListener( { task ->
                                if (task.isSuccessful) {
                                    Log.d("", "User account deleted.")
                                }
                            })
                })
        Toast.makeText(this@HomeActivity, "Se borro su cuenta.",
                Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun sendMessage(){
        mReference!!.setValue(mesages)
        Toast.makeText(this@HomeActivity, "Mensaje enviado.",
                Toast.LENGTH_SHORT).show()

        mReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvMessages!!.text = snapshot.value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
