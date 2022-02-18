package com.hiddenDimension.thoughtmapper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewOfListAdapter extends RecyclerView.Adapter<RecyclerViewOfListAdapter.MyViewHolder> {


	private final List<String> mDataset;
	private OnItemClickListener listener;

	public RecyclerViewOfListAdapter(List<String> mDataset) {
		//  this.inflater = inflater;
		this.mDataset = mDataset;
		//   this.context = context;
	}

	// Provide a suitable constructor (depends on the kind of dataset)

	// Create new views (invoked by the layout manager)
	@NotNull
    @Override
	public RecyclerViewOfListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
		// ...
		return new MyViewHolder(v);
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(MyViewHolder holder,  int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element

		try {
			JSONObject med = new JSONObject(mDataset.get(holder.getBindingAdapterPosition()));
			holder.rtv1.setText(String.format("%s-%s", med.getString("1"), med.getString("3")));
			holder.kanji.setText(med.getString("0"));

		} catch (JSONException e) {
			holder.rtv1.setText(mDataset.get(holder.getBindingAdapterPosition()));
		}

		//4343holder.rtv1.setText(mDataset.get(position));


		holder.rtv1.setOnClickListener(v -> {

			listener.onClick(holder.getBindingAdapterPosition(), mDataset.get(holder.getBindingAdapterPosition()));
		});

		holder.kanji.setOnClickListener(v -> {

			listener.onKanjiClick( ((TextView)v).getText().toString());
		});


	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {

		this.listener = listener;
	}


	@SuppressWarnings("unused")
	public interface OnItemClickListener {

		@SuppressWarnings("EmptyMethod")
		void onClick(int position, String s);

		void onKanjiClick(String kanji);
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		final TextView rtv1, kanji;


		MyViewHolder(View v) {
			super(v);

			rtv1 = v.findViewById(R.id.rtv1);
			kanji = v.findViewById(R.id.kanji);

		}
	}


}