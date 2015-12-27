package it.jaschke.alexandria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.jaschke.alexandria.R;


public class About extends Fragment
{
    public About()
    {

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activity.setTitle(R.string.about);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        return rootView;
    }

}
