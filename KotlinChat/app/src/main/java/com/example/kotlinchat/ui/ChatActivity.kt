package com.example.kotlinchat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kotlinchat.R
import com.example.kotlinchat.adapter.ChatsAdapter
import com.example.kotlinchat.databinding.ActivityChatBinding
import com.example.kotlinchat.databinding.ActivityMainBinding
import com.example.kotlinchat.model.Message
import com.example.kotlinchat.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mRef: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }
    private val mCurrentUserId by lazy { mAuth.currentUser?.uid }

    private lateinit var mChatUserId:String
    private lateinit var binding:ActivityChatBinding
    private lateinit var adapter:ChatsAdapter
    private var messageList: ArrayList<Message> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatUserName= intent.extras?.getString(Constants.EXTRA_NAME)
        mChatUserId= intent.extras?.getString(Constants.EXTRA_ID).toString()
        setSupportActionBar(binding.chatToolbar)
        supportActionBar?.title=chatUserName
        adapter= ChatsAdapter(this,messageList)
        binding.chatRecyclerView.layoutManager=LinearLayoutManager(this).also { it.stackFromEnd=true }
         binding.chatRecyclerView.adapter=adapter
        loadMesseages()
        binding.sendMessageBtn.setOnClickListener { sendMessage() }


    }

    private fun sendMessage() {
      val message=binding.sendMessageEdt.text.toString()
        if(message.isNotEmpty())
        {
            val currentUserRef = "messages/$mCurrentUserId/$mChatUserId"
            val chatUserRef = "messages/$mChatUserId/$mCurrentUserId"
            val userMessageRef: DatabaseReference = mRef.child(Constants.MESSAGES).child(mCurrentUserId!!).child(mChatUserId).push()
            val messageId = userMessageRef.key

            val messageMap: HashMap<String, Any> = HashMap()
            messageMap["message"] = message
            messageMap["time"] = System.currentTimeMillis().toString()
            messageMap["from"] = mCurrentUserId!!

            val messageUserMap = mutableMapOf<String, Any>()
            messageUserMap["$currentUserRef/$messageId"] = messageMap
            messageUserMap["$chatUserRef/$messageId"] = messageMap

           binding.sendMessageEdt.setText("")

            mRef.updateChildren(messageUserMap)


        }
    }

    private fun loadMesseages() {
        mRef.child(Constants.MESSAGES).child(mCurrentUserId!!).child(mChatUserId)
            .addChildEventListener(object:ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                   val message=snapshot.getValue(Message::class.java)
                    adapter.add(message!!)
                    binding.chatRecyclerView.scrollToPosition(binding.chatRecyclerView.adapter?.itemCount!!.minus(-1))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
}