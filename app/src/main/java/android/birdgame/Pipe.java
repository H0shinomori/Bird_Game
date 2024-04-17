package android.birdgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Pipe extends BaseObject{
    public static int speed;

    public Pipe(float x, float y, int width, int height){
        super(x, y, width, height);
        speed = 10*Constants.SCREEN_WIDTH/1080;
    }
    public void draw(Canvas canvas){
        this.x -= speed;
        canvas.drawBitmap(this.bm, this.x, this.y, null);
    }
    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt((this.height / 4) + 1) - (float) this.height /4;
    }
    public Rect getRect() {
        return new Rect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height));
    }
    @Override
    public void setBm(Bitmap bm){
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }

}
