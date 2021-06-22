package com.example.mydiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.Adapter.ToDoAdapter;

import static android.R.color.holo_red_dark;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{

    private ToDoAdapter adapter;
    private RecyclerView.ViewHolder viewHolder;
    private RecyclerView recyclerView;

    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("DELETE TASK");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog= builder.create();
            dialog.show();
        }
        else {
            adapter.editItem(position);
        }
    }
       @SuppressLint("ResourceAsColor")
       public void onChildDraw(Canvas c, RecyclerView recyclerview, RecyclerView.ViewHolder viewHolder,
                               float dX, float dY, int actionState, boolean isCurrentlyActive){
            super.onChildDraw(c, recyclerView, viewHolder ,dX ,dY ,actionState, isCurrentlyActive);

            Drawable icon;
            ColorDrawable background;

            View itemView= viewHolder.itemView;
            int backgroundCornerOffset=20;

            if(dX>0){
                icon= ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit_edit);
                background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark));
            }
            else{
                icon= ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
                background=new ColorDrawable(R.color.colorPrimaryDark);
            }
            int iconMargin=(itemView.getHeight()-icon.getIntrinsicHeight())/2;
            int iconTop= itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
            int iconBottom=iconTop+icon.getIntrinsicHeight();

            if (dX > 0) {//swiped right
                int iconLeft= itemView.getLeft()+iconMargin;
                int iconRight= itemView.getLeft()+iconMargin+icon.getIntrinsicHeight();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft()+((int)dX)+ backgroundCornerOffset, itemView.getBottom());
            }
            else if (dX<0){//swiped left
                int iconLeft= itemView.getRight() - iconMargin- icon.getIntrinsicWidth();
                int iconRight= itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() +((int)dX)-  backgroundCornerOffset,
                        itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
            }
            else{
                background.setBounds(0,0,0,0);
            }
        background.draw(c);
        icon.draw(c);}
}
