package android.birdgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GameView extends View {
// Declaracion de variables
    private final Bird bird;
    private final Runnable r;
// Constructor
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bird = new Bird();
// Configuracion del tamaño y la posicion del pajaro
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX((float) (100 * Constants.SCREEN_WIDTH) / 1080);
        bird.setY((float) Constants.SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
// Carga las imagenes del pajaro de la carpeta drawable y las asigna al objeto Bird
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird2));
        bird.setArrBms(arrBms);
// Iniciacion del handler para realizar tareas asincronas
        Handler handler = new Handler() {
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
// Inicializa el Runnable para desactivar la vista despues de un tiempo
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }
// Dibujar la vista del juego en la pantalla
    public void draw(@NonNull Canvas canvas){
        super.draw(canvas);
        bird.draw(canvas);
        getHandler().postDelayed(r,10);
    }
// Metodo para que el pajaro reaccione a cuando se de click en la pantalla
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
        }
        return true;
    }
}
