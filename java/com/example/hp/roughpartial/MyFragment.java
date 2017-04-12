package com.example.hp.roughpartial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyFragment extends Fragment{

    int mCurrentPage;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mCurrentPage==1) {
            v = inflater.inflate(R.layout.activity_quick_guide, container, false);
        }
        else if(mCurrentPage==2){
            v = inflater.inflate(R.layout.guide2, container, false);
        }
        else
        {
            v = inflater.inflate(R.layout.guide3, container, false);
        }
        return v;
    }

}