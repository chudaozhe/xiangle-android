package net.cuiwei.xiangle.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import net.cuiwei.xiangle.R;

/**
 * A helper class facilitates loading a remote image into a given ListView.
 * http://www.cnblogs.com/bjzhanghao/archive/2012/11/11/2764970.html
 * 
 * @author zhanghao
 *
 */
public class RemoteImageHelper {

	private final Map<String, Drawable> cache = new HashMap<String, Drawable>();

	public void loadImage(final ImageView imageView, final String urlString) {
		loadImage(imageView, urlString, true);
	}

	public void loadImage(final ImageView imageView, final String urlString, boolean useCache) {
		if (useCache && cache.containsKey(urlString)) {
			imageView.setImageDrawable(cache.get(urlString));
		}

		//You may want to show a "Loading" image here
		imageView.setImageResource(R.mipmap.email2);

		Log.d(this.getClass().getSimpleName(), "Image url:" + urlString);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageView.setImageDrawable((Drawable) message.obj);
			}
		};

		Runnable runnable = new Runnable() {
			public void run() {
				Drawable drawable = null;
				try {
					InputStream is = download(urlString);
					drawable = Drawable.createFromStream(is, "src");

					if (drawable != null) {
						cache.put(urlString, drawable);
					}
				} catch (Exception e) {
					Log.e(this.getClass().getSimpleName(), "Image download failed", e);
					//Show "download fail" image 
					drawable = imageView.getResources().getDrawable(R.mipmap.ic_launcher);
				}
				
				//Notify UI thread to show this image using Handler
				Message msg = handler.obtainMessage(1, drawable);
				handler.sendMessage(msg);
			}
		};
		new Thread(runnable).start();

	}

	/**
	 * Download image from given url.
	 * Make sure you have "android.permission.INTERNET" permission set in AndroidManifest.xml.
	 * 
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private InputStream download(String urlString) throws MalformedURLException, IOException {
		InputStream inputStream = (InputStream) new URL(urlString).getContent();
		return inputStream;
	}
}
