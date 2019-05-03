package utaeats.uta.mav.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UTAEatsController {
    private RelativeLayout invoiceLayout;
    private Bitmap bitmap;

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void save(RelativeLayout invoiceLayout, Context applicationContext){
        try {
            this.invoiceLayout = invoiceLayout;
            bitmap = loadBitmapFromView(this.invoiceLayout, this.invoiceLayout.getWidth(), this.invoiceLayout.getHeight());
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(displaymetrics);
            float hight = displaymetrics.heightPixels ;
            float width = displaymetrics.widthPixels ;

            int convertHighet = (int) hight, convertWidth = (int) width;

            //        Resources mResources = getResources();
            //        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0 , null);
            document.finishPage(page);

            // write the document content
            String targetPdf = "/sdcard/pdffromlayout.pdf";
            File filePath;
            filePath = new File(targetPdf);

            document.writeTo(new FileOutputStream(filePath));
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(applicationContext, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        Toast.makeText(applicationContext, "PDF is created!!!", Toast.LENGTH_SHORT).show();
        openGeneratedPDF(applicationContext);
    }
    private void openGeneratedPDF(Context applicationContext){
        File file = new File("/sdcard/pdffromlayout.pdf");
        if (file.exists())
        {
            Intent intent = new Intent();
            intent.setPackage("com.adobe.reader");
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");

            try
            {
                applicationContext.startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(applicationContext, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
}
