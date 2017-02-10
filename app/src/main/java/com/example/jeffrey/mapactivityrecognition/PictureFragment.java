package com.example.jeffrey.mapactivityrecognition;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffrey.mapactivityrecognition.R;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class PictureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_picture,
                container, false);
        return view;
    }
}
