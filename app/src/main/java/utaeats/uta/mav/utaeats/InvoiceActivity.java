package utaeats.uta.mav.utaeats;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import utaeats.uta.mav.models.Cart;
import utaeats.uta.mav.models.Items;

public class InvoiceActivity extends AppCompatActivity {

    ArrayList<Items> items = new ArrayList<Items>();
    private ListView listView;
    private CustomInvoiceAdapter customListView;
    private RelativeLayout invoiceLayout;
    private Bitmap bitmap;

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#35838F'>Invoice</font>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.invoiceListView);

        customListView = new CustomInvoiceAdapter(this, Cart.cartItems);
        listView.setAdapter(customListView);

        float totalCost = 0.0f;
        for (Items itemloop:items
             ) {
            totalCost += Float.parseFloat(itemloop.getCost()) * Integer.parseInt(itemloop.getNo_of_servings());
        }

        TextView totalCostView = findViewById(R.id.totalInvoiceCost);
        totalCostView.setText("$"+String.valueOf(totalCost));
    }

    public void saveAsPDF(View view) {
        // Here, thisActivity is the current activity
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        System.out.println(task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    System.out.println("Token = "+token);
                }
            });
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
            save();
        }
    }

    public void save(){
        try {
            invoiceLayout = findViewById(R.id.invoiceLayout);
            bitmap = loadBitmapFromView(invoiceLayout, invoiceLayout.getWidth(), invoiceLayout.getHeight());
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            //  Display display = wm.getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();
        openGeneratedPDF();
    }

    private void openGeneratedPDF(){
        File file = new File("/sdcard/pdffromlayout.pdf");
        if (file.exists())
        {
            Intent intent = new Intent();
            intent.setPackage("com.adobe.reader");
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goBuyer(View view) {
        Intent i = new Intent(this, FoodActivity.class);
        startActivity(i);
        finish();
    }

}
