package com.treasure.traveldiary.activity.diary_center;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class GamePuzzleActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ViewStub puzzle_main,puzzle_pre;
    private RadioButton easy_btn,normal_btn,hard_btn;
    private ImageView easy_img,normal_img,hard_img,game_thumbnail,game_reset;
    private FrameLayout play_btn;
    private TextView game_time,pre_start;
    private GridLayout game_page;
    private Bitmap puzzle_bitmap;
    private ImageView[][] iv_game_arr;
    private ImageView iv_null_game;
    private GestureDetector mDetector;
    private Thread mThread;
    private int gameTime = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    int time = (int) msg.obj;
                    int hour;
                    int minute;
                    int second = 0;
                    if (time >= 3600) {
                        hour = time / 3600;
                        time = time - hour * 3600;
                    } else {
                        hour = 0;
                    }
                    if (time >= 60) {
                        minute = time / 60;
                        time = time - minute * 60;
                    } else {
                        minute = 0;
                    }
                    if (time >= 0) {
                        second = time;
                    }
                    if (second >= 10) {
                        if (minute >= 10) {
                            game_time.setText("0" + hour + ":" + minute + ":" + second);
                        } else {
                            game_time.setText("0" + hour + ":" + "0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            game_time.setText("0" + hour + ":" + minute + ":" + "0" + second);
                        } else {
                            game_time.setText("0" + hour + ":" + "0" + minute + ":" + "0" + second);
                        }
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_puzzle);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("拼图游戏");
        puzzle_pre = (ViewStub) findViewById(R.id.puzzle_game_pre);
        puzzle_main = (ViewStub) findViewById(R.id.puzzle_game_main);
        puzzle_main.inflate();
        puzzle_pre.inflate();
        puzzle_pre.setVisibility(View.VISIBLE);
        initFindId();
        initClick();
        easy_btn.setChecked(true);
        puzzle_bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.pic_lufei)).getBitmap();
        game_thumbnail.setImageBitmap(puzzle_bitmap);
        setDetector();
    }

    private void initFindId() {
        easy_btn = (RadioButton) findViewById(R.id.puzzle_game_easy_btn);
        normal_btn = (RadioButton) findViewById(R.id.puzzle_game_normal_btn);
        hard_btn = (RadioButton) findViewById(R.id.puzzle_game_hard_btn);
        easy_img = (ImageView) findViewById(R.id.puzzle_game_easy_img);
        normal_img = (ImageView) findViewById(R.id.puzzle_game_normal_img);
        hard_img = (ImageView) findViewById(R.id.puzzle_game_hard_img);
        game_thumbnail = (ImageView) findViewById(R.id.puzzle_game_thumbnail);
        game_reset = (ImageView) findViewById(R.id.puzzle_game_reset);
        play_btn = (FrameLayout) findViewById(R.id.puzzle_game_play);
        pre_start = (TextView) findViewById(R.id.puzzle_game_pre_start);
        game_time = (TextView) findViewById(R.id.puzzle_game_time);
        game_page = (GridLayout) findViewById(R.id.puzzle_game_easy_page);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        pre_start.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        easy_img.setOnClickListener(this);
        normal_img.setOnClickListener(this);
        hard_img.setOnClickListener(this);
        easy_btn.setOnCheckedChangeListener(this);
        normal_btn.setOnCheckedChangeListener(this);
        hard_btn.setOnCheckedChangeListener(this);
        game_reset.setOnClickListener(this);
    }

    private void setDetector() {
        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int dirByGes = getDirByGes(e1.getX(), e1.getY(), e2.getX(), e2.getY());
                changeByDir(dirByGes);
                boolean isGameOver= judgeGameOver();
                if (isGameOver){
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePuzzleActivity.this);
                    builder.setMessage("想要挑战下去吗？");
                    builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(GamePuzzleActivity.this, "老铁，看你的了", Toast.LENGTH_SHORT).show();
                            puzzle_pre.setVisibility(View.VISIBLE);
                            puzzle_main.setVisibility(View.GONE);
                            game_time.setVisibility(View.GONE);
                        }
                    });
                    builder.setNegativeButton("走了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            GamePuzzleActivity.this.finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return false;
            }
        });
    }

    private boolean judgeGameOver() {
        boolean isGameOver = true;
        for (int i = 0; i < iv_game_arr.length; i++) {
            for (int j = 0; j < iv_game_arr[0].length; j++) {
                if (iv_game_arr[i][j] == iv_null_game)
                    continue;
                GameData gameData = (GameData) iv_game_arr[i][j].getTag();
                if (!gameData.isTrue()){
                    isGameOver = false;
                    break;
                }
            }
        }
        return isGameOver;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private int getDirByGes(float x, float y, float x1, float y1) {
        boolean horizontal = (Math.abs(x - x1) > Math.abs(y - y1)) ? true : false;
        if (horizontal){
            boolean isToLeft = x - x1 > 0 ? true : false;
            if (isToLeft){
                return 3;
            }else {
                return 4;
            }
        }else {
            boolean isToTop = y - y1 > 0 ? true : false;
            if (isToTop){
                return 1;
            }else {
                return 2;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                GamePuzzleActivity.this.finish();
                break;
            case R.id.puzzle_game_pre_start:
                puzzle_pre.setVisibility(View.GONE);
                puzzle_main.setVisibility(View.VISIBLE);
                if (easy_btn.isChecked()){
                    play_btn.setVisibility(View.VISIBLE);
                    iv_game_arr = new ImageView[3][3];
                    int bm_width = puzzle_bitmap.getWidth() / 3;
                    int bm_height = puzzle_bitmap.getHeight() / 3;
                    setPicAnyBox(bm_width,bm_height);//设置每一个方块
                }else if (normal_btn.isChecked()){
                    play_btn.setVisibility(View.VISIBLE);
                    int bm_width = puzzle_bitmap.getWidth() / 4;
                    int bm_height = puzzle_bitmap.getHeight() / 4;
                    iv_game_arr = new ImageView[4][4];
                    setPicAnyBox(bm_width,bm_height);
                }else if (hard_btn.isChecked()){
                    play_btn.setVisibility(View.VISIBLE);
                    int bm_width = puzzle_bitmap.getWidth() / 5;
                    int bm_height = puzzle_bitmap.getHeight() / 5;
                    iv_game_arr = new ImageView[5][5];
                    setPicAnyBox(bm_width,bm_height);
                }
                break;
            case R.id.puzzle_game_play:
                play_btn.setVisibility(View.GONE);
                gameTime =0;
                game_time.setVisibility(View.VISIBLE);
                randomMove();
                startTime();
                break;
            case R.id.puzzle_game_easy_img:
                easy_btn.setChecked(true);
                normal_btn.setChecked(false);
                hard_btn.setChecked(false);
                easy_img.setBackgroundResource(R.color.colorOrange);
                normal_img.setBackgroundResource(R.color.colorFull);
                hard_img.setBackgroundResource(R.color.colorFull);
                break;
            case R.id.puzzle_game_normal_img:
                easy_btn.setChecked(false);
                normal_btn.setChecked(true);
                hard_btn.setChecked(false);
                easy_img.setBackgroundResource(R.color.colorFull);
                normal_img.setBackgroundResource(R.color.colorOrange);
                hard_img.setBackgroundResource(R.color.colorFull);
                break;
            case R.id.puzzle_game_hard_img:
                easy_btn.setChecked(false);
                normal_btn.setChecked(false);
                hard_btn.setChecked(true);
                easy_img.setBackgroundResource(R.color.colorFull);
                normal_img.setBackgroundResource(R.color.colorFull);
                hard_img.setBackgroundResource(R.color.colorOrange);
                break;
            case R.id.puzzle_game_reset:
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(game_reset, "Rotation", -360, 0);
                // 设置持续时间
                objectAnimator.setDuration(500);
                // 设置循环播放
                objectAnimator.setRepeatCount(1);
                objectAnimator.start();
                Toast.makeText(this, "老铁，重新玩", Toast.LENGTH_SHORT).show();
                iv_null_game.setImageBitmap(null);
                randomMove();
                break;
        }
    }

    private void startTime() {
        if (mThread == null){
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            Thread.sleep(1000);
                            gameTime ++;
                            Message message = mHandler.obtainMessage(200);
                            message.obj = gameTime;
                            mHandler.sendMessage(message);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            if (buttonView.getText().equals("简单")){
                easy_btn.setChecked(true);
                normal_btn.setChecked(false);
                hard_btn.setChecked(false);
                easy_img.setBackgroundResource(R.color.colorOrange);
                normal_img.setBackgroundResource(R.color.colorFull);
                hard_img.setBackgroundResource(R.color.colorFull);
            }else if (buttonView.getText().equals("普通")){
                easy_btn.setChecked(false);
                normal_btn.setChecked(true);
                hard_btn.setChecked(false);
                easy_img.setBackgroundResource(R.color.colorFull);
                normal_img.setBackgroundResource(R.color.colorOrange);
                hard_img.setBackgroundResource(R.color.colorFull);
            }else if (buttonView.getText().equals("困难")){
                easy_btn.setChecked(false);
                normal_btn.setChecked(false);
                hard_btn.setChecked(true);
                easy_img.setBackgroundResource(R.color.colorFull);
                normal_img.setBackgroundResource(R.color.colorFull);
                hard_img.setBackgroundResource(R.color.colorOrange);
            }
        }
    }

    private void setPicAnyBox(int bm_width, int bm_height) {
        game_page.setColumnCount(iv_game_arr.length);
        game_page.setRowCount(iv_game_arr[0].length);
        for (int i = 0; i < iv_game_arr.length; i++) {
            for (int j = 0; j < iv_game_arr[0].length; j++) {
                Bitmap bitmap = Bitmap.createBitmap(puzzle_bitmap, j * bm_width, i * bm_height, bm_width, bm_height);
                iv_game_arr[i][j] = new ImageView(this);
                iv_game_arr[i][j].setImageBitmap(bitmap);
                iv_game_arr[i][j].setPadding(2,2,2,2);
                iv_game_arr[i][j].setTag(new GameData(i,j,bitmap));
                game_page.addView(iv_game_arr[i][j]);
            }
        }
        //设置最后一个为空，并将空的记录
        iv_game_arr[iv_game_arr.length - 1][iv_game_arr[0].length - 1].setImageBitmap(null);
        iv_null_game = iv_game_arr[iv_game_arr.length - 1][iv_game_arr[0].length - 1];
    }

    private void randomMove() {
        for (int i = 0; i < 100; i++) {
            int type = (int) ((Math.random() * 4) + 1);
            changeByDir(type);
        }
    }

    private void changeByDir(int type) {
        GameData nullData = (GameData) iv_null_game.getTag();
        int new_x = nullData.x;
        int new_y = nullData.y;
        if (type == 1){
            new_x++;
        }else if (type == 2){
            new_x--;
        }else if (type == 3){
            new_y++;
        }else if (type == 4){
            new_y--;
        }
        if ((new_x >=0&&new_x<iv_game_arr.length)&&(new_y>=0&&new_y<iv_game_arr[0].length)){
            changeDataStartByImageView(iv_game_arr[new_x][new_y]);
        }
    }

    private void changeDataStartByImageView(ImageView imageView) {
        GameData gameData = (GameData) imageView.getTag();
        iv_null_game.setImageBitmap(gameData.bm);
        GameData nullData = (GameData) iv_null_game.getTag();
        nullData.bm = gameData.bm;
        nullData.p_x = gameData.p_x;
        nullData.p_y = gameData.p_y;
        imageView.setImageBitmap(null);
        iv_null_game = imageView;
    }

    /**每个游戏小方块上要绑定的数据*/
    static class GameData {
        /**每个小方块的实际位置x*/
        public int x=0;
        /**每个小方块的实际位置y*/
        public int y=0;
        /**每个小方块的图片*/
        public  Bitmap bm;
        /**每个小方块的图片的位置*/
        public int p_x=0;
        /**每个小方块的图片的位置*/
        public int p_y=0;
        public GameData(int x , int y, Bitmap bm ){
            super();
            this.x=x;
            this.y=y;
            this.bm=bm;
            this.p_x=x;
            this.p_y=y;

        }
        /**
         * 判断每个小方块的位置是否正确
         * @return true:正确；false:不正确
         */
        public  boolean isTrue(){
            if (x==p_x&&y==p_y){
                return true;
            }
            return false;
        }


    }
}
