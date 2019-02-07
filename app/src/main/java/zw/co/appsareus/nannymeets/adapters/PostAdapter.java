package zw.co.appsareus.nannymeets.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnEmployeeClickListener;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.viewHolders.PostViewHolder;


public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final Context context;
    private final ArrayList<Employee> maids;
    private OnPostClickedlistener listener;

    public PostAdapter(Context context, ArrayList<Employee> maids) {
        this.context = context;
        this.maids = maids;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_maid_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.setData(maids.get(position));
        holder.setOnEmployeeClickListener(new OnEmployeeClickListener() {
            @Override
            public void onMaidClick(Employee maid) {
                listener.getPostClicked(maid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return maids.size();
    }

    public interface OnPostClickedlistener{
        void getPostClicked(Employee mPost);
    }
    public void setOnPostClickedlistener(OnPostClickedlistener listener){
        this.listener = listener;
    }
}
