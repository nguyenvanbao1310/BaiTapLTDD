package com.example.sqlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite.Activity.MainActivity;
import com.example.sqlite.Model.NotesModel;

import com.example.sqlite.R;

import org.w3c.dom.Text;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<NotesModel> noteList;

    public NotesAdapter(Context context, int layout, List<NotesModel> noteList) {
        this.context = context;
        this.layout = layout;
        this.noteList = noteList;
    }

    @Override
    public int getCount()
    {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Tạo ViewHolder
    private class ViewHolder{
        TextView textViewNote;
        ImageView imageViewEdit;
        ImageView imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.textViewNote = (TextView) convertView.findViewById(R.id.textViewNameNote);
            viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);
            viewHolder.imageViewEdit = (ImageView) convertView.findViewById(R.id.imageViewEdit);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NotesModel notes = noteList.get(position);
        viewHolder.textViewNote.setText(notes.getNameNote());

        viewHolder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cập nhật " + notes.getNameNote(), Toast.LENGTH_SHORT).show();
                if (context instanceof MainActivity) {
                    ((MainActivity) context).DialogCapNhatNotes(notes.getNameNote(), notes.getIdNote());
                }
            }
        });

        viewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).DialogDelete(notes.getNameNote(), notes.getIdNote());
            }
        });

        return convertView;
    }

}
