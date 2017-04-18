package io.etna.intranet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class QRCode extends Fragment {
    Bitmap image;
    GenerateQRCode qrcode;

    Button b1;
    Button b2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_qrcode, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Mon QR Code");

        /*QR Code generator*/
        qrcode = new GenerateQRCode("bedmin_j|07934|205");
        ImageView img = (ImageView) getActivity().findViewById(R.id.qrcode);
        img.setImageBitmap(qrcode.getQRCode());

        /*Call buttons*/
        b1 = (Button) getActivity().findViewById(R.id.b1);
        b2 = (Button) getActivity().findViewById(R.id.b2);
        b1.setOnClickListener(call_prep);
        b2.setOnClickListener(call_alt);
    }

    View.OnClickListener call_prep = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = "tel:" + "01 44 08 00 22";
            i.setData(Uri.parse(p));
            startActivity(i);
        }
    };
    View.OnClickListener call_alt = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = "tel:" + "01 84 07 42 50";
            i.setData(Uri.parse(p));
            startActivity(i);
        }
    };



}
