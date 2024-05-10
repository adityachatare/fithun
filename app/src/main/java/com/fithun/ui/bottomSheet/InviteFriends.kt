package com.fithun.ui.bottomSheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fithun.R
import com.fithun.utils.setSafeOnClickListener

class InviteFriends(private val referCode: String) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.invite_friend_refer, container, false)
        val referCodeTv:TextView =  view.findViewById(R.id.referCodeTV)
        val inviteImageView: ImageView = view.findViewById(R.id.InviteClick)
        val copyClick: ImageView = view.findViewById(R.id.copyClick)
        val infoIconClick: ImageView = view.findViewById(R.id.inforIconClick)
        referCodeTv.text =  referCode

        val message = "Check out this link:"
        val url = "https://www.example.com"


        infoIconClick.setSafeOnClickListener {
            parentFragmentManager.let {
                WalletDialog("Share and Earn", "Use this refer code to earn coin.").show(it, "MyCustomFragment")
            }
        }


        inviteImageView.setOnClickListener {
            val whatsappLink = generateWhatsAppLink(message, url)
            shareOnWhatsApp(whatsappLink)
        }


        copyClick.setSafeOnClickListener {
            copyToClipboard(requireContext(),referCode,copyClick)
        }



        return view
    }






    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.invite_friend_refer, null)
        dialog.setContentView(contentView)



        contentView.post {
            val contentHeight = contentView.height
            val peekHeightFraction = 0.75
            val peekHeight = (contentHeight * peekHeightFraction).toInt()

            val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            if (behavior != null && behavior is BottomSheetBehavior) {
                behavior.peekHeight = peekHeight
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme


    // Function to generate WhatsApp shareable link
    private fun generateWhatsAppLink(message: String, url: String): String {
        val encodedMessage = Uri.encode(message)
        val encodedUrl = Uri.encode(url)
        return "https://wa.me/?text=$encodedMessage%20$encodedUrl"
    }

    // Function to open WhatsApp and share the link
    private fun shareOnWhatsApp(link: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, link)
        sendIntent.type = "text/plain"
        sendIntent.`package` = "com.whatsapp" // Specify WhatsApp package name

        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed, handle the error here
            Toast.makeText(requireContext(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToClipboard(context: Context, text: String, copyClick: ImageView) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        copyClick.isEnabled = false
        Glide.with(requireContext()).load(R.drawable.filled_icon_tracking).into(copyClick)
        Toast.makeText(context, "Refer code copied to clipboard", Toast.LENGTH_SHORT).show()
    }


}