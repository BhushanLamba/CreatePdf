package it.services.createpdftesting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    Button btnCreatePdf, btnOpenPdf;
    private final String TAG = "pdf";

    String[] keysArray = new String[]{"Name", "Company Name", "Address", "Phone", "Email"};
    String[] valuesArray = new String[]{"Bhushan Lamba", "Lamba Enterprises", "H no.48 Relipay house", "8527365890", "bhushanlamba97@gmail.com"};

    Bitmap bitmapLogo, scaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreatePdf = findViewById(R.id.btn_create_pdf);
        btnOpenPdf = findViewById(R.id.btn_open_pdf);

        bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo1_screen1);
        scaledBitmap = Bitmap.createScaledBitmap(bitmapLogo, 150, 50, false);
        btnCreatePdf.setOnClickListener(v -> createPdf());
        btnOpenPdf.setOnClickListener(v -> openPDF("/storage/emulated/0/Download/RelipayCms.pdf"));
    }

    private void createPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 1200, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();


        canvas.drawBitmap(scaledBitmap, 220, 10, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(28.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setColor(Color.rgb(16, 31, 51));
        canvas.drawText("CMS Receipt", pageInfo.getPageWidth() >> 1, 100, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.rgb(16, 31, 51));
        canvas.drawText("Details", 10, 130, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawText("CSP FIRM NAME", 10, 160, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawText("Lamba Enterprise", 250, 160, paint);



        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawText("CSP Mobile", 10, 190, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawText("8527365890", 250, 190, paint);


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        canvas.drawLine(10, 210, 590, 210, paint);
        canvas.drawLine(10, 220, 590, 220, paint);

        int startXPositionKeys = 10;
        int startXPositionValues = 200;
        int startYPosition = 260;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);

        for (int i = 0; i < 5; i++) {
            canvas.drawText(keysArray[i], startXPositionKeys, startYPosition, paint);
            canvas.drawText(valuesArray[i], startXPositionValues, startYPosition, paint);
            startYPosition += 20;
        }


       /* int startXPosition = 10;
        int endXPosition = pageInfo.getPageWidth() - 10;
        int startYPosition = 130;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.0f);
        paint.setColor(Color.BLACK);

        for (int i = 0; i < 5; i++) {
            canvas.drawText(informationArray[i], startXPosition, startYPosition, paint);
            canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, paint);
            startYPosition += 20;
        }

        canvas.drawLine(110, 120, 110, 213, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawRect(10, 220, pageInfo.getPageWidth() - 10, 320, paint);

        canvas.drawLine(100, 220, 100, 320, paint);
        canvas.drawLine(200, 220, 200, 320, paint);

        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawText("Photo", 40, 270, paint);
        canvas.drawText("Photo", 130, 270, paint);
        canvas.drawText("Photo", 225, 270, paint);*/

        pdfDocument.finishPage(page);

        File file = new File("/storage/emulated/0/Download/", "/RelipayCms.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));

        } catch (Exception e) {
            Log.d(TAG, "createPdf: " + e.getLocalizedMessage());
        }
        pdfDocument.close();
    }

    private void openPDF(String filePath) {
        File file = new File(filePath);
        Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application available to view PDF", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}