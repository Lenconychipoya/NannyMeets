package zw.co.appsareus.nannymeets.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnEmployeeClickListener;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.views.SquareImageView;


public class PostViewHolder extends RecyclerView.ViewHolder {
    private SquareImageView image;
    private TextView name;
    private TextView age;
    private TextView jobType;
    private View v;
    private OnEmployeeClickListener onEmployeeClickListener;

    public PostViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        image = itemView.findViewById(R.id.siv_profile);
        name = itemView.findViewById(R.id.tv_name);
        jobType = itemView.findViewById(R.id.tv_job_type);
    }

    public void setData(final Employee mMaid){
        //download image and update the image
        name.setText(mMaid.getName()+" "+ mMaid.getSurname());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmployeeClickListener.onMaidClick(mMaid);
            }
        });

        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.mipmap.ic_launcher);
        Glide.with(v).load(mMaid.getProfilePic() != null ? mMaid.getProfilePic().getPath() : "").into(image);
    }

    public void setOnEmployeeClickListener(OnEmployeeClickListener onEmployeeClickListener) {
        this.onEmployeeClickListener = onEmployeeClickListener;
    }
}
