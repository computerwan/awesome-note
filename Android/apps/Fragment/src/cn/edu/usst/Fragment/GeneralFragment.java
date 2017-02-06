package cn.edu.usst.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

/**
 * Created by Wan on 2016/3/1 0001.
 */
public class GeneralFragment extends Fragment{
    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generalfragment,null);
        btn = (Button) view.findViewById(R.id.search);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                EditText ed_search = (EditText) getActivity().findViewById(R.id.ed_search);
                String search_values = ed_search.getText().toString().trim();

                makeText(getActivity(),"需要查找的内容是："+search_values, LENGTH_SHORT).show();
				
            }
        });

        return view;
    }

}
