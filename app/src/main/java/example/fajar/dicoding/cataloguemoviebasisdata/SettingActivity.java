package example.fajar.dicoding.cataloguemoviebasisdata;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.fajar.dicoding.cataloguemoviebasisdata.service.DailyReminderMovie;
import example.fajar.dicoding.cataloguemoviebasisdata.service.UpComingTask;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.daily_notif) LinearLayout dailyNotif;
    @BindView(R.id.upcoming_notif) LinearLayout upcomingNotif;
    @BindView(R.id.setting_local) LinearLayout settingLocal;
    @BindView(R.id.switch_daily) Switch dailySwitch;
    @BindView(R.id.switch_upcoming) Switch upcomingSwitch;

    private UpComingTask mUpComingTask;
    private DailyReminderMovie dailyReminderMovie;
    private boolean isUpcoming, isDaily;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dailyReminderMovie = new DailyReminderMovie();

        appPreference = new AppPreference(this);

        setEnableDisableNotif();
    }

    void setEnableDisableNotif(){
        if (appPreference.isDaily()){
            dailySwitch.setChecked(true);
        }else {
            dailySwitch.setChecked(false);
        }

        if (appPreference.isUpcoming()){
            upcomingSwitch.setChecked(true);
        }else {
            upcomingSwitch.setChecked(false);
        }
    }

    @OnClick({R.id.switch_daily,R.id.switch_upcoming,R.id.setting_local})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.switch_daily:
                isDaily = dailySwitch.isChecked();
                if (isDaily){
                    dailySwitch.setEnabled(true);
                    appPreference.setDaily(isDaily);
                    // Mengeset waktu dan message Daily Reminder Secara Manual dulu :D
                    dailyReminderMovie.setDailyReminderMovieAlarm(this, DailyReminderMovie.TYPE_DAILY_REMINDER,
                            "10:43", getString(R.string.message_notif_daily));
                }else {
                    dailySwitch.setChecked(false);
                    appPreference.setDaily(isDaily);
                    dailyReminderMovie.cancelDailyReminderAlarm(this,DailyReminderMovie.TYPE_DAILY_REMINDER);
                }
                break;
            case R.id.switch_upcoming:
                mUpComingTask = new UpComingTask(this);
                isUpcoming = upcomingSwitch.isChecked();
                if (isUpcoming){
                    upcomingSwitch.setEnabled(true);
                    appPreference.setUpcoming(isUpcoming);
                    // Mengeset Upcoming Movie / NowPlaying Movie (dengan tanggal 2017-09-14)
                    mUpComingTask.createPeriodicTask();
                    Toast.makeText(this, R.string.message_setup_upcoming, Toast.LENGTH_SHORT).show();
                }else {
                    upcomingSwitch.setChecked(false);
                    appPreference.setUpcoming(isUpcoming);
                    mUpComingTask.cancelPeriodicTask();
                    Toast.makeText(this, R.string.message_cancel_upcoming, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting_local:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
        }
    }

}
