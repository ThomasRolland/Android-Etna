package io.etna.intranet;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * Created by nextjoey on 14/04/2017.
 */

public class GenerateQRCode {

    Bitmap bitmap;
    Bitmap qrImage;


    public GenerateQRCode(String inputValue) {
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 260);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage = bitmap;
        } catch (WriterException e) {
            Log.v("Erreur QRCode : ", e.toString());
        }
    }

    public Bitmap getQRCode() { return qrImage; }


}
