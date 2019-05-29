package org.smartregister.chw.anc_sample.activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.smartregister.chw.anc.activity.BaseAncHomeVisitActivity;
import org.smartregister.chw.anc.activity.AncMemberProfileActivity;
import org.smartregister.chw.anc.activity.BaseAncRegisterActivity;
import org.smartregister.chw.anc_sample.R;

public class AncRegisterActivity extends BaseAncRegisterActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile_page:
                openProfilePage();
                return true;
            case R.id.visit_page:
                openVisitPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openVisitPage() {
        BaseAncHomeVisitActivity.startMe(this, "1233435");
    }

    private void openProfilePage() {
        AncMemberProfileActivity.startMe(this, "1233435");
    }
}
