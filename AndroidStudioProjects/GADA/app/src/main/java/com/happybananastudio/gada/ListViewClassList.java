package com.happybananastudio.gada;

import android.content.Context;
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

public class ListViewClassList extends ArrayAdapter<ClassUserInfo> {

    private Context ThisContext;
    private ArrayList<ClassUserInfo> ClassList;
    private String ClassCode, UserHandle;

    ListViewClassList(Context Context, int Resource, ArrayList<ClassUserInfo> NewClassList) {
        super(Context, Resource, NewClassList);
        ThisContext = Context;
        ClassList = NewClassList;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder ViewHolder;
        String UserHandleToView, UserName, UserType;
        View Result;
        ClassUserInfo LocalUserInfo = ClassList.get(position);

        if (convertView == null) {
            ViewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_row_user_info, parent, false);

            ViewHolder.TV_UserHandle = convertView.findViewById(R.id.UserInfoRowTV_UserHandle);
            ViewHolder.TV_UserName = convertView.findViewById(R.id.UserInfoRowTV_UserName);
            ViewHolder.TV_UserType = convertView.findViewById(R.id.UserInfoRowTV_UserType);

            Result = convertView;

            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
            Result = convertView;
        }

        UserHandleToView = LocalUserInfo.GetHandle();
        UserName = LocalUserInfo.GetName();
        UserType = LocalUserInfo.GetType();

        ViewHolder.TV_UserHandle.setText(UserHandleToView);
        ViewHolder.TV_UserName.setText(UserName);
        ViewHolder.TV_UserType.setText(UserType);

        return Result;
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
    public ClassUserInfo getItem(int position) {
        return ClassList.get(position);
    }

    private class ViewHolder {
        private TextView TV_UserHandle, TV_UserName, TV_UserType;
        private Button B_Profile, B_Speech;
    }

    public void SetClassCode(String C) {
        ClassCode = C;
    }

    public void SetUserHandle(String U) {
        UserHandle = U;
    }

}
