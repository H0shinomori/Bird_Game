package android.birdgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GameView extends View {
    private Bird bird;
    private Runnable r;
    private Handler handler;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe, distance;
    private int score, bestscore;
    private boolean start;
    private Context context;
    private int soundJump;
    private float volume;
    private boolean loadedsound;
    private SoundPool soundPool;
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestscore = sp.getInt("bestscore", 0);
        }
        score = 0;
        start = false;
        initBird();
        initPipe();
        handler = new Handler() {
            @Override
            public void publish(LogRecord record) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if(Build.VERSION.SDK_INT >= 21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedsound = true;
            }
        });
        soundJump = this.soundPool.load(context, R.raw.jump_02, 1);
    }
    private void initPipe() {
        sumpipe = 6;
        distance = 300 * Constants.SCREEN_HEIGHT / 1920;
        arrPipes = new ArrayList<>();
        for (int i = 0; i < sumpipe; i++) {
            if (i < sumpipe / 2) {
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH + i * ((float) (Constants.SCREEN_WIDTH + 200 * Constants.SCREEN_WIDTH / 1080) / ((float) sumpipe / 2)),
                        0, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2));
                this.arrPipes.get(i).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size() - 1).randomY();
            } else {
                this.arrPipes.add(new Pipe(this.arrPipes.get(i - sumpipe / 2).getX(), this.arrPipes.get(i - sumpipe / 2).getY()
                        + this.arrPipes.get(i - sumpipe / 2).getHeight() + this.distance, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2));
                this.arrPipes.get(this.arrPipes.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
            }
        }
    }
    private void initBird() {
        bird = new Bird();
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX((float) (100 * Constants.SCREEN_WIDTH) / 1080);
        bird.setY((float) Constants.SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird2));
        bird.setArrBms(arrBms);
    }
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (start) {
            bird.draw(canvas);
            for (int i = 0; i < sumpipe; i++) {
                if (bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY() - bird.getHeight() < 0 || bird.getY() > Constants.SCREEN_HEIGHT) {
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("Best: " + bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                }
                if (this.bird.getX() + this.bird.getWidth() > arrPipes.get(i).getX() + arrPipes.get(i).getWidth() / 2 && this.bird.getX() + this.bird.getWidth() <= arrPipes.get(i).getX() + arrPipes.get(i).getWidth() / 2 + Pipe.speed && i < sumpipe / 2) {
                    score++;
                    if(score > bestscore){
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore", score);
                        editor.apply();
                        bestscore = score;
                    }
                    MainActivity.txt_score.setText("" + score);
                }
                if (this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()) {
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if (i < sumpipe / 2) {
                        arrPipes.get(i).randomY();
                    } else {
                        arrPipes.get(i).setY(this.arrPipes.get(i - sumpipe / 2).getY()
                                + this.arrPipes.get(i - sumpipe / 2).getHeight() + this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }

        } else {
            if (bird.getY() > Constants.SCREEN_HEIGHT / 2) {
                bird.setDrop(-15 * Constants.SCREEN_HEIGHT / 1920);
            }
            bird.draw(canvas);
        }
        getHandler().postDelayed(r, 10);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-15);
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundJump, (float) 0.5,(float) 0.5,1,0,1f);
            }
        }
        return true;
    }
    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        initPipe();
        initBird();
    }
}
