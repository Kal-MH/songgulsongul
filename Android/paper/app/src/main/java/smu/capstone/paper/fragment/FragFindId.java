package smu.capstone.paper.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import smu.capstone.paper.activity.FindAccountActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeMarketAdapter;
import smu.capstone.paper.item.HomeMarketItem;

public class FragFindId extends Fragment {
    private View view;
    public static EditText find_id_email;

    public FragFindId() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_find_id, container, false);

        find_id_email = (EditText)view.findViewById(R.id.find_id_email);

        return view;
    }

    public String getEmail(){
        String email = find_id_email.getText().toString();

        return email;
    }

}
