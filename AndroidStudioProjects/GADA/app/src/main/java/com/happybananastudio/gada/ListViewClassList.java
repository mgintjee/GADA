package com.happybananastudio.gada;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by mgint on 7/11/2018.
 */

public class ListViewClassList extends ArrayAdapter<ClassUser> {

    private Context ThisContext;
    private ArrayList<ClassUser> ClassList;
    private String ActiveUserID, ClassCode;

    ListViewClassList(Context Context, int Resource, ArrayList<ClassUser> NewClassList, String Code, String UserID) {
        super(Context, Resource, NewClassList);
        ThisContext = Context;
        ClassList = NewClassList;
        ActiveUserID = UserID;
        ClassCode = Code;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder ViewHolder;
        String UserHandle, UserID;
        View Result;
        ClassUser LocalUserInfo = ClassList.get(position);

        if (convertView == null) {
            ViewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_row_user_info, parent, false);

            ViewHolder.TV_UserHandle = convertView.findViewById(R.id.UserInfoRowTV_UserHandle);
            ViewHolder.B_Profile = convertView.findViewById(R.id.UserInfoRowB_UserProfile);

            Result = convertView;

            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
            Result = convertView;
        }

        UserHandle = LocalUserInfo.UserHandle;
        UserID = LocalUserInfo.UserID;
        ViewHolder.TV_UserHandle.setText(UserHandle);
        SetButtonListenerProfile(ViewHolder.B_Profile, UserID);

        return Result;
    }

    private void SetButtonListenerProfile(Button B, final String UserID) {
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ThisContext, ActivityUserProfile.class);
                intent.putExtra("ClassCode", ClassCode);
                intent.putExtra("ActiveUserID", ActiveUserID);
                intent.putExtra("UserID", UserID);
                ThisContext.startActivity(intent);
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return ClassList.size();
    }

    @Override
    public ClassUser getItem(int position) {
        return ClassList.get(position);
    }

    private class ViewHolder {
        private TextView TV_UserHandle, TV_UserTeam, TV_UserType;
        private Button B_Profile;
    }

}
