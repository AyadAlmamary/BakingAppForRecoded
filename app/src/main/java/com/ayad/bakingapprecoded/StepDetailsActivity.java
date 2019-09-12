package com.ayad.bakingapprecoded;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.ayad.bakingapprecoded.fragments.StepDetailsFragment;
import com.ayad.bakingapprecoded.models.Step;

public class StepDetailsActivity extends AppCompatActivity {

    private Step mStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intent = getIntent();
        mStep = (Step) intent.getSerializableExtra("step");

        // Check so it doesn't recreate the fragment on orientation change
        if (savedInstanceState == null) {
            StepDetailsFragment fragment = new StepDetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putString("video_url", mStep.getVideoURL());
            bundle.putString("thumbnail_url", mStep.getThumbnailURL());
            bundle.putString("description", mStep.getDescription());
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_container, fragment)
                    .commit();
        }
    }
}
