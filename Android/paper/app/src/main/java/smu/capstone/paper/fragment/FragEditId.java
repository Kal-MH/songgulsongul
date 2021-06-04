package smu.capstone.paper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import smu.capstone.paper.R;

public class FragEditId extends Fragment {
    public FragEditId() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.frag_edit_id, container, false);

        Button account_id_check= (Button) rootView.findViewById(R.id.account_id_check);

        return rootView;

    }
}
