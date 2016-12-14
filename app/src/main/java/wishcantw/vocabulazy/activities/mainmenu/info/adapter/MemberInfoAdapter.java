package wishcantw.vocabulazy.activities.mainmenu.info.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.mainmenu.info.view.MemberInfoViewHolder;

/**
 * Created by allencheng07 on 2016/9/29.
 */

public class MemberInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int[] pictures;
    private String[] introductions;

    public MemberInfoAdapter(int[] pictures, String[] introductions) {
        // send data to here
        this.pictures = pictures;
        this.introductions = introductions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create views
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_info, parent, false);
        return new MemberInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // get data and bind to views
        if (holder instanceof MemberInfoViewHolder) {
            ((MemberInfoViewHolder) holder).setIntroduction(introductions[position]);
            ((MemberInfoViewHolder) holder).setProfileDrawable(pictures[position]);
            ((MemberInfoViewHolder) holder).setDivider(isTheLastItem(position));
        }
    }

    @Override
    public int getItemCount() {
        return pictures.length;
    }

    // check if the position is the last one
    private boolean isTheLastItem(int position) {
        return (position == getItemCount()-1);
    }
}
