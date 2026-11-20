package com.example.projetootes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {
	private static final String CHANNEL_ID = "financeiro_reminders";
	private static final int NOTIFICATION_ID = 1001;
	private static final String PREFS = "notificacoes_financeiro";
	private static final String KEY_ENABLED = "enabled";

	public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	public Result doWork() {
		SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		if (!preferences.getBoolean(KEY_ENABLED, false)) {
			return Result.success();
		}

		createChannel();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
				.setSmallIcon(android.R.drawable.ic_dialog_info)
				.setContentTitle("Lembrete financeiro")
				.setContentText("Abra o app e revise suas transações de hoje.")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setAutoCancel(true);

		NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID, builder.build());
		return Result.success();
	}

	private void createChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Lembretes financeiros", NotificationManager.IMPORTANCE_DEFAULT);
			NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
			if (manager != null) {
				manager.createNotificationChannel(channel);
			}
		}
	}
}