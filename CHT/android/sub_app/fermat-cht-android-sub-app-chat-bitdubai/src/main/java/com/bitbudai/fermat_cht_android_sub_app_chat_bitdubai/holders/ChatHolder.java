package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;

//import android.widget.ImageView;

/**
 * ChatHolder ViewHolder
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 05/01/15.
 * @version 1.0
 */
public class ChatHolder extends FermatViewHolder {

    public TextView txtMessage;//TextView
    public TextView txtInfo;//TextView
    public LinearLayout content;
    public LinearLayout contentWithBG;

    //private static ChatsList chats;

    /**
     * Constructor
     *
     * @param itemView
     */
    public ChatHolder(View itemView) {
        super(itemView);

        txtMessage      = (TextView)  itemView.findViewById(R.id.txtMessage);
        content         = (LinearLayout)    itemView.findViewById(R.id.content);
        contentWithBG   = (LinearLayout)    itemView.findViewById(R.id.contentWithBackground);
        txtInfo         = (TextView)  itemView.findViewById(R.id.txtInfo);

        /*rowView = (LinearLayout) itemView.findViewById(R.id.row_view);
        bitcoins = (FermatTextView) itemView.findViewById(R.id.asset_bitcoins);*/
    }

    public TextView getTxtMessage() {
        return txtMessage;
    }

    public TextView getTxtInfo() {
        return txtInfo;
    }

    public LinearLayout getContent() {
        return content;
    }

    public LinearLayout getContentWithBG() {
        return contentWithBG;
    }
}