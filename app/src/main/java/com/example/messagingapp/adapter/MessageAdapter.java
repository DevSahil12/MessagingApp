package com.example.messagingapp.adapter;

import static com.example.messagingapp.mainpages.ChatWindow.receiverImg;
import static com.example.messagingapp.mainpages.ChatWindow.senderImg;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.messagingapp.R;
import com.example.messagingapp.modalclass.MessageModalClass;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
public class MessageAdapter extends RecyclerView.Adapter {
        Context context;
        ArrayList<MessageModalClass> messagesAdpterArrayList;
        int ITEM_SEND=1;
        int ITEM_RECEIVE=2;
        public MessageAdapter(Context context, ArrayList<MessageModalClass> messagesAdpterArrayList) {
            this.context = context;
            this.messagesAdpterArrayList = messagesAdpterArrayList;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_SEND){
                View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
                return new SenderViewHolder(view);
            }else {
                View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
                return new ReciverViewHolder(view);
            }

        }
    @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageModalClass messages = messagesAdpterArrayList.get(position);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context).setTitle("Delete")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                    return false;
                }
            });
            if (holder.getClass()==SenderViewHolder.class){
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.msgtxt.setText(messages.getMessage());
                Picasso.get().load(senderImg).into(viewHolder.circleImageView);
            }else { ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
                viewHolder.msgtxt.setText(messages.getMessage());
                Picasso.get().load(receiverImg).into(viewHolder.circleImageView);
            }
        }
        @Override
        public int getItemCount() {
            return messagesAdpterArrayList.size();
        }
        @Override
        public int getItemViewType(int position) {
            MessageModalClass messages = messagesAdpterArrayList.get(position);
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(messages.getSenderId())) {
                return ITEM_SEND;
            } else {
                return ITEM_RECEIVE;
            }
        }
        static class  SenderViewHolder extends RecyclerView.ViewHolder {
            CircleImageView circleImageView;
            TextView msgtxt;

            public SenderViewHolder(@NonNull View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.profilerggg);
                msgtxt = itemView.findViewById(R.id.msgsendertyp);

            }
        }
        static class ReciverViewHolder extends RecyclerView.ViewHolder {
            CircleImageView circleImageView;
            TextView msgtxt;
            public ReciverViewHolder(@NonNull View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.pro);
                msgtxt = itemView.findViewById(R.id.recivertextset);
            }
        }
    }
