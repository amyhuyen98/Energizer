

package com.amyhuyen.energizer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amyhuyen.energizer.models.User;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO change all findViewByIds and OnClick listeners to butterknife style and call finish() as marked

public abstract class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    User user;

    public ProfileFragment() {
    }// Required empty public constructor

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //views
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.btn_logout)
    ImageButton btn_logout;
    @BindView(R.id.tv_email) TextView tv_email;

    private OnFragmentInteractionListener mListener;
    FragmentActivity listener;


    //fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LandingActivity) {
            this.listener = (LandingActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //will I have access to this in the subclasses of profile fragment?
//        user = getActivity().getIntent().getParcelableExtra("UserObject"); //tried moving this to VOlProfileFrag subclass
        //TODO - START HERE - getting null user object reference here
        firebaseAuth = FirebaseAuth.getInstance();

        // bind the views
        ButterKnife.bind(getActivity());

        //set textview text
//        tv_name.setText(user.getName());
//        tv_email.setText(user.getEmail());

    }

    //abstract methods to be implemented by subclasses VolProfileFragment or NPOPorfileFragment

    public abstract void drawSkills();

    public abstract void drawCauseAreas();

    //equivalent to btnLogout.setOnClickListener with butterknife (may need to go outside of onViewCreated)
    @OnClick(R.id.btn_logout)
    public void onLogoutClick() {
        // log user out
        firebaseAuth.signOut();

        // log the sign out
        if (firebaseAuth.getCurrentUser() == null) {
            Log.d("Logging Out", "User has successfully logged out");
            Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
        }

        // intent to login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        listener.finish(); //TODO - why couldn't I call finish here?
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}


    //        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//
//                // log the sign out
//                if (firebaseAuth.getCurrentUser() == null) {
//                    Log.d("Logging Out", "User has successfully logged out");
//                    Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
//                }
//
//                //Intent
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//                //finish(); TODO - why can't I call finish here?
//            }
//        });


