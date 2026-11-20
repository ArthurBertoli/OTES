package com.example.projetootes;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificacoesActivity extends AppCompatActivity {
    private static final String PREFS = "notificacoes_financeiro";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String UNIQUE_WORK_NAME = "lembrete_financeiro";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 501;

    private MaterialSwitch switchAtivar;
    private Button buttonSelecionarHora;
    private TextView textProximaNotificacao;
    private SharedPreferences preferences;

    private int selectedHour = 9;
    private int selectedMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notificacoes);

        preferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        bindViews();
        loadSettings();
        setupActions();
    }

    private void bindViews() {
        switchAtivar = findViewById(R.id.switchAtivar);
        buttonSelecionarHora = findViewById(R.id.buttonSelecionarHora);
        textProximaNotificacao = findViewById(R.id.textProximaNotificacao);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void loadSettings() {
        boolean enabled = preferences.getBoolean(KEY_ENABLED, false);
        selectedHour = preferences.getInt(KEY_HOUR, 9);
        selectedMinute = preferences.getInt(KEY_MINUTE, 0);

        switchAtivar.setChecked(enabled);
        updateTimeButtonLabel();
    }

    private void setupActions() {
        buttonSelecionarHora.setOnClickListener(v -> abrirSeletorHora());
        findViewById(R.id.buttonSalvarNotificacoes).setOnClickListener(v -> salvarConfiguracao());
    }

    private void abrirSeletorHora() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;
            updateTimeButtonLabel();
        }, selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    private void updateTimeButtonLabel() {
        String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
        buttonSelecionarHora.setText(time);
        textProximaNotificacao.setText("A próxima notificação será às " + time);
    }

    private void salvarConfiguracao() {
        boolean enabled = switchAtivar.isChecked();

        preferences.edit()
                .putBoolean(KEY_ENABLED, enabled)
                .putInt(KEY_HOUR, selectedHour)
                .putInt(KEY_MINUTE, selectedMinute)
                .apply();

        if (enabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                scheduleReminder();
                Toast.makeText(this, "Configuração salva com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } else {
            cancelReminder();
            Toast.makeText(this, "Lembretes desativados", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder() {
        // Para fins de simplicidade neste projeto OTES, manteremos o PeriodicWorkRequest 
        // No mundo real, usaríamos AlarmManager para horários exatos diários.
        // Aqui, simulamos o intervalo de 24 horas.
        
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(calculateDelay(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, workRequest);
    }

    private long calculateDelay() {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, selectedHour);
        target.set(Calendar.MINUTE, selectedMinute);
        target.set(Calendar.SECOND, 0);

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1);
        }

        return target.getTimeInMillis() - now.getTimeInMillis();
    }

    private void cancelReminder() {
        WorkManager.getInstance(this).cancelUniqueWork(UNIQUE_WORK_NAME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleReminder();
                Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sem permissão para notificações", Toast.LENGTH_SHORT).show();
            }
        }
    }
}