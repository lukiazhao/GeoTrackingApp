//package com.rmit.geotracking.controller;
//
//import android.content.Context;
//import android.view.ContextMenu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.Toast;
//
//import com.rmit.geotracking.R;
//
//public class ItemViewListener implements View.OnCreateContextMenuListener, View.OnContextClickListener {
//
//    private Context context;
//    private int position;
//
//    public ItemViewListener(Context context, int position) {
//        this.context = context;
//        this.position = position;
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        Toast.makeText(context, "you select: " + position , Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "view.getId()" + view.getId(), Toast.LENGTH_SHORT).show();
//        if (view.getId() == R.id.add_to_tracking){
//
//        }
//
//    }
//
//
//    @Override
//    public boolean onContextClick(View view) {
//        return false;
//    }
//}
