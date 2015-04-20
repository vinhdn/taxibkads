package bk.vinhdo.taxiads.utils.bitmap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore.Images;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap util class
 * 
 * @author khanhnv
 * 
 */
public class BitmapUtil {

    private static String TAG = BitmapUtil.class.getName();

	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * Scale bitmap from resource
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(Resources res, int resId, int reqWidth,
			int reqHeight) {
		// create bitmap options
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Scale bitmap from path
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(String pathName, int reqWidth,
			int reqHeight) {

		// create bitmap options
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	/**
	 * Crop bitmap
	 * 
	 * @param context
	 * @param source
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	public static Bitmap cropBitmap(Context context, Bitmap source, int left,
			int top, int right, int bottom) {

		Bitmap result = null;
		try {
			Paint paint = new Paint();
			paint.setFilterBitmap(true);

			int targetWidth = source.getWidth();
			int targetHeight = source.getHeight();

			Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
					targetHeight, Config.ARGB_8888);

			Canvas canvas = new Canvas(targetBitmap);
			canvas.drawBitmap(source,
					new Rect(0, 0, source.getWidth(), source.getHeight()),
					new Rect(0, 0, targetWidth, targetHeight), paint);

			Matrix matrix = new Matrix();
			matrix.postScale(1f, 1f);
			result = Bitmap.createBitmap(targetBitmap, left, top, right,
					bottom, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param source
	 * @param newHeight
	 * @param newWidth
	 * @return
	 */
	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
			int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	/**
	 * 
	 * @param bitmap
	 * @param leftRightThk
	 * @param bottomThk
	 * @param padTop
	 * @return
	 */
	public static Bitmap drawShadow(Bitmap bitmap, int leftRightThk,
			int bottomThk, int padTop) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int newW = w - (leftRightThk * 2);
		int newH = h - (bottomThk + padTop);

		Config conf = Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(w, h, conf);
		Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, newW, newH, false);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Canvas c = new Canvas(bmp);

		// Left
		int leftMargin = (leftRightThk + 7) / 2;
		Shader lshader = new LinearGradient(0, 0, leftMargin, 0,
				Color.TRANSPARENT, Color.BLACK, TileMode.CLAMP);
		paint.setShader(lshader);
		c.drawRect(0, padTop, leftMargin, newH, paint);

		// Right
		Shader rshader = new LinearGradient(w - leftMargin, 0, w, 0,
				Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
		paint.setShader(rshader);
		c.drawRect(newW, padTop, w, newH, paint);

		// Bottom
		Shader bshader = new LinearGradient(0, newH, 0, bitmap.getHeight(),
				Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
		paint.setShader(bshader);
		c.drawRect(leftMargin - 3, newH, newW + leftMargin + 3,
				bitmap.getHeight(), paint);
		c.drawBitmap(sbmp, leftRightThk, 0, null);

		return bmp;
	}

	/**
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffffffff;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Convert Bitmap to String
	 * 
	 * @param bm
	 * @param quality
	 * @return
	 * @throws OutOfMemoryError
	 */
	public static String convertBitmapToString(Bitmap bm, int quality)
			throws OutOfMemoryError {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, quality, baos);
		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}

	/**
	 * Convert String to Bitmap
	 * 
	 * @param encodedImage
	 * @return
	 * @throws OutOfMemoryError
	 */
	public static Bitmap convertStringToBitmap(String encodedImage)
			throws OutOfMemoryError {
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
				decodedString.length);
		return bitmap;
	}

