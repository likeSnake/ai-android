package com.skythinker.gptassistant.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.view.Window;

import com.skythinker.gptassistant.R;


public class NoticeUtil {
	public static void showAlertDialogWithYesOrNo(Context ctx, String msg, String title, String yes, String no, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onYes();
			}
		});
		builder.setNegativeButton(no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onNo();
			}
		});
		builder.create().show();
	}

	public static void showAlertDialogWithChoose(Context ctx, String msg, String title, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ctx.getString(R.string.determine), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onYes();
			}
		});
		builder.setNegativeButton(ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onNo();
			}
		});
		builder.create().show();
	}

	public static void showDialogChooseCountdown(Context ctx, String msg, String title, int countDownTime, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);

		builder.setNegativeButton(ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onNo();
			}
		});

		builder.setPositiveButton(ctx.getString(R.string.determine) + " (" + countDownTime + ")", null); // 先禁用按钮

		final AlertDialog dialog = builder.create();
		dialog.show();

		// 设置弹窗圆角
		Window window = dialog.getWindow();
		if (window != null) {
			GradientDrawable drawable = new GradientDrawable();
			drawable.setCornerRadius(5 * ctx.getResources().getDisplayMetrics().density);
			drawable.setColor(ctx.getResources().getColor(android.R.color.white));
			window.setBackgroundDrawable(drawable);
		}

		final int interval = 1000; // 1秒
		final CountDownTimer timer = new CountDownTimer(countDownTime * 1000L, interval) {
			int remainingTime = countDownTime;

			@Override
			public void onTick(long millisUntilFinished) {
				remainingTime--;
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ctx.getString(R.string.determine) + " (" + remainingTime + ")");
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
			}

			@Override
			public void onFinish() {
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(ctx.getString(R.string.determine));
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
					dialog.dismiss();
					cb.onYes();
				});
			}
		};

		timer.start();

		dialog.setOnDismissListener(dialogInterface -> timer.cancel());
	}

	public static void showAlertDialogJustInfo(Context ctx, String msg, String title) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ctx.getString(R.string.determine), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static void showAlertDialogJustInfoTwo(Context ctx, String msg, String title, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ctx.getString(R.string.determine), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onYes();
			}
		});
		builder.create().show();
	}

	public static void showAlertDialogCheckDesign(Context ctx, String msg, String title, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ctx.getString(R.string.create_yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onYes();
			}
		});
		builder.setNegativeButton(ctx.getString(R.string.create_no), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onNo();
			}
		});
		builder.create().show();
	}

	public static void showAlertDialogLeftError(Context ctx, String msg, String title, final NoticeCallback cb) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ctx.getString(R.string.determine), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cb.onYes();
			}
		});
		builder.create().show();
	}

	public interface NoticeCallback {
		void onYes();

		void onNo();
	}
}
