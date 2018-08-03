package com.amyhuyen.energizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amyhuyen.energizer.models.GlideApp;
import com.amyhuyen.energizer.models.Volunteer;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VolProfileFragment extends ProfileFragment {

    Volunteer volunteer;
    private static final int SELECTED_PIC = 2;
    private StorageReference storageReference;

    public interface SkillFetchListner {
        void onSkillsFetched(List<String> skills);
    }

    public interface CauseFetchListener {
        void onCausesFetched(List<String> causes);
    }

    // the views
    @BindView(R.id.tv_skills) TextView tv_skills;
    @BindView(R.id.tv_cause_area) TextView tv_cause_area;
    @BindView (R.id.profile_pic) ImageView profilePic;
    @BindView(R.id.btn_edit_causes) Button btn_edit_causes;
    @BindView(R.id.tv_contact_info) TextView tv_contact_info;

    // menu views
    @BindView(R.id.tvLeftNumber) TextView tvLeftNumber;
    @BindView(R.id.tvLeftDescription) TextView tvLeftDescription;
    @BindView(R.id.tvMiddleNumber) TextView tvMiddleNumber;
    @BindView(R.id.tvMiddleDescription) TextView tvMiddleDescription;
    @BindView(R.id.tvRightNumber) TextView tvRightNumber;
    @BindView(R.id.tvRightDescription) TextView tvRightDescription;



    public VolProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volunteer = new Volunteer();
        storageReference = FirebaseStorage.getInstance().getReference();
        ButterKnife.bind(this, view);

        drawContactInfo();
        drawCauseAreas();
        drawSkills();
        drawMenu();
        storageReference.child("profilePictures/users/" + UserDataProvider.getInstance().getCurrentUserId() + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = new String(uri.toString());
                GlideApp.with(getContext())
                        .load(downloadUrl)
                        .transform(new CircleCrop())
                        .into(profilePic);
            }
        });

        volunteer = UserDataProvider.getInstance().getCurrentVolunteer();
    }


    @Override
    public void drawCauseAreas() {
        volunteer.fetchCauses(new CauseFetchListener(){
            @Override
            public void onCausesFetched(List<String> causes){
                String causeString = causes.toString().replace("[", "").replace("]", "");
                tv_cause_area.setText("My causes: " + causeString);

                // set the text in the menu for number of causes
                tvRightNumber.setText(Integer.toString(causes.size()));
                if (causes.size() == 1) {
                    tvRightDescription.setText("Cause");
                }
            }
        });

    }

    @Override
    public void drawSkills() {

        volunteer.fetchSkills(new SkillFetchListner() {
            @Override
            public void onSkillsFetched(List<String> skills) {
                //TODO - why does it go through this 2x - and the last time it gives me an empty string? drawCauses not getting called, and skillString has causes
                String skillString = skills.toString().replace("[", "").replace("]", "");
                tv_skills.setText("My skills: " + skillString);

                // set the text in the menu for number of skills
                tvMiddleNumber.setText(Integer.toString(skills.size()));
                if (skills.size() == 1){
                    tvMiddleDescription.setText("Skill");
                }

            }
        });
    }

    @Override
    public void drawEditCausesBtn() {
    }

    @Override
    public void drawContactInfo() {
        tv_contact_info.setText(UserDataProvider.getInstance().getCurrentVolunteer().getAddress());
    }

    @OnClick(R.id.profile_pic)
    public void onProfileImageClick(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        super.startActivityForResult(intent, SELECTED_PIC);
    }

    @OnClick(R.id.btn_edit_causes)
    public void onEditCausesClick() {
        Intent intent = new Intent(getActivity(), SetCausesActivity.class);
        startActivity(intent);
    }

    @Override
    public void drawMenu() {
        // set the text for the descriptions
        tvLeftDescription.setText("Commits");
        tvMiddleDescription.setText("Skills");
        tvRightDescription.setText("Causes");

        // set the text for the number of commits
        int numCommits = ((VolCommitFragment) ((LandingActivity) getActivity()).commitFrag).getCommitCount();
        tvLeftNumber.setText(Integer.toString(numCommits));
        if (numCommits == 1){
            tvLeftDescription.setText("Commit");
        }
    }

    @Override
    public void switchToCommitFragment(){
        LandingActivity landing = (LandingActivity) getActivity();
        landing.bottomNavigationView.setSelectedItemId(R.id.ic_middle);
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        Fragment volCommitFragment = landing.commitFrag;
        fragmentTransaction.replace(R.id.flContainer, volCommitFragment);
        fragmentTransaction.commit();
    }

    ///////getting banner

//    @Override
//    public void getCauseIds() {
//        databaseReference.child(DBKeys.KEY_CAUSES_PER_USER).child(volunteer.getUserID()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                getACause(volunteer.getCauseIds(dataSnapshot));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("Volunteer", "unable to load causePushID datasnapshot");
//            }
//        });
//    }

//    @Override
    public String getACause(List<String> causeIds) {
        return causeIds.get(0);
    }

}