	/**
	 * 
	 * @param context
	 * @param inFile
	 * @param outFile
	 * @param destHeight
	 * @param destWidth
	 */
	public static void resizeImage(Context context, String inFile,
			String outFile, int destHeight, int destWidth) {
		try {
			int inWidth = 0;
			int inHeight = 0;

			InputStream in = new FileInputStream(inFile);

			// decode image size (decode metadata only, not the whole image)
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			in = null;

			// save width and height
			inWidth = options.outWidth;
			inHeight = options.outHeight;
			// byte per pixel = 4
			int sizeOfFile = inWidth * inHeight * 4;
			// 50M = 52428800
			if (sizeOfFile < 0) { // size of file is exceed 50M
				if (getAvailableMemory(context) - 20480 < 52428800) {
					Log.e(TAG,"Out of memory");
                    return;
				}
			} else if (sizeOfFile > getAvailableMemory(context) - 20480) {
				Log.e(TAG,"Out of memory");
				return;
			}

			// decode full image pre-resized
			in = new FileInputStream(inFile);
			options = new BitmapFactory.Options();
			// calc rought re-size (this is no exact resize)
			options.inSampleSize = Math.max(inWidth / destWidth, inHeight
					/ destHeight);
			options.inPurgeable = true;
			options.inPreferredConfig = Config.RGB_565;
			try {
				// decode full image
				Bitmap roughBitmap = BitmapFactory.decodeStream(in, null,
						options);

				// calc exact destination size
				Matrix m = new Matrix();
				RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
						roughBitmap.getHeight());
				RectF outRect = new RectF(0, 0, destWidth, destHeight);
				m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
				float[] values = new float[9];
				m.getValues(values);

				// resize bitmap
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
						(int) (roughBitmap.getWidth() * values[0]),
						(int) (roughBitmap.getHeight() * values[4]), true);

				in.close();

				// save image
				FileOutputStream out = new FileOutputStream(outFile);
				resizedBitmap.compress(CompressFormat.JPEG, 80, out);
				out.close();
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			} catch (OutOfMemoryError e) {
				Log.e(TAG,e.toString());
			}
		} catch (IOException e) {
			Log.e(TAG,e.toString());
		} catch (OutOfMemoryError e) {
			Log.e(TAG,e.toString());
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailableMemory(Context context) {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		return mi.availMem;
	}

	/**
	 * get orientation of bitmap
	 * 
	 * @param exifOrientation
	 * @return
	 */
	public static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	/**
	 * rotate bitmap
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static float getRotationInDegreeOfImage(Context context, Uri uri) {
		if (TextUtils.isEmpty(uri.getScheme())) {
			return 0;
		}
		if (uri.getScheme().equals("content")) {
			String[] projection = { Images.ImageColumns.ORIENTATION };
			Cursor c = context.getContentResolver().query(uri, projection,
					null, null, null);
			if (c.moveToFirst()) {
				return c.getInt(0);
			}
		} else if (uri.getScheme().equals("file")) {
			try {
				ExifInterface exif = new ExifInterface(uri.getPath());
				float rotation = exifOrientationToDegrees(exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL));
				return rotation;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0f;
	}

	/**
	 * get bitmap from image view
	 * 
	 * @param imageView
	 * @return
	 */
	public static Bitmap getBitmapFromImageView(ImageView imageView) {
		Bitmap bitmap = null;
		bitmap = Bitmap.createBitmap(imageView.getWidth(),
				imageView.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.BLACK);
		imageView.draw(canvas);
		return bitmap;
	}

	/**
	 * get bitmap from image path
	 * 
	 * @param scale
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getBitmapFromPath(Context context, int scale,
			String imagePath) {

		Uri uri = Uri.parse(imagePath);

		float rotation = getRotationInDegreeOfImage(context, uri);

		Matrix matrix = new Matrix();
		if (rotation != 0f) {
			matrix.preRotate(rotation);
		}

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = scale;

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

		if (rotation != 0f) {
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		}

		return bitmap;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap getVideoFrame(String uri) {
		Log.d(TAG,"uri video: " + uri);
		if (Build.VERSION.SDK_INT <= 10) {
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(uri);
			return retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,e.toString());
		} catch (RuntimeException e) {
			Log.e(TAG,e.toString());
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				Log.e(TAG,e.toString());
			}
		}
		return null;
	}

	public static Bitmap getVideoThumb(String uri) {
		return ThumbnailUtils.createVideoThumbnail(uri,
				Images.Thumbnails.FULL_SCREEN_KIND);
	}

	/**
	 * Create circle bitmap from other bitmap
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bmp, int size,
			int borderColor) {
		Bitmap bitmap = Bitmap.createScaledBitmap(bmp, size, size, false);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int radius = Math.min(h / 2, w / 2);
		Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);
		p.setColor(Color.WHITE);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, p);
		p.setXfermode(null);
		p.setStyle(Style.STROKE);
		p.setColor(borderColor);
		p.setStrokeWidth(3);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		return output;
	}

	/**
	 * Create round corner from other bitmap
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Convert bitmap to byte array, used for save bitmap to database
	 */
	public static byte[] convertBitmapToBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, outputStream);
		return outputStream.toByteArray();
	}

	public static String convertBitmapToBase64(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		byte[] byteArrayImage = convertBitmapToBytes(bitmap);
		String encodedImage = Base64.encodeToString(byteArrayImage,
				Base64.DEFAULT);
		return encodedImage;
	}

	public static Bitmap convertBase64ToBitmap(String input) {
		if (TextUtils.isEmpty(input)) {
			return null;
		}
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	/**
	 * Convert byte array to bitmap
	 * 
	 * @param array
	 * @return
	 */
	public static Bitmap getByteArrayAsBitmap(byte[] array) {
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	/******************************** AsycTask for load large image *************************/

	public static class BitmapLargeManager {

		private static BitmapLargeManager instance;
		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache; // key is resource id
		private AsyncTask<Integer, Void, Boolean> mLoadBitmapTask;

		public static BitmapLargeManager getInstance(Context context) {
			if (instance == null) {
				instance = new BitmapLargeManager(context);
			}
			return instance;
		}

		public BitmapLargeManager(Context context) {
			this.mContext = context;
			initMemoryCache();
		}

		private void initMemoryCache() {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			Log.d(TAG,"maxMemory = " + maxMemory);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory / 8;

			mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(Integer key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}

		/**
		 * Load bitmap from cache
		 * 
		 * @param resId
		 *            resource id of bitmap
		 * @return
		 */
		public Bitmap getBitmapFromCache(int resId) {
			return mMemoryCache.get(resId);
		}

		/**
		 * Remove bitmap from cache
		 * 
		 * @param resId
		 *            rersource id of bitmap
		 */
		public void removeBitmap(int resId) {
			if (mMemoryCache.get(resId) != null) {
				mMemoryCache.remove(resId);
				System.gc();
			}
		}

		/**
		 * Load large bitmap
		 * 
		 * @param resId
		 *            resource id of bitmap
		 * @param listener
		 *            called when load bitmap finish
		 */
		public void loadLargeBimap(final int resId, final int width,
				final int height, final LoadBitmapListener listener) {

			if (mLoadBitmapTask != null) {
				Log.e(TAG,"Loading other bitmap...");
				return;
			}
			mLoadBitmapTask = new AsyncTask<Integer, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(Integer... params) {
					try {
						Bitmap largeBitmap;
						int size = params.length;
						for (int i = 0; i < size; i++) {
							if (mMemoryCache.get(params[i]) != null) {
								// exist bitmap in cache
								continue;
							}
							largeBitmap = BitmapUtil.scaleBitmap(
									mContext.getResources(), params[i], width,
									height);
							mMemoryCache.put(params[i], largeBitmap);
						}

					} catch (OutOfMemoryError e) {
						Log.e(TAG,e.toString());
						return false;
					} catch (Exception e) {
						Log.e(TAG,e.toString());
						return false;
					}
					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					mLoadBitmapTask = null;
					if (listener != null) {
						listener.onLoadFinish(resId, result);
					}
				}

			};
			mLoadBitmapTask.execute(resId);
		}

		/**
		 * Called when finish load large bitmap
		 */
		public interface LoadBitmapListener {
			void onLoadFinish(int id, boolean isSuccess);
		}
	}

	/**
	 * 
	 * @param radius
	 * @param color
	 * @return
	 */
	public static Bitmap createCircle(int radius, int color) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);

		int width = radius * 2;
		int height = radius * 2;

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawCircle(radius, radius, radius, paint);
		return bitmap;
	}

	/**
	 * write bitmap from drawable to file sdcard
	 * 
	 * @param context
	 * @param nameResource
	 * @param pathOfImageToWrite
	 * @throws java.io.IOException
	 */
	public static void writeBitmapFromDrawable(Context context,
			String nameResource, String pathOfImageToWrite) throws IOException {
		// get id of bitmap from drawable folder
		int id = context.getResources().getIdentifier(nameResource, "drawable",
				context.getPackageName());
		// get bitmap from id
		Bitmap bitmap = BitmapFactory
				.decodeResource(context.getResources(), id);
		if (bitmap != null) {
			// write bitmap
			writeBitmap(bitmap, pathOfImageToWrite, 100);
		}
	}

	public static void writeBitmapFromDrawable(Context context, int resId,
			String pathOfImageToWrite) throws IOException {
		
		// get bitmap from id
		Bitmap bitmap = BitmapFactory
				.decodeResource(context.getResources(), resId);
		if (bitmap != null) {
			// write bitmap
			writeBitmap(bitmap, pathOfImageToWrite, 100);
		}
	}

	/**
	 * wrire bitmap to file sdcard
	 * 
	 * @param bitmap
	 * @param pathOfImage
	 * @param quality
	 * @throws java.io.IOException
	 */
	public static void writeBitmap(Bitmap bitmap, String pathOfImage,
			int quality) throws IOException {
		// create new file in sd card
		File fileImage = new File(pathOfImage);
		fileImage.createNewFile();
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, quality, arrayOutputStream);

		FileOutputStream fos = new FileOutputStream(fileImage);
		fos.write(arrayOutputStream.toByteArray());
		fos.close();
	}
    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
