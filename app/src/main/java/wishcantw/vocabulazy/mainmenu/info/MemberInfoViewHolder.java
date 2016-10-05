package wishcantw.vocabulazy.mainmenu.info;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

/**
 * Created by allencheng07 on 2016/9/29.
 */

public class MemberInfoViewHolder extends RecyclerView.ViewHolder {

    private TextView memberInfo;
    private View divider;

    public MemberInfoViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        memberInfo = (TextView) view.findViewById(R.id.member_info);
        divider = view.findViewById(R.id.divider);
    }

    public void setProfileDrawable(int drawable) {
        memberInfo.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
    }

    public void setIntroduction(String string) {
        memberInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        memberInfo.setText(string);
    }

    public void setDivider(boolean isLastItem) {

        // if it is the last item, set the divider height to 0.
        if (isLastItem) {
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        }
    }
}
