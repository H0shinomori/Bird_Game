package android.birdgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Bird extends BaseObject {
// Declaracion de variables
    private ArrayList<Bitmap> arrBms = new ArrayList<>();
    private int count;
    private final int vFlap;
    private int idCurrentBitmap;
    private float drop;
// Constructor de la clase Bird junto a sus variables
    public Bird(){
        this.count = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.drop = 0;
    }
// Metodo para "dibujar" el pajaro en la pantalla y lo situa en un punto en concreto
    public void draw(Canvas canvas){
        canvas.drawBitmap(this.getBm(),this.x,this.y,null);
        drop(); // Utiliza el metodo drop() para hacer que el pajaro tenga una caida siempre
    }
// Metodo que permite que el pajaro se caiga
    private void drop() {
        this.drop += 0.6F; // Agumenta la velocidad de la caida
        this.y += this.drop; // Actiualiza la posicion del pajaro
    }
// Getter del array de bitmap del pajaro
    public ArrayList<Bitmap> getArrBms(){
        return arrBms;
    }
// Setter del array de bitmap del pajaro
    public void setArrBms(ArrayList<Bitmap> arrBms){
        this.arrBms = arrBms;
        // Escala los bitmaps para que se ajusten al tamaño del pajaro
        for(int i = 0; i < arrBms.size(); i++){
            this.arrBms.set(i, scaleBitmap(arrBms.get(i), this.width, this.height));
        }
    }
// Metodo para ecalar el bitmap
    private Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
// Getter para obtener el bitmap actual del pajaro
    @Override
    public Bitmap getBm(){
        count++;
        if(this.count == this.vFlap){
            for(int i = 0; i < arrBms.size(); i++){
                // Si llega al final, se reinicia el bitmap
                if(i == arrBms.size()-1){
                    this.idCurrentBitmap = 0;
                    break;
                // Cambio de bitmap
                } else if (this.idCurrentBitmap == i) {
                    idCurrentBitmap = i+1;
                    break;
                }
            }
            count = 0;
        }
// Verificacion de si el pajaro esta cayendo
        if(this.drop < 0 ){
            Matrix matrix = new Matrix();
            matrix.postRotate(-25);
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap),0,0,arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight());
// Verificacion de si el pajaro esta subiendo
        } else if (drop >= 0) {
            Matrix matrix = new Matrix();
            if (drop < 70) {
                matrix.postRotate(-25 + (drop * 2));
            } else {
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight());
        }
            return this.arrBms.get(idCurrentBitmap);
    }
// Obtener la velocidad de la caida
    public float getDrop() {
        return drop;
    }
// Establecer velocidad de la caida
    public void setDrop(float drop) {
        this.drop = drop;
    }
}
