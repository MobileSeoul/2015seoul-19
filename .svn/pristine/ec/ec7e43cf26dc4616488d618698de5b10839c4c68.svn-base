package db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.SQLException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import util.Logger;

/**
 * Created by HanyLuv on 2015-10-24.
 */
public class CartoonDBHelper extends SQLiteOpenHelper implements ICartoonDAO {

    public CartoonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void setDB(Context context) {
        File folder = new File(PACKAGE_DIR);
        if (!folder.exists()) folder.mkdirs();

        File outfile = new File(PACKAGE_DIR + "/" + DATABASE_NAME);

        if (outfile.length() <= 0) {
            AssetManager assetManager = context.getResources().getAssets();
            try {
                InputStream is = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
                long filesize = is.available();
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
