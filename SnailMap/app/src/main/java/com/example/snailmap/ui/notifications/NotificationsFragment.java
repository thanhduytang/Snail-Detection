package com.example.snailmap.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.snailmap.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    // myList to save data get from firebase
    private List<Object> myList = new ArrayList<>();
    //define ListView
    private ListView listView;
    //get FirebaseFirestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //on create view for notification fragment
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //find list view by id
        listView = (ListView) root.findViewById(R.id.list_item);

        //get data from firebase
        db.collection("snail-detection").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                myList.clear();
                //get data and format
                for(DocumentSnapshot snapshot : value){

                    if(snapshot.getBoolean("HaveSnail")){
                        //get data and convert to string
                        String data = snapshot.getDate("DateTime").toString();
                        //item for listview
                        String myList_each = "DateTime: " + data.substring(data.length()-4) +", "+ data.substring(3, data.length()-15)
                                + "\nNumberofSnails: "+snapshot.get("NumberofSnails")
                                + "\nCoordinate: "+snapshot.getDouble("longitude") +", "+ snapshot.getDouble("latitude");
                        myList.add(myList_each);

                    }
                }
                //set the listview by the data get from cloud database
                if(getContext() != null){
                    listView.setAdapter(new ArrayAdapter<Object>(getContext(),
                            android.R.layout.simple_list_item_1 , removeNull(myList)));

                }

            }

        });


        return root;
    }

    //function for remove Null values
    public static <T> List<T> removeNull(List<? extends T> oldList) {
        List<T> listTemp = new ArrayList();
        for (int i = 0;i < oldList.size(); i++) {
            if (oldList.get(i) != null) {
                listTemp.add(oldList.get(i));
            }
        }
        return listTemp;
    }
}