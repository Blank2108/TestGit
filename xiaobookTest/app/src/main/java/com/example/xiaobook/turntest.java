package com.example.xiaobook;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class turntest extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private PageWidget mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	private String datapath;
	private int mWidth;
	private int mHeight;
	private PopupWindow popupWindow = null;
	private TextView mTextView;
	private TimeChangedReceiver timeChange;
	static String CONFIG = "config";
	static String LIGHT = "light";
	static String BACKCOLOR = "backcolor";
	static String TXTCOLOR = "txtcolor";
	static String USEBG = "useBg";
	static String FONT_SIZE = "font_size";
	static String LINE_JIANJU = "line_jianju";
	static String LIGHT_USERSYSTEM = "light_userSys";
	static public String dialog_string = "The current version\\uFF1AV01.0\\nAuthor : Blank Ting\\nE-mail : dwenwen21@gmail.com\"";
	static String JINDU = "jindu"; // �����˳�λ��
	private MarkHelper markhelper;
	private String bookPath;// ��¼�������·��
	private MarkDialog mDialog = null;
	private String bookname = "";
	private int bookid = 0;

	// public static native parse()
	private static class TimeChangedReceiver extends BroadcastReceiver {
		/**
		 * PageWidget mPageWidget
		 */
		private PageWidget mPageWidget;

		public TimeChangedReceiver(PageWidget mPageWidget) {
			// TODO Auto-generated constructor stub
			this.mPageWidget = mPageWidget;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// Post a runnable to avoid blocking the broadcast.
			// turntest.this.getMainLooper().getThread().
			mPageWidget.bIsTimeRefresh = true;
			mPageWidget.settime();
			Log.i("lxm", "sss");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mWidth = GetW(this);
		mHeight = GetH(this);
		markhelper = new MarkHelper(this);
		bookid = (int) getIntent().getExtras().getInt("bookname");
		bookname = BookShelfActivity.mTitles.get(bookid)+".txt";
		Log.i("lxmlmx", bookname);
		// mPageWidget = //new PageWidget(this, mWidth, mHeight);
		setContentView(R.layout.pagewidget_main);
		mPageWidget = (PageWidget) findViewById(R.id.PageWidget);
		// Log.i("lxm++",mPageWidget.getHeight()+"--"+mPageWidget.getWidth());
		timeChange = new TimeChangedReceiver(mPageWidget);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		this.registerReceiver(timeChange, filter);
		datapath = "/data/data/" + this.getPackageName().toString()
				+ "/databases";
		mCurPageBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(this, mWidth, mHeight, getIntPeizhi(
				BACKCOLOR, Color.WHITE), getIntPeizhi(TXTCOLOR, Color.BLACK),
				getIntPeizhi(LINE_JIANJU, 5), getIntPeizhi(FONT_SIZE, 29),
				getIntPeizhi(JINDU + bookname, 0));
		mPageWidget.setcolor(getIntPeizhi(TXTCOLOR, Color.BLACK));
		if (getBoolPeizhi(USEBG, false)) {
			pagefactory.setUseBg(true);
			pagefactory.setBgBitmap(BitmapFactory.decodeResource(
					this.getResources(), getIntPeizhi(BACKCOLOR, 0)));
		}
		setbacklight();
		try {
			CopyRawtodata.CopyRawtodata(datapath, bookname,
					getApplicationContext(), BookShelfActivity.bookslistid.get(bookid),
					true);
			// ������Ҫ�������
			pagefactory.openbook(datapath + "/" + bookname);
			// ������Ҫ���û�����ɫ �Լ�������ɫ
			pagefactory.Draw(mCurPageCanvas, mPageWidget);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

			show_toast("system error,I am Sorry!");
			// Toast.makeText(this, "ϵͳ�����쳣,I am Sorry!", Toast.LENGTH_SHORT)
			// .show();
		}
		// ������,Ȼ��������ʽ
		String aaa = getFileEncode(datapath + "/" + bookname);
		Log.i("aaa", aaa);
		bookPath = datapath + "/" + bookname;
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				// return false;
				boolean ret = false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						// if (!pagefactory.isfirstPage())
						mPageWidget.abortAnimation();
						mPageWidget.bIsShowNoMore = false;
						mPageWidget.calcCornerXY(e.getX(), e.getY());
						Log.i("lxm", "setOnTouchListener");
						pagefactory.Draw(mCurPageCanvas, mPageWidget);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage()) {
								mPageWidget.bIsShowNoMore = true;
								mPageWidget.invalidate();
								return true;
							}
							pagefactory.Draw(mNextPageCanvas, mPageWidget);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.islastPage()) {
								mPageWidget.bIsShowNoMore = true;
								mPageWidget.invalidate();
								return true;
							}
							// return true;
							pagefactory.Draw(mNextPageCanvas, mPageWidget);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					} else if (e.getAction() == MotionEvent.ACTION_UP
							|| e.getAction() == MotionEvent.ACTION_MOVE) {
						// if ((mPageWidget.DragToRight()
						// &&pagefactory.isfirstPage())
						// || pagefactory.islastPage()) {
						// {
						// Log.i("lxm", "1"+mPageWidget.DragToRight());
						// Log.i("lxm", "1"+pagefactory.isfirstPage());
						// Log.i("lxm", "1"+pagefactory.islastPage());
						// return true;
						// }
						// }
					}

					return ret;
				}
				return false;
			}

		});
	}

	public void showMenu(View view)
	{
		initPopupWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		// Log.i("lxm++",mPageWidget.getHeight()+"--"+mPageWidget.getWidth());
		mWidth = mPageWidget.getWidth();
		mHeight = mPageWidget.getHeight();
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onBackPressed() {
		if (popupWindow != null) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}

	}

	private void MyDialog() {
		LayoutInflater flater = LayoutInflater.from(this);

		View view = flater.inflate(R.layout.my_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(turntest.this);
		// builder.setCancelable(true);
		builder.setView(view);

		final AlertDialog alert = builder.create();
		// alert.dispatchKeyEvent(event)
		alert.show();
		// alert.
		Button btnSearch = (Button) view.findViewById(R.id.about);
		// ((TextView) view.findViewById(R.id.message)).setText(Html
		// .fromHtml(dialog_string));
		((TextView) view.findViewById(R.id.message)).setText(dialog_string);
		btnSearch.setOnClickListener(new OnClickListener()

		{

			public void onClick(View v)

			{
				alert.dismiss();

			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN: {
			if (keyCode == KeyEvent.KEYCODE_MENU) {

				initPopupWindow();
			}
		}
			break;
		}
		return super.onKeyDown(keyCode, event);

	};

	/**
	 * ����PopupWindow
	 */
	public void initPopupWindow() {
		// ����PopupWindow�Ĳ����ļ�
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.popup, null);
		// ����PopupWindow��ý�鲼���ļ�
		View parentView = layoutInflater.inflate(R.layout.first, null);
		// ʵ����PopupWindow�е����
		// TextView titleTextView = (TextView)
		// view.findViewById(R.id.popup_title);
		// ImageView editImg = (ImageView) view.findViewById(R.id.popup_edit);
		// ImageView listenImg = (ImageView)
		// view.findViewById(R.id.popup_listen);

		// SeekBar seekBar = (SeekBar) view.findViewById(R.id.popup_progress);
		// TextView progressTextView = (TextView) view
		// .findViewById(R.id.popup_progress_tv);
		// view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});

		TextView bookmark = (TextView) view.findViewById(R.id.bookmark);
		bookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				SQLiteDatabase db = markhelper.getWritableDatabase();
				try {
					String col[] = { "path", "begin", "word", "time" };
					Cursor cur = db.query("markhelper", col,
							"path=? AND begin=? ", new String[] { bookPath,
									"" + pagefactory.get_jindu() }, null, null,
							null);
					Integer num = cur.getCount();
					if (num > 0) {
						show_toast("The bookmark already exists!");
						cur.close();
						db.close();

						return;
					}
					SimpleDateFormat sf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm ss");
					String time = sf.format(new Date());
					db.execSQL(
							"insert into markhelper (path ,begin,word,time) values (?,?,?,?)",
							new String[] { bookPath,
									pagefactory.get_jindu() + "",
									pagefactory.get_work(), time });
					db.close();
					show_toast("Bookmark added successfully!");

				} catch (SQLException e) {
					show_toast("The bookmark already exists!");

				} catch (Exception e) {
					show_toast("Add bookmark failure!");
				}
			}
		});
		// black.get
		GridView gridView = (GridView) view.findViewById(R.id.popup_grid);
		GridViewAdapter adapter = new GridViewAdapter(this, mPictures, mTitles,
				R.layout.grid);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new ItemClickListener());
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected
			 * (android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
			 * (android.widget.AdapterView)
			 */
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// ������ʵ����PopupWindow
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// ΪPopupWindow���õ�����λ��
		// popupWindow.showAsDropDown(parentView);

		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		// popupWindow.
		// popupWindow.update();
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);// �����������ʧ,��Ҫ���ñ��������㡢touchable��update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// �����Ϊ�˷�ֹ�����˵���ȡ����֮�󣬵��activity���������û����Ӧ
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	// lxm
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int tag = Integer.parseInt((String) v.getTag());
		Log.i("eee", "tag" + v.getTag());
		if (tag <= 10) {
			pagefactory.setTextColor(color[tag][1]);// ���ñ�ˢ��ɫ
			mPageWidget.setcolor(color[tag][1]);
			pagefactory.setUseBg(false);
			pagefactory.Draw(mCurPageCanvas, color[tag][0], mPageWidget); // ���ñ�����ɫ
			mPageWidget.setcurBitmaps(mCurPageBitmap);

			PutBoolPeizhi(USEBG, false);
			PutIntPeizhi(BACKCOLOR, color[tag][0]);
			PutIntPeizhi(TXTCOLOR, color[tag][1]);
		} else {
			pagefactory.setTextColor(colorbg[tag - 11][1]);// ���ñ�ˢ��ɫ
			mPageWidget.setcolor(colorbg[tag - 11][1]);
			pagefactory.setBgBitmap(BitmapFactory.decodeResource(
					this.getResources(), colorbg[tag - 11][0])); // ���ñ�����ɫ
			pagefactory.setUseBg(true);
			pagefactory.Draw(mCurPageCanvas, 0, mPageWidget); // ���ñ�����ɫ

			mPageWidget.setcurBitmaps(mCurPageBitmap);
			PutIntPeizhi(BACKCOLOR, colorbg[tag - 11][0]);
			PutIntPeizhi(TXTCOLOR, colorbg[tag - 11][1]);
			PutBoolPeizhi(USEBG, true);
		}
	}

	public void setfont(int fontsize) {
		pagefactory.fontsizechange(fontsize);
		pagefactory.Draw(mCurPageCanvas, mPageWidget);
		mPageWidget.setcurBitmaps(mCurPageBitmap);
	}

	public void setline_jianju(int line_jianju) {
		pagefactory.fontline_jianju(line_jianju);
		pagefactory.Draw(mCurPageCanvas, mPageWidget);
		mPageWidget.setcurBitmaps(mCurPageBitmap);
	}

	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();// �ر�
				// getParent().getWindow().c
			}
			// view.setBackgroundResource(R.drawable.pop_pressed);
			Log.i("lxm", "" + position);
			switch (position) {
			case 7:
				initjinduPopupWindow(turntest.this);
				break;
			case 6: // ����
				MyUtil.fenxiang(turntest.this);
				break;
			case 4: // ����

				break;
			case 2: // ���ȵ���
				initlightPopupWindow(turntest.this);
				break;
			case 5: // ����
				MyDialog();
				break;
			case 1: //
				initFontPopupWindow(turntest.this);
				break;
			case 3:
				initReadPopupWindow(turntest.this);
				break;
			case 0:
				show_bookmark();
				break;
			default:
				Toast show = Toast.makeText(turntest.this,
						"Is not currently supported, so stay tuned.",
						Toast.LENGTH_SHORT);
				show.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0); // .show();
				show.show();
				break;
			}
		}
	}

	/**
	 * ͼƬ��Դ
	 */
	private int[] mPictures = { R.drawable.cartoon_extract,
			R.drawable.cartoon_fontsize, R.drawable.cartoon_light,
			R.drawable.cartoon_style, R.drawable.cartoon_internet,
			R.drawable.cartoon_about_us, R.drawable.cartoon_share,
			R.drawable.cartoon_jump, };
	private int[][] color = { { 0xFFFFFFFF, 0xFF000000 },
			{ 0xFFCCE8CF, 0xFF333333 },// ����ɫ��ǰ��ɫ // ����
			{ 0xFF151C1F, 0xFF4D5052 },// ҹ��1
			{ 0xFF000000, 0xFF373737 },// ҹ��2
			{ 0xFF000000, 0xFF5F5F5F },// ҹ��3
			{ 0xFF000000, 0xFF333333 },// ҹ��4
			{ 0xFF000000, 0xFF494949 },// ҹ��5
			{ 0xFF000000, 0xFF222222 },// ҹ��6
			{ 0xFF001622, 0xFF204353 },// ҹ��7
			{ 0xFF171F27, 0xFF445053 },// ҹ��8
			{ 0xFF251C05, 0xFF574F3C },// ҹ��9

	};

	private int[][] colorbg = { { R.raw.bg1, 0xFF95938F },// Bg 1
			{ R.raw.bg2, 0xFF3A342B },// Bg 2
			{ R.raw.bg3, 0xFF637079 },// Bg 3
			{ R.raw.bg5, 0xFF3A342B },// Bg 5
	// #FF3A342B
	};
	/**
	 * ������Դ
	 */
	private String[] mTitles = { "Bookmarks", "Font Size", "Brightness",
			"Read Style", "Recreation", "About", "Share", "Progress Rate" };

	public int GetW(Activity context) {
		// int screenWidth ;
		DisplayMetrics dm; // = new DisplayMetrics();
		// dm = context.getResources().getDisplayMetrics();

		dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		// dm.widthPixels;
		return dm.widthPixels;
	}

	public int GetH(Activity context) {
		// int screenWidth ;
		DisplayMetrics dm; // = new DisplayMetrics();
		// dm = context.getResources().getDisplayMetrics();

		dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		// dm.widthPixels;
		return dm.heightPixels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (timeChange != null) {
			this.unregisterReceiver(timeChange);
			timeChange = null;
		}
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (timeChange != null) {
			this.unregisterReceiver(timeChange);
			timeChange = null;
		}
		PutIntPeizhi(JINDU + bookname, pagefactory.get_jindu());
		super.onPause();
	}

	protected void onSaveInstanceState(Bundle outState) {

		PutIntPeizhi(JINDU + bookname, pagefactory.get_jindu());
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		this.registerReceiver(timeChange, filter);

		super.onResume();
	}

	public void setbacklight() {
		SharedPreferences Preferences = turntest.this.getSharedPreferences(
				CONFIG, Context.MODE_PRIVATE);
		if (Preferences.getBoolean(LIGHT_USERSYSTEM, false)) {
			// �����ϵͳ�����ˣ�������
			return;
		}
		WindowManager.LayoutParams wl = turntest.this.getWindow()
				.getAttributes();
		// SharedPreferences Preferences = turntest.this.getSharedPreferences(
		// CONFIG, Context.MODE_PRIVATE);
		float tmpFloat = (float) Preferences.getFloat(LIGHT, -1);
		Log.i("lxm", "22222��" + tmpFloat + "%");
		if (tmpFloat > 0 && tmpFloat <= 1) {
			wl.screenBrightness = tmpFloat;
		}
		turntest.this.getWindow().setAttributes(wl);
	}

	public void initlightPopupWindow(Context context) {
		// ����PopupWindow�Ĳ����ļ�
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.popup_light, null);
		// ����PopupWindow��ý�鲼���ļ�
		View parentView = layoutInflater.inflate(R.layout.main, null);

		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);// �����������ʧ,��Ҫ���ñ��������㡢touchable��update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// �����Ϊ�˷�ֹ�����˵���ȡ����֮�󣬵��activity���������û����Ӧ
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});
		final SeekBar adjest = (SeekBar) view
				.findViewById(R.id.read_bright_adjust_group_id);

		SharedPreferences Preferences = turntest.this.getSharedPreferences(
				CONFIG, Context.MODE_PRIVATE);
		Float light = Preferences.getFloat(LIGHT, -1);
		adjest.setProgress((int) (light * 100 - 10));// light.

		final CheckBox systemLight = (CheckBox) view
				.findViewById(R.id.checkBox);
		systemLight.setChecked(Preferences.getBoolean(LIGHT_USERSYSTEM, false));

		adjest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * �϶���ֹͣ�϶���ʱ�����
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// description.setText("�϶�ֹͣ");
			}

			/**
			 * �϶�����ʼ�϶���ʱ�����
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// description.setText("��ʼ�϶�");
			}

			/**
			 * �϶������ȸı��ʱ�����
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				// Log.i("lxm", "��ǰ���ȣ�" + progress + "%");
				// Settings.System.putInt(turntest.this.getContentResolver(),
				// Settings.System.SCREEN_BRIGHTNESS, progress + 10);
				// int tmpInt = Settings.System.getInt(
				// turntest.this.getContentResolver(),
				// Settings.System.SCREEN_BRIGHTNESS, -1);
				WindowManager.LayoutParams wl = turntest.this.getWindow()
						.getAttributes();
				SharedPreferences Preferences = turntest.this
						.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
				Preferences.edit().putBoolean(LIGHT_USERSYSTEM, false).commit();
				float tmpFloat = (float) (progress + 10) / 100;
				Log.i("lxm", "��ǰ���ȣ�" + tmpFloat + "%");
				if (tmpFloat > 0 && tmpFloat <= 1) {
					wl.screenBrightness = tmpFloat;
				}
				turntest.this.getWindow().setAttributes(wl);
				// SharedPreferences Preferences = turntest.this
				// .getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
				Preferences.edit().putFloat(LIGHT, tmpFloat).commit();
				systemLight.setChecked(false);
				Preferences.edit().putBoolean(LIGHT_USERSYSTEM, false).commit();
			}
		});
		// SharedPreferences Preferences = turntest.this.getSharedPreferences(
		// CONFIG, Context.MODE_PRIVATE);
		// Preferences.getBoolean(LIGHT_USERSYSTEM, true);

		systemLight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					SharedPreferences Preferences = turntest.this
							.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
					Preferences.edit().putBoolean(LIGHT_USERSYSTEM, true)
							.commit();
					int tmpInt = Settings.System.getInt(
							turntest.this.getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS, -1);
					WindowManager.LayoutParams wl = turntest.this.getWindow()
							.getAttributes();

					float tmpFloat = (float) tmpInt / 255;
					Log.i("lxm", "��ǰ���ȣ�" + tmpFloat + "%");
					if (tmpFloat > 0 && tmpFloat <= 1) {
						wl.screenBrightness = tmpFloat;
					}
					turntest.this.getWindow().setAttributes(wl);
					// adjest.setClickable(false);
					// Log.i("lxm", "true");
				} else {
					SharedPreferences Preferences = turntest.this
							.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
					Preferences.edit().putBoolean(LIGHT_USERSYSTEM, false)
							.commit();
					// Log.i("lxm", "false");
					float tmpFloat = Preferences.getFloat(LIGHT, -1);
					WindowManager.LayoutParams wl = turntest.this.getWindow()
							.getAttributes();
					if (tmpFloat > 0 && tmpFloat <= 1) {
						wl.screenBrightness = tmpFloat;
					}
					turntest.this.getWindow().setAttributes(wl);
					// adjest.setClickable(true);
				}
			}
		});

	}

	public void initReadPopupWindow(Context context) {
		// ����PopupWindow�Ĳ����ļ�
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.read_style, null);
		// ����PopupWindow��ý�鲼���ļ�
		View parentView = layoutInflater.inflate(R.layout.main, null);
		// ʵ����PopupWindow�е����
		// TextView titleTextView = (TextView)
		// view.findViewById(R.id.popup_title);
		// ImageView editImg = (ImageView) view.findViewById(R.id.popup_edit);
		// ImageView listenImg = (ImageView)
		// view.findViewById(R.id.popup_listen);

		// SeekBar seekBar = (SeekBar) view.findViewById(R.id.popup_progress);
		// TextView progressTextView = (TextView) view
		// .findViewById(R.id.popup_progress_tv);
		// view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// GridView gridView = (GridView) view.findViewById(R.id.popup_grid);
		// GridViewAdapter adapter = new GridViewAdapter(this, mPictures,
		// mTitles);
		// gridView.setAdapter(adapter);
		// gridView.setOnItemClickListener(new ItemClickListener());
		// gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected
		 * (android.widget.AdapterView, android.view.View, int, long)
		 */
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// /*
		// * (non-Javadoc)
		// *
		// * @see
		// * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
		// * (android.widget.AdapterView)
		// */
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// ������ʵ����PopupWindow
		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// ΪPopupWindow���õ�����λ��
		// popupWindow.showAsDropDown(parentView);

		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		// popupWindow.
		// popupWindow.update();
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);// �����������ʧ,��Ҫ���ñ��������㡢touchable��update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// �����Ϊ�˷�ֹ�����˵���ȡ����֮�󣬵��activity���������û����Ӧ
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		// popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});

		View color = view.findViewById(R.id.color1);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color2);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color3);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color4);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color5);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color6);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color7);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color8);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color9);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color10);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color11);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color12);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color13);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color14);
		color.setOnClickListener(turntest.this);
		color = view.findViewById(R.id.color15);
		color.setOnClickListener(turntest.this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	// lxm
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				mPageWidget.abortAnimation();
				mPageWidget.bIsShowNoMore = false;
				mPageWidget.calcCornerXY(mWidth * 1 / 8, mHeight * 1 / 8);
				mPageWidget.settouchpos(mWidth * 1 / 8, mHeight * 1 / 8);
				Log.i("lxm", "setOnTouchListener");
				pagefactory.Draw(mCurPageCanvas, mPageWidget);
				// if (mPageWidget.DragToRight()) {
				try {
					pagefactory.prePage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (pagefactory.isfirstPage()) {
					mPageWidget.bIsShowNoMore = true;
					mPageWidget.invalidate();
					return true;
				}
				pagefactory.Draw(mNextPageCanvas, mPageWidget);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
				mPageWidget.startAnimation(1200);
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				// } else {
				mPageWidget.abortAnimation();
				mPageWidget.bIsShowNoMore = false;
				mPageWidget.calcCornerXY(mWidth * 7 / 8, mHeight * 7 / 8);
				mPageWidget.settouchpos(mWidth * 7 / 8, mHeight * 7 / 8);
				Log.i("lxm", "setOnTouchListener");
				pagefactory.Draw(mCurPageCanvas, mPageWidget);
				try {
					pagefactory.nextPage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (pagefactory.islastPage()) {
					mPageWidget.bIsShowNoMore = true;
					mPageWidget.invalidate();
					return true;
				}
				// return true;
				pagefactory.Draw(mNextPageCanvas, mPageWidget);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
				mPageWidget.startAnimation(1200);
				// }
				return true;
				// break;

			default:
				break;
			}
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * ���õ�������Դ��cpdetector��ȡ�ļ������ʽ
	 * 
	 * @param path
	 *            Ҫ�ж��ļ������ʽ��Դ�ļ���·��
	 * @author huanglei
	 * @version 2012-7-12 14:05
	 */
	public static String getFileEncode(String path) {
		/*
		 * detector��̽����������̽�����񽻸������̽��ʵ�����ʵ����ɡ�
		 * cpDetector������һЩ���õ�̽��ʵ���࣬��Щ̽��ʵ�����ʵ������ͨ��add���� �ӽ�������ParsingDetector��
		 * JChardetFacade��ASCIIDetector��UnicodeDetector��
		 * detector���ա�˭���ȷ��طǿյ�̽���������Ըý��Ϊ׼����ԭ�򷵻�̽�⵽��
		 * �ַ������롣ʹ����Ҫ�õ�����������JAR����antlr.jar��chardet.jar��cpdetector.jar
		 * cpDetector�ǻ���ͳ��ѧԭ��ģ�����֤��ȫ��ȷ��
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/*
		 * ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı���,���췽���еĲ�������
		 * ָʾ�Ƿ���ʾ̽����̵���ϸ��Ϣ��Ϊfalse����ʾ��
		 */
		detector.add(new ParsingDetector(false));
		/*
		 * JChardetFacade��װ����Mozilla��֯�ṩ��JChardet����������ɴ�����ļ��ı���
		 * �ⶨ�����ԣ�һ���������̽�����Ϳ�����������Ŀ��Ҫ������㻹�����ģ�����
		 * �ٶ�Ӽ���̽���������������ASCIIDetector��UnicodeDetector�ȡ�
		 */
		detector.add(JChardetFacade.getInstance());// �õ�antlr.jar��chardet.jar
		// ASCIIDetector����ASCII����ⶨ
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector����Unicode�������Ĳⶨ
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		File f = new File(path);
		try {
			charset = detector.detectCodepage(f.toURI().toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null)
			return charset.name();
		else
			return null;
	}

	public void initFontPopupWindow(Context context) {
		// ����PopupWindow�Ĳ����ļ�
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.popup_font_size, null);
		// ����PopupWindow��ý�鲼���ļ�
		View parentView = layoutInflater.inflate(R.layout.main, null);

		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);// �����������ʧ,��Ҫ���ñ��������㡢touchable��update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// �����Ϊ�˷�ֹ�����˵���ȡ����֮�󣬵��activity���������û����Ӧ
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});
		final SeekBar adjest = (SeekBar) view.findViewById(R.id.read_font_size);
		int fontsize = getIntPeizhi(FONT_SIZE, 29);
		adjest.setProgress((int) (fontsize - 14));// light.

		adjest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * �϶���ֹͣ�϶���ʱ�����
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// description.setText("�϶�ֹͣ");
			}

			/**
			 * �϶�����ʼ�϶���ʱ�����
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// description.setText("��ʼ�϶�");
			}

			/**
			 * �϶������ȸı��ʱ�����
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				PutIntPeizhi(FONT_SIZE, progress + 14);
				setfont(progress + 14);
			}
		});

		final SeekBar adjest1 = (SeekBar) view
				.findViewById(R.id.read_line_jianju);
		int Line_jianju = getIntPeizhi(LINE_JIANJU, 5);
		adjest1.setProgress((int) (Line_jianju - 2));// light.

		adjest1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * �϶���ֹͣ�϶���ʱ�����
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// description.setText("�϶�ֹͣ");
			}

			/**
			 * �϶�����ʼ�϶���ʱ�����
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// description.setText("��ʼ�϶�");
			}

			/**
			 * �϶������ȸı��ʱ�����
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				PutIntPeizhi(LINE_JIANJU, progress + 2);
				setline_jianju(progress + 2);
			}
		});
	}

	public int getIntPeizhi(String name, int defValue) {
		// Log.i("lxm", name +""+this.getSharedPreferences(CONFIG,
		// Context.MODE_PRIVATE).getInt(
		// name, defValue));
		return this.getSharedPreferences(CONFIG, Context.MODE_PRIVATE).getInt(
				name, defValue);
	}

	public void PutIntPeizhi(String name, int Value) {
		Log.i("lxm", name + "" + Value);
		SharedPreferences Preferences = turntest.this.getSharedPreferences(
				CONFIG, Context.MODE_PRIVATE);
		Preferences.edit().putInt(name, Value).commit();
	}

	public boolean getBoolPeizhi(String name, boolean defValue) {
		// Log.i("lxm", name +""+this.getSharedPreferences(CONFIG,
		// Context.MODE_PRIVATE).getInt(
		// name, defValue));
		return this.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
				.getBoolean(name, defValue);
	}

	public void PutBoolPeizhi(String name, boolean Value) {
		Log.i("lxm", name + "" + Value);
		SharedPreferences Preferences = turntest.this.getSharedPreferences(
				CONFIG, Context.MODE_PRIVATE);
		Preferences.edit().putBoolean(name, Value).commit();
	}

	public void initjinduPopupWindow(Context context) {
		// ����PopupWindow�Ĳ����ļ�
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.popup_jindu, null);
		// ����PopupWindow��ý�鲼���ļ�
		View parentView = layoutInflater.inflate(R.layout.main, null);

		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);// �����������ʧ,��Ҫ���ñ��������㡢touchable��update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// �����Ϊ�˷�ֹ�����˵���ȡ����֮�󣬵��activity���������û����Ӧ
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});
		final SeekBar adjest = (SeekBar) view
				.findViewById(R.id.read_adjust_jindu);

		// SharedPreferences Preferences = turntest.this.getSharedPreferences(
		// CONFIG, Context.MODE_PRIVATE);
		// Float light = Preferences.getFloat(LIGHT, -1);
		adjest.setProgress(pagefactory.get_jindu_baifen());// light.

		// final CheckBox systemLight = (CheckBox) view
		// .findViewById(R.id.checkBox);
		// systemLight.setChecked(Preferences.getBoolean(LIGHT_USERSYSTEM,
		// false));

		adjest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * �϶���ֹͣ�϶���ʱ�����
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// description.setText("�϶�ֹͣ");
			}

			/**
			 * �϶�����ʼ�϶���ʱ�����
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// description.setText("��ʼ�϶�");
			}

			/**
			 * �϶������ȸı��ʱ�����
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//
				set_jindu_baifen(progress);
				// mPageWidget.invalidate();
			}
		});
		// SharedPreferences Preferences = turntest.this.getSharedPreferences(
		// CONFIG, Context.MODE_PRIVATE);
		// Preferences.getBoolean(LIGHT_USERSYSTEM, true);

	}

	private void set_jindu_baifen(int jindu) {
		pagefactory.set_jindu_baifen(jindu);
		pagefactory.Draw(mCurPageCanvas, mPageWidget);
		mPageWidget.setcurBitmaps(mCurPageBitmap);
	}

	private void set_jindu(int jindu) {
		pagefactory.set_jindu(jindu);
		pagefactory.Draw(mCurPageCanvas, mPageWidget);
		mPageWidget.setcurBitmaps(mCurPageBitmap);
	}

	private void show_bookmark() {
		SQLiteDatabase dbSelect = markhelper.getReadableDatabase();
		String col[] = { "begin", "word", "time" };
		Cursor cur = dbSelect.query("markhelper", col, "path = '" + bookPath
				+ "'", null, null, null, null);
		Integer num = cur.getCount();
		if (num == 0) {
			show_toast("You have no bookmarks.");

		} else {
			ArrayList<MarkVo> markList = new ArrayList<MarkVo>();
			while (cur.moveToNext()) {
				String s1 = cur.getString(cur.getColumnIndex("word"));
				String s2 = cur.getString(cur.getColumnIndex("time"));
				int b1 = cur.getInt(cur.getColumnIndex("begin"));
				int p = 0;
				int count = 10;
				MarkVo mv = new MarkVo(s1, p, count, b1, s2, bookPath);
				markList.add(mv);
			}
			mDialog = new MarkDialog(this, markList, mHandler,
					R.style.FullHeightDialog);

			mDialog.setCancelable(false);

			mDialog.setTitle("My Bookmarks:");
			mDialog.show();
		}
	}

	public Handler mHandler = new Handler() {
		// �������̷߳�������Ϣ��ͬʱ����UI
		public void handleMessage(Message msg) {
			int begin = 0;
			switch (msg.what) {
			case 0:
				begin = msg.arg1;
				set_jindu(begin);
				break;
			case 1:
				// pagefactory.setM_mbBufBegin(begin);
				// pagefactory.setM_mbBufEnd(begin);
				// postInvalidateUI();
				break;
			default:
				break;
			}
		}
	};

	private void show_toast(String content) {
		Toast toast = Toast
				.makeText(turntest.this, content, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}