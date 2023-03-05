// برای برقراری اتصال به Firebase
class FirebaseManager(private val context: Context) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    // ثبت‌نام کاربر جدید
    fun signUp(email: String, password: String, onSignUpResult: (Result<FirebaseUser>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSignUpResult(Result.Success(firebaseAuth.currentUser!!))
                } else {
                    onSignUpResult(Result.Error(task.exception))
                }
            }
    }

    // ورود کاربر به حساب کاربری خود
    fun signIn(email: String, password: String, onSignInResult: (Result<FirebaseUser>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSignInResult(Result.Success(firebaseAuth.currentUser!!))
                } else {
                    onSignInResult(Result.Error(task.exception))
                }
            }
    }

    // ارسال پیام در گروه یا به کاربر خاصی
    fun sendMessage(chatId: String, message: Message, onSendMessageResult: (Result<Unit>) -> Unit) {
        val databaseReference = firebaseDatabase.getReference("chats/$chatId/messages").push()
        databaseReference.setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSendMessageResult(Result.Success(Unit))
                } else {
                    onSendMessageResult(Result.Error(task.exception))
                }
            }
    }

    // دریافت لیست چت‌ها
    fun getChats(userId: String, onGetChatsResult: (Result<List<Chat>>) -> Unit) {
        val databaseReference = firebaseDatabase.getReference("users/$userId/chats")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chats = mutableListOf<Chat>()
                for (childSnapshot in dataSnapshot.children) {
                    val chatId = childSnapshot.key!!
                    val chat = childSnapshot.getValue(Chat::class.java)!!
                    chat.id = chatId
                    chats.add(chat)
                }
                onGetChatsResult(Result.Success(chats))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onGetChatsResult(Result.Error(databaseError.toException()))
            }
        })
    }

    // دریافت لیست پیام‌ها برای یک چت خاص
    fun getMessages(chatId: String, onGetMessagesResult: (Result<List<Message>>) -> Unit) {
        val databaseReference = firebaseDatabase.getReference("chats/$chatId/messages")
