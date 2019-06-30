package com.clay.tspsurat.fragment;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clay.tspsurat.R;
import com.clay.tspsurat.fragment.NodeFragment.OnListFragmentInteractionListener;
import com.clay.tspsurat.fragment.dummy.DummyContent.DummyItem;
import com.clay.tspsurat.model.Node;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

    private final List<Node> mValues;
    private final OnListFragmentInteractionListener mListener;

    public NodeAdapter(List<Node> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_node, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText((position+1)+"");

        String text = "";

        switch (mValues.get(position).getKelas()) {
            case 0:
                text += "KANTOR-"+holder.mItem.getKeterangan()+"-"+holder.mItem.getNama();
                break;
            case 1:
                text += "RW-"+holder.mItem.getNomor()+"-"+holder.mItem.getNama();
                break;
            case 2:
                text += "RT-"+holder.mItem.getNomor()+"-"+holder.mItem.getNama();
                break;
            case 3:
                text += "SIMPANGAN-"+holder.mItem.getNomor()+"-"+holder.mItem.getNama();
                break;
        }

//        holder.mIdView.setText(holder.mItem.getId()+"");
        holder.mContentView.setText(text);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Node mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
