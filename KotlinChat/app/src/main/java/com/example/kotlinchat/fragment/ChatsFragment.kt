package com.example.kotlinchat.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinchat.adapter.FriendsAdapter
import com.example.kotlinchat.databinding.FragmentChatsBinding
import com.example.kotlinchat.model.User
import com.example.kotlinchat.ui.ChatActivity
import com.example.kotlinchat.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class ChatsFragment:Fragment() ,FriendsAdapter.OnFriendClickListener{
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val mMessageDatabase: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference.child(Constants.MESSAGES)
    }
    private val mUserDatabase: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference.child(Constants.CHILD_USERS)
    }
    private lateinit var adapter: FriendsAdapter
    private var userList: ArrayList<User> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUserId=FirebaseAuth.getInstance().currentUser.uid
        adapter= FriendsAdapter(activity!!,userList)
        binding.chatRecyclerView.layoutManager=LinearLayoutManager(activity)
        binding.chatRecyclerView.adapter=adapter
        mMessageDatabase.child(currentUserId!!).addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               if(snapshot.value!=null)
               {
                   try {
                       val chatFriendKey=snapshot.ref.key
                        getUsers(chatFriendKey)

                   }catch (e:Exception){

                   }
               }
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
        adapter.setOnFriendClickListener(this)
    }

    private fun getUsers(chatFriendKey: String?) {
        mUserDatabase.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.value!=null){
                    val users=snapshot.getValue(User::class.java)
                    val friendKey=snapshot.ref.key
                    if(chatFriendKey==friendKey)
                    {
                        userList.add(users!!)
                        adapter.notifyItemInserted(userList.size-1)
                    }
                }
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

    override fun onFriendClick(user: User) {
        mUserDatabase.orderByChild(Constants.CHIELD_NAME).equalTo(user.name)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clickedUserKey=snapshot.children.iterator().next().ref.key//tıklanan user key
                    val intent= Intent(activity, ChatActivity::class.java)
                    intent.putExtra(Constants.EXTRA_NAME,user.name)
                    intent.putExtra(Constants.EXTRA_ID,clickedUserKey)
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

}
