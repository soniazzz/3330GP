package hku.hk.cs.a3330gp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KmCallback


class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kommunicate.init(this@ChatBotActivity, "3d7accd86a7554188baee92120fbac99d")

        KmConversationBuilder(this@ChatBotActivity)
            .setSingleConversation(false) // Pass false if you would like to create new conversation every time user starts a conversation. This is true by default which means only one conversation will open for the user every time the user starts a conversation.
            .createConversation(object : KmCallback {
                override fun onSuccess(message: Any) {
                    val conversationId = message.toString()
                }

                override fun onFailure(error: Any) {
                    Log.d("ConversationTest", "Error : $error")
                }
            })

        Kommunicate.openConversation(this@ChatBotActivity);
    }
}