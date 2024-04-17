package android.birdgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;

public class Bird extends BaseObject {
    private ArrayList<Bitmap> arrBms = new ArrayList<>();
    private int count;
    private final int vFlap;
    private int idCurrentBitmap;
    private float drop;

    public Bird() {
        this.count = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.drop = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
        drop();
    }
    public Rect getRect() {
        return new Rect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height));
    }

    private void drop() {
        this.drop += 0.6F;
        this.y += this.drop;
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        for (int i = 0; i < arrBms.size(); i++) {
            this.arrBms.set(i, scaleBitmap(arrBms.get(i), this.width, this.height));
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    @Override
    public Bitmap getBm() {
        count++;
        if (count == vFlap) {
            idCurrentBitmap = (idCurrentBitmap + 1) % arrBms.size();
            count = 0;
        }
        if (drop < 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-25);
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
        } else {
            Matrix matrix = new Matrix();
            if (drop < 70) {
                matrix.postRotate(-25 + (drop * 2));
            } else {
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
        }
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }
}

